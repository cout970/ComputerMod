package com.cout970.computer.emulator;

import com.cout970.computer.api.IModuleRAM;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Arrays;

public class ModuleRAM implements IModuleRAM {

    private byte[][] memory;
    private boolean littleEndian;
    private int size;
    private int modules;

    public ModuleRAM(int size, boolean little, int modules) {
        this.modules = modules;
        memory = new byte[modules][];
        for (int i = 0; i < modules; i++) {
            memory[i] = new byte[size / modules];
        }
        this.size = size;
        littleEndian = little;
    }

    public int readWord(int pos) {
        int data;
        if (littleEndian) {
            data = (readByte(pos + 3) & 0xFF);
            data |= (readByte(pos + 2) & 0xFF) << 8;
            data |= (readByte(pos + 1) & 0xFF) << 16;
            data |= (readByte(pos) & 0xFF) << 24;
        } else {
            data = (readByte(pos) & 0xFF);
            data |= (readByte(pos + 1) & 0xFF) << 8;
            data |= (readByte(pos + 2) & 0xFF) << 16;
            data |= (readByte(pos + 3) & 0xFF) << 24;
        }
        return data;
    }

    public byte readByte(int pos) {
        if (pos < 0 || pos >= getMemorySize())
            return 0;
        byte data = memory[pos / memory[0].length][pos % memory[0].length];
//        Log.debug(String.format("readByte: addr: 0x%x, byte: %x, %d", pos, data, data));
        return data;
    }

    public void writeByte(int pos, byte data) {
        if (pos < 0 || pos >= getMemorySize())
            return;
//        Log.debug(String.format("writeByte: addr: 0x%x, byte: %x, %d", pos, data, data));
        memory[pos / memory[0].length][pos % memory[0].length] = data;
    }

    public void writeWord(int pos, int data) {
        if (littleEndian) {
            writeByte(pos + 3, (byte) (data & 0x000000FF));
            writeByte(pos + 2, (byte) ((data & 0x0000FF00) >> 8));
            writeByte(pos + 1, (byte) ((data & 0x00FF0000) >> 16));
            writeByte(pos, (byte) ((data & 0xFF000000) >> 24));
        } else {
            writeByte(pos, (byte) (data & 0x000000FF));
            writeByte(pos + 1, (byte) ((data & 0x0000FF00) >> 8));
            writeByte(pos + 2, (byte) ((data & 0x00FF0000) >> 16));
            writeByte(pos + 3, (byte) ((data & 0xFF000000) >> 24));
        }
    }

    @Override
    public boolean isLittleEndian() {
        return littleEndian;
    }

    @Override
    public void setLittleEndian(boolean little) {
        littleEndian = little;
    }

    @Override
    public int getMemorySize() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < modules; i++) {
            Arrays.fill(memory[i], (byte) 0);
        }
    }

    @Override
    public String getName() {
        return "ram";
    }

    @Override
    public void load(NBTTagCompound nbt) {
        modules = nbt.getInteger("Modules_RAM");
        size = nbt.getInteger("Size");
        memory = new byte[modules][];
        for (int i = 0; i < modules; i++) {
            memory[i] = nbt.getByteArray("Module_RAM_" + i);
            if (memory[i].length != size)
                memory[i] = new byte[size];
        }
    }

    @Override
    public void save(NBTTagCompound nbt) {
        nbt.setInteger("Modules_RAM", modules);
        nbt.setInteger("Size", size);
        for (int i = 0; i < modules; i++) {
            nbt.setByteArray("Module_RAM_" + i, memory[i]);
        }
    }

    /**
     *
     * Useless old systems
     *
     * OLD MEMORY MAP
     * 0x0000 -> on/off flag
     * 0x0004 -> Initial PC on start
     * 0x0004 - 0x1FFC -> ROM code
     * 0x1000 - 0X1FFC -> Stack
     * 0x2000 - 0X2FFF -> OS and programs
     * 0x3000 - 0XEFFF -> Data storage
     * 0XF000 -> Keyboard press bit
     * 0XF004 -> Keyboard press char in ASCII
     * 0xF008 -> Monitor cursor position
     * 0XF00C - 0xf010 -> Monitor mouse coords
     * 0XF014 -> Monitor Text
     * 0XFFB4 -> IO Expander
     *
     *
     * NEW MEMORY MAP (old)
     * 0x0000 -> hardware I/O on/off(1 byte), auto shutdown(1 byte), halt(1 byte), restart(1 byte), time(4 bytes), memory size(4 bytes),
     * peripheral (1 byte), is peripheral conneted(1 byte),  etc
     * 0x0010 - 0x03FC -> stack 1024-20 code 0x1FFF
     * 0x0400 - memory size -> OS, programs and data storage
     * IO devices using IBusConectable
     * 0x00FF0000 -> bus mask Address
     * 0xFF000000 -> HardDrive Disk
     * 0xFF010000 -> KeyBoard and mouse && Text Monitor
     * 0xFF020000 -> Floppy Disk
     * 0xFF030000 -> IO Expander
     * 0xFF040000 -> Color Monitor
     * 0xFF050000 - 0xFFFF0000 -> IO devices using IBusConectable get (Addreds * 0x00010000) | 0xFF000000
     */
}
