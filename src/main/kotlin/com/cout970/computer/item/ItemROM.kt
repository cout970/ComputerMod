package com.cout970.computer.item

import com.cout970.computer.api.IComponentROM
import com.cout970.computer.api.IComputer
import com.cout970.computer.api.IModuleROM
import com.cout970.computer.emulator.ModuleROM
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ICapabilityProvider

/**
 * Created by cout970 on 01/06/2016.
 */
object ItemROM : ItemBase("rom"), IComponentROM, ICapabilityProvider {

    override fun createModule(item: ItemStack?, computer: IComputer?): IModuleROM? = ModuleROM()

    override fun loadModule(item: ItemStack?, computer: IComputer?, nbt: NBTTagCompound?): IModuleROM? = ModuleROM()

    override fun <T : Any?> getCapability(capability: Capability<T>?, facing: EnumFacing?): T = this as T

    override fun hasCapability(capability: Capability<*>?, facing: EnumFacing?): Boolean = capability == IComponentROM.CAPABILITY
}