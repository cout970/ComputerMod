package com.cout970.computer.item

import com.cout970.computer.api.IComponentCPU
import com.cout970.computer.api.IComputer
import com.cout970.computer.api.IModuleCPU
import com.cout970.computer.emulator.ModuleCPU_MIPS
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ICapabilityProvider

/**
 * Created by cout970 on 01/06/2016.
 */
object ItemCPUMips : ItemBase("mips_cpu"), IComponentCPU, ICapabilityProvider {

    override fun createModule(item: ItemStack?, computer: IComputer?): IModuleCPU? = ModuleCPU_MIPS()

    override fun loadModule(item: ItemStack?, computer: IComputer?, nbt: NBTTagCompound?): IModuleCPU? = ModuleCPU_MIPS()

    override fun <T : Any?> getCapability(capability: Capability<T>?, facing: EnumFacing?): T = this as T

    override fun hasCapability(capability: Capability<*>?, facing: EnumFacing?): Boolean = capability == IComponentCPU.CAPABILITY
}