package com.cout970.computer.item

import com.cout970.computer.api.IPeripheralStorageDrive
import com.cout970.computer.api.IStorageDevice
import com.cout970.computer.util.getFileFromItemStack
import com.cout970.computer.util.getString
import com.cout970.computer.util.sanityCheck
import com.cout970.computer.util.setString
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ICapabilityProvider
import java.io.File

/**
 * Created by cout970 on 21/05/2016.
 */
object ItemFloppyDisk : ItemBase("floppy_disk"), IStorageDevice, ICapabilityProvider {

    const val DISK_SIZE = 0x40000 //256kB

    override fun getAssociateFile(i: ItemStack): File {
        sanityCheck(i)
        return getFileFromItemStack(i)!!
    }

    override fun getDiskLabel(i: ItemStack): String {
        return "" + i.getString("Label")
    }

    override fun getSectorSize(i: ItemStack): Int {
        return 0x1000
    }

    override fun getSectorCount(i: ItemStack): Int {
        return (Math.ceil(DISK_SIZE.toDouble()) / getSectorSize(i)).toInt()
    }

    override fun isCompatible(i: ItemStack, drive: IPeripheralStorageDrive): Boolean {
        return drive.sectorSize == getSectorSize(i)
    }

    override fun getSerialNumber(i: ItemStack): String {
        return "" + i.getString("SerialNumber")
    }

    override fun getSize(i: ItemStack): Int {
        return DISK_SIZE
    }

    override fun getAccessTime(i: ItemStack): Int {
        return 5
    }

    override fun setDiskLabel(i: ItemStack, label: String) {
        i.setString("Label", label)
    }

    override fun <T : Any?> getCapability(capability: Capability<T>?, facing: EnumFacing?): T {
        return this as T
    }

    override fun hasCapability(capability: Capability<*>?, facing: EnumFacing?): Boolean {
        return capability == IStorageDevice.IDENTIFIER
    }
}