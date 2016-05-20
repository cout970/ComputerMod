package com.cout970.computer2.tileentity;

import com.cout970.computer.api.*;
import com.cout970.computer.emulator.*;
import com.cout970.computer2.network.ChannelManager;
import com.cout970.computer.network.IButtonListener;
import com.cout970.computer2.network.MessageButtonClick;
import net.darkaqua.blacksmith.api.Game;
import net.darkaqua.blacksmith.api.common.intermod.IInterfaceIdentifier;
import net.darkaqua.blacksmith.api.common.intermod.IInterfaceProvider;
import net.darkaqua.blacksmith.api.common.intermod.InterModUtils;
import net.darkaqua.blacksmith.api.common.inventory.IItemStack;
import net.darkaqua.blacksmith.api.common.inventory.defaults.SimpleInventoryHandler;
import net.darkaqua.blacksmith.api.common.storage.IDataCompound;
import net.darkaqua.blacksmith.api.common.util.Direction;
import net.darkaqua.blacksmith.api.common.util.WorldRef;

/**
 * Created by cout970 on 15/11/2015.
 */
public class TileComputer extends TileBase implements IComputer, IInterfaceProvider, IButtonListener {

    private SimpleInventoryHandler inv = new SimpleInventoryHandler(6){
        @Override
        public void setStackInSlot(int slot, IItemStack stack) {
            inventory[slot] = stack;
            if (slot == 0 && isAssembled && Game.isServer()){
                diskDrive.setDisk(stack);
            }
        }
    };
    private IModuleCPU cpu;
    private IModuleRAM ram;
    private IModuleROM rom;
    private IPeripheralDiskDrive diskDrive;
    private boolean isAssembled;

    public TileComputer() {
    }

    @Override
    public void update() {
        if (Game.isServer()) {
            if (isAssembled) {
                cpu.iterate();
                diskDrive.iterate();
            } else if (parent.getWorldRef().getWorld().getWorldTime() % 40 == 0) {
                assemble();
            }
        }
    }

    private void assemble() {
        //DEBUG
        cpu = new ModuleCPU_MIPS();
        ram = new ModuleRAM(0x10000, false, 1);
        rom = new ModuleROM();
        IModuleMMU mmu = new ModuleMMU();
        cpu.setMemory(ram);
        cpu.setMMU(mmu);
        mmu.setRAM(ram);
        mmu.setCPU(cpu);
        mmu.setComputer(this);
        diskDrive = new PeripheralDiskDrive(this);
        if (inv.getStackInSlot(0) != null){
            diskDrive.setDisk(inv.getStackInSlot(0));
        }
        isAssembled = true;
    }

    @Override
    public IModuleCPU getCPU() {
        return cpu;
    }

    @Override
    public IModuleRAM getMemory() {
        return ram;
    }

    @Override
    public IModule[] getModules() {
        return new IModule[]{diskDrive};
    }

    @Override
    public WorldRef getWorldRef() {
        return parent.getWorldRef();
    }

    @Override
    public void loadData(IDataCompound data) {
        super.loadData(data);
        inv.load(data, "inv");
        if (isAssembled) {
            cpu.load(data);
            ram.load(data);
            diskDrive.load(data);
        }
    }

    @Override
    public void saveData(IDataCompound data) {
        super.saveData(data);
        inv.save(data, "inv");
        if (!isAssembled) {
            assemble();
        }
        if (isAssembled) {
            cpu.save(data);
            ram.save(data);
            diskDrive.save(data);
        }
    }

    @Override
    public boolean hasInterface(IInterfaceIdentifier<?> id, Direction side) {
        return InterModUtils.matches(IComputer.IDENTIFIER, id);
    }

    @Override
    public <T> T getInterface(IInterfaceIdentifier<T> identifier, Direction side) {
        return (T) this;
    }

    public SimpleInventoryHandler getInv() {
        return inv;
    }

    @Override
    public void onPress(int buttonId) {
        if (Game.isClient()) {
            MessageButtonClick msg = new MessageButtonClick(buttonId, getWorld().getWorldDimension(), getPosition());
            ChannelManager.channel.sendToServer(msg);
        } else {
            if (isAssembled) {
                if (buttonId == 0) {
                    cpu.start();
                } else if (buttonId == 1) {
                    cpu.reset();
                    rom.loadBios(ram);
                } else if (buttonId == 2) {
                    cpu.stop();
                }
            }
        }
    }
}
