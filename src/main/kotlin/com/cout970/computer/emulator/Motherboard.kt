package com.cout970.computer.emulator

import com.cout970.computer.api.IModuleCPU
import com.cout970.computer.api.IModuleRAM
import com.cout970.computer.api.IModuleROM
import com.cout970.computer.api.IPeripheralDiskDrive
import com.cout970.computer.tileentity.TileOldComputer
import net.minecraftforge.items.ItemStackHandler

/**
 * Created by cout970 on 20/05/2016.
 */
class Motherboard(
        var parent: TileOldComputer,
        var cpu: IModuleCPU? = null,
        var ram: IModuleRAM? = null,
        var rom: IModuleROM? = null,
        var diskDrive: IPeripheralDiskDrive? = null,
        var hddDrive0: IPeripheralDiskDrive? = null,
        var hddDrive1: IPeripheralDiskDrive? = null
) : ItemStackHandler(6) {

    var isAssembled = false

    override fun onContentsChanged(slot: Int) {
        if (slot == 0 && diskDrive != null) {
            diskDrive?.disk = getStackInSlot(slot);
        } else if (slot == 1 && hddDrive0 != null) {
            hddDrive0?.disk = getStackInSlot(slot);
        } else if (slot == 2 && hddDrive1 != null) {
            hddDrive1?.disk = getStackInSlot(slot);
        }
    }

    fun iterate() {
        cpu?.iterate()
        diskDrive?.iterate()
        hddDrive0?.iterate()
        hddDrive1?.iterate()
    }

    fun assembly() {
        //DEBUG
        cpu = ModuleCPU_MIPS()
        ram = ModuleRAM(0x10000, false, 1)
        rom = ModuleROM()
        val mmu = ModuleMMU()
        cpu!!.memory = ram
        cpu!!.mmu = mmu
        mmu.ram = ram
        mmu.cpu = cpu
        mmu.computer = parent
        diskDrive = PeripheralDiskDrive(parent)
        if (getStackInSlot(0) != null) {
            diskDrive!!.disk = getStackInSlot(0)
        }
        isAssembled = true
    }
}