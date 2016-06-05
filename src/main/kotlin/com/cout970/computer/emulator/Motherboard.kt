package com.cout970.computer.emulator

import com.cout970.computer.api.*
import com.cout970.computer.tileentity.TileOldComputer
import com.cout970.computer.util.findInItem
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.items.ItemStackHandler

/**
 * Created by cout970 on 20/05/2016.
 */
class Motherboard(
        var parent: TileOldComputer,
        var cpu: IModuleCPU? = null,
        var ram: IModuleRAM? = null,
        var rom: IModuleROM? = null,
        var diskDrive: IPeripheralFloppyDrive? = null,
        var hddDrive0: IPeripheralHardDrive? = null,
        var hddDrive1: IPeripheralHardDrive? = null
) : ItemStackHandler(6), IMotherboard {

    var isAssembled = false
    var changed = true

    override fun onContentsChanged(slot: Int) {
        if (slot == 0 && diskDrive != null) {
            diskDrive?.disk = getStackInSlot(slot);
        } else if (slot == 1 && hddDrive0 != null) {
            hddDrive0?.hardDrive = getStackInSlot(slot);
        } else if (slot == 2 && hddDrive1 != null) {
            hddDrive1?.hardDrive = getStackInSlot(slot);
        } else if (slot == 3 && isAssembled) {
            cpu = null;
            isAssembled = false;
        } else if (slot == 4 && isAssembled) {
            ram = null;
            cpu?.stop()
            isAssembled = false;
        } else if (slot == 5 && isAssembled) {
            rom = null;
            cpu?.stop()
            isAssembled = false;
        }
        changed = true
    }

    fun iterate() {
        cpu?.iterate()
        diskDrive?.iterate()
        hddDrive0?.iterate()
        hddDrive1?.iterate()
    }

    fun assembly() {

        //cpu
        if (getStackInSlot(3) != null && (changed || cpu == null)) {
            val comp0 = findInItem(getStackInSlot(3).item, IComponentCPU.CAPABILITY, null)
            if (comp0 != null) {
                cpu = comp0.createModule(getStackInSlot(3), parent)
            }
        }

        //ram
        if (getStackInSlot(4) != null && (changed || ram == null)) {
            val comp0 = findInItem(getStackInSlot(4).item, IComponentRAM.CAPABILITY, null)
            if (comp0 != null) {
                ram = comp0.createModule(getStackInSlot(4), parent)
            }
        }

        //rom
        if (getStackInSlot(5) != null && (changed || rom == null)) {
            val comp0 = findInItem(getStackInSlot(5).item, IComponentROM.CAPABILITY, null)
            if (comp0 != null) {
                rom = comp0.createModule(getStackInSlot(5), parent)
            }
        }
        if (cpu != null && ram != null && rom != null) {
            val mmu = ModuleMMU()
            cpu!!.memory = ram
            cpu!!.mmu = mmu
            mmu.ram = ram
            mmu.cpu = cpu
            mmu.computer = parent
            diskDrive = PeripheralFloppyDrive(parent)
            if (getStackInSlot(0) != null) {
                diskDrive!!.disk = getStackInSlot(0)
            }
            hddDrive0 = PeripheralHardDrive(parent)
            if (getStackInSlot(1) != null) {
                hddDrive0!!.hardDrive = getStackInSlot(1)
            }
            hddDrive1 = PeripheralHardDrive(parent)
            if (getStackInSlot(2) != null) {
                hddDrive1!!.hardDrive = getStackInSlot(2)
            }
            isAssembled = true
        } else {
            isAssembled = false;
        }
        changed = false;
    }

    fun assembly(nbt: NBTTagCompound) {
        //cpu
        if (getStackInSlot(3) != null && cpu == null) {
            val comp0 = findInItem(getStackInSlot(3).item, IComponentCPU.CAPABILITY, null)
            if (comp0 != null) {
                cpu = comp0.loadModule(getStackInSlot(3), parent, nbt)
            }
        }

        //ram
        if (getStackInSlot(4) != null && ram == null) {
            val comp0 = findInItem(getStackInSlot(3).item, IComponentRAM.CAPABILITY, null)
            if (comp0 != null) {
                ram = comp0.loadModule(getStackInSlot(3), parent, nbt)
            }
        }

        //rom
        if (getStackInSlot(5) != null && rom == null) {
            val comp0 = findInItem(getStackInSlot(3).item, IComponentROM.CAPABILITY, null)
            if (comp0 != null) {
                rom = comp0.loadModule(getStackInSlot(3), parent, nbt)
            }
        }
        if (cpu != null && ram != null && rom != null) {
            val mmu = ModuleMMU()
            cpu!!.memory = ram
            cpu!!.mmu = mmu
            mmu.ram = ram
            mmu.cpu = cpu
            mmu.computer = parent
            diskDrive = PeripheralFloppyDrive(parent)
            if (getStackInSlot(0) != null) {
                diskDrive!!.disk = getStackInSlot(0)
            }
            hddDrive0 = PeripheralHardDrive(parent)
            if (getStackInSlot(1) != null) {
                hddDrive0!!.hardDrive = getStackInSlot(1)
            }
            hddDrive1 = PeripheralHardDrive(parent)
            if (getStackInSlot(2) != null) {
                hddDrive1!!.hardDrive = getStackInSlot(2)
            }
            isAssembled = true
        } else {
            isAssembled = false;
        }
    }

    override fun getStackLimit(slot: Int, stack: ItemStack): Int = 1

    override fun getCPU(): IModuleCPU? = cpu

    override fun getMemory(): IModuleRAM? = ram

    override fun getROM(): IModuleROM? = rom

    override fun deserializeNBT(nbt: NBTTagCompound?) {
        super.deserializeNBT(nbt)
        assembly(nbt!!)
        ram?.load(nbt)
        cpu?.load(nbt)
        if (nbt.hasKey("FloppyDrive"))
            diskDrive?.deserializeNBT(nbt.getCompoundTag("FloppyDrive"))
        if (nbt.hasKey("HardDrive0"))
            hddDrive0?.deserializeNBT(nbt.getCompoundTag("HardDrive0"))
        if (nbt.hasKey("HardDrive1"))
            hddDrive1?.deserializeNBT(nbt.getCompoundTag("HardDrive1"))

        for (i in 0..slots - 1)
            onContentsChanged(i);
    }

    override fun serializeNBT(): NBTTagCompound? {
        val nbt = super.serializeNBT()
        ram?.save(nbt)
        cpu?.save(nbt)
        if (diskDrive != null) {
            nbt.setTag("FloppyDrive", diskDrive!!.serializeNBT())
        }
        if (hddDrive0 != null) {
            nbt.setTag("HardDrive0", hddDrive0!!.serializeNBT())
        }
        if (hddDrive1 != null) {
            nbt.setTag("HardDrive1", hddDrive1!!.serializeNBT())
        }
        return nbt
    }
}