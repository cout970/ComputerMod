package com.cout970.computer.emulator;

import com.cout970.computer.api.*;
import com.cout970.computer.util.ComputerUtilsKt;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by cout970 on 16/11/2015.
 */
public class ModuleMMU implements IModuleMMU {

    /**
     * vmpp peee oooo dddd dddd dddd dddd dddd
     * <--------------------------------------->
     * 32 bits
     * v: validity bit
     * m: modified bit
     * e: empty/unused
     * d: 20 bits, original address
     * o: 4 bits, replacement
     * p: 3 bits, protection
     */
    private int[] tlb = new int[8];
    private IComputer computer;
    private IModuleCPU cpu;
    private IModuleRAM ram;
    private boolean activeTranslation;
    private IPeripheral cache;

    public ModuleMMU() {
        tlb[0] = 0x80000000 | 0x00400 | 0x00100000;
        tlb[1] = 0x80000000 | 0x00401 | 0x00200000;
        tlb[2] = 0x80000000 | 0x00402 | 0x00300000;
        tlb[3] = 0x80000000 | 0x00403 | 0x00400000;
        tlb[4] = 0x80000000 | 0x10010 | 0x00500000;
        tlb[5] = 0x80000000 | 0x10011 | 0x00600000;
        tlb[6] = 0x80000000 | 0x00404 | 0x00700000;
        tlb[7] = 0x80000000 | 0x00405 | 0x00800000;
    }

    @Override
    public int translate(int addr, boolean read, boolean write, boolean execute) {
        int desc = addr >>> 12;
        for (int aTlb : tlb) {
            if ((aTlb & 0x80000000) != 0) {
                if ((aTlb & 0x000FFFFF) == desc) {
                    return (addr & 0x00000FFF) | ((aTlb & 0x00F00000) >>> 8);
                }
            }
        }
//        Log.debug(String.format("addr: %x, desc: %x", addr, desc));
        //cpu.throwException(4);
//        if((addr & 0x00400000) > 0){
//           return addr & 0xFFFF;
//        }
//        if ((addr & 0x10010000) > 0){
//            return (addr & 0xFFFF) + 0x4000;
//        }
        return addr & 0xFFFF;
    }

    @Override
    public byte readByte(int addr) {
        if ((addr & 0xFF000000) == 0xFF000000) {//peripheral
            if (getComputer() == null) return 0;
            int ext = (addr & 0x00FF0000) >> 16;

            if (cache == null || cache.getAddress() != ext) {
                cache = ComputerUtilsKt.getBusByAddress(getComputer().getWorld(), getComputer().getPosition(), ext);
            }
            if (cache == null) return 0;
            return cache.readByte(addr & 0xFFFF);
        }
        int taddr = activeTranslation ? translate(addr, true, false, false) : addr;
        return ram.readByte(taddr);
    }

    @Override
    public void writeByte(int addr, byte data) {
        if ((addr & 0xFF000000) == 0xFF000000) {//peripheral
            if (getComputer() == null) return;
            int ext = (addr & 0x00FF0000) >> 16;

            if (cache == null || cache.getAddress() != ext) {
                cache = ComputerUtilsKt.getBusByAddress(getComputer().getWorld(), getComputer().getPosition(), ext);
            }
            if (cache == null) return;
            cache.writeByte(addr & 0xFFFF, data);
            return;
        }
        int taddr = activeTranslation ? translate(addr, false, true, false) : addr;
        ram.writeByte(taddr, data);
    }

    @Override
    public void writeWord(int addr, int data) {
        if ((addr & 0xFF000000) == 0xFF000000) {//peripheral
            if (getComputer() == null) return;
            int ext = (addr & 0x00FF0000) >> 16;

            if (cache == null || cache.getAddress() != ext) {
                cache = ComputerUtilsKt.getBusByAddress(getComputer().getWorld(), getComputer().getPosition(), ext);
            }
            if (cache == null) return;
            cache.writeByte((addr + 3) & 0xFFFF, (byte) (data & 0x000000FF));
            cache.writeByte((addr + 2) & 0xFFFF, (byte) ((data & 0x0000FF00) >> 8));
            cache.writeByte((addr + 1) & 0xFFFF, (byte) ((data & 0x00FF0000) >> 16));
            cache.writeByte((addr) & 0xFFFF, (byte) ((data & 0xFF000000) >> 24));
            return;
        }
        int taddr = activeTranslation ? translate(addr, false, true, false) : addr;
        ram.writeWord(taddr, data);
    }

    @Override
    public int readWord(int addr) {
        if ((addr & 0xFF000000) == 0xFF000000) {//peripheral
            if (getComputer() == null) return 0;
            int ext = (addr & 0x00FF0000) >> 16;

            if (cache == null || cache.getAddress() != ext) {
                cache = ComputerUtilsKt.getBusByAddress(getComputer().getWorld(), getComputer().getPosition(), ext);
            }
            if (cache == null) return 0;
            int data = 0;
            data |= (cache.readByte((addr + 3) & 0xFFFF) & 0xFF);
            data |= (cache.readByte((addr + 2) & 0xFFFF) & 0xFF) << 8;
            data |= (cache.readByte((addr + 1) & 0xFFFF) & 0xFF) << 16;
            data |= (cache.readByte((addr) & 0xFFFF) & 0xFF) << 24;
            return data;
        }
        int taddr = activeTranslation ? translate(addr, true, false, false) : addr;
        return ram.readWord(taddr);
    }

    @Override
    public int readInstruction(int addr) {
        int taddr = activeTranslation ? translate(addr, false, false, true) : addr;
        return ram.readWord(taddr);
    }

    @Override
    public boolean isTranslationActive() {
        return activeTranslation;
    }

    @Override
    public void activeTranslation(boolean active) {
        activeTranslation = active;
    }

    @Override
    public void setCPU(IModuleCPU cpu) {
        this.cpu = cpu;
    }

    @Override
    public IModuleCPU getCPU() {
        return cpu;
    }

    @Override
    public void setRAM(IModuleRAM ram) {
        this.ram = ram;
    }

    @Override
    public IModuleRAM getRAM() {
        return ram;
    }

    public IComputer getComputer() {
        return computer;
    }

    public void setComputer(IComputer computer) {
        this.computer = computer;
    }

    @Override
    public String getName() {
        return "mmu";
    }

    @Override
    public void load(NBTTagCompound data) {
        tlb = data.getIntArray("tlb");
        if (tlb.length != 8) {
            tlb = new int[8];
        }
    }

    @Override
    public void save(NBTTagCompound data) {
        data.setIntArray("tlb", tlb);
    }
}
