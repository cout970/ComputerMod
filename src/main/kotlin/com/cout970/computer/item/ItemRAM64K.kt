package com.cout970.computer.item

import com.cout970.computer.api.IComponentRAM
import com.cout970.computer.api.IComputer
import com.cout970.computer.api.IModuleRAM
import com.cout970.computer.emulator.ModuleRAM
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ICapabilityProvider

/**
 * Created by cout970 on 01/06/2016.
 */
object ItemRAM64K : ItemBase("64k_ram"), IComponentRAM, ICapabilityProvider {

    override fun createModule(item: ItemStack?, computer: IComputer?): IModuleRAM? = ModuleRAM(0x10000, false, 1)

    override fun loadModule(item: ItemStack?, computer: IComputer?, nbt: NBTTagCompound?): IModuleRAM? = ModuleRAM(0x10000, false, 1)

    override fun <T : Any?> getCapability(capability: Capability<T>?, facing: EnumFacing?): T = this as T

    override fun hasCapability(capability: Capability<*>?, facing: EnumFacing?): Boolean = capability == IComponentRAM.CAPABILITY
}