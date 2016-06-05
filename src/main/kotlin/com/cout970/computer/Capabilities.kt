package com.cout970.computer

import com.cout970.computer.api.IComponentCPU
import com.cout970.computer.api.IComponentRAM
import com.cout970.computer.api.IComponentROM
import com.cout970.computer.item.ItemCPUMips
import com.cout970.computer.item.ItemRAM64K
import com.cout970.computer.item.ItemROM
import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.Capability.IStorage
import net.minecraftforge.common.capabilities.CapabilityManager

/**
 * Created by cout970 on 03/06/2016.
 */


fun registerCapabilities(){
    CapabilityManager.INSTANCE.register(IComponentCPU::class.java, EmptyStorage(), { ItemCPUMips })
    CapabilityManager.INSTANCE.register(IComponentRAM::class.java, EmptyStorage(), { ItemRAM64K })
    CapabilityManager.INSTANCE.register(IComponentROM::class.java, EmptyStorage(), { ItemROM })
}


class EmptyStorage<T> : IStorage<T>{

    override fun readNBT(capability: Capability<T>?, instance: T?, side: EnumFacing?, nbt: NBTBase?) {}

    override fun writeNBT(capability: Capability<T>?, instance: T?, side: EnumFacing?): NBTBase? = NBTTagCompound()
}