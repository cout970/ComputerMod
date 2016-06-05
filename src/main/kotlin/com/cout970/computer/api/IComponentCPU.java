package com.cout970.computer.api;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

/**
 * Created by cout970 on 03/06/2016.
 */
public interface IComponentCPU {

    @CapabilityInject(IComponentCPU.class)
    Capability<IComponentCPU> CAPABILITY = null;

    /**
     * Creates a cpu module for the computer using the this item
     *
     * @param item the item used to create the module
     * @param computer the computer where the module will be installed
     * @return the cpu module assigned to the item
     */
    IModuleCPU createModule(ItemStack item, IComputer computer);

    /**
     * Loads the cpu module from an NBT
     * This method will be called from readFromNBT() in TileEntity, so IComputer.getWorld() will give a null value
     * This method doesn't have to deserialize the nbt and restore the data in the module
     *
     * @param item the item used to load the component
     * @param computer the computer there the component is loaded
     * @param nbt the NBTTagCompound used to load the component
     * @return the cpu module
     */
    IModuleCPU loadModule(ItemStack item, IComputer computer, NBTTagCompound nbt);
}
