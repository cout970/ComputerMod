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
abstract class ItemStorageDevice(name: String) : ItemBase(name), IStorageDevice, ICapabilityProvider {

    abstract fun getDiskSize(): Int;

    override fun getAssociateFile(i: ItemStack): File {
        sanityCheck(i)
        return getFileFromItemStack(i)!!
    }

    override fun getDiskLabel(i: ItemStack): String {
        return "" + i.getString("Label")
    }

    override fun getSectorCount(i: ItemStack): Int {
        return (Math.ceil(getDiskSize().toDouble()) / getSectorSize(i)).toInt()
    }

    override fun isCompatible(i: ItemStack, drive: IPeripheralStorageDrive): Boolean {
        return drive.sectorSize == getSectorSize(i)
    }

    override fun getSerialNumber(i: ItemStack): String {
        return "" + i.getString("SerialNumber")
    }

    override fun getSize(i: ItemStack): Int {
        return getDiskSize()
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