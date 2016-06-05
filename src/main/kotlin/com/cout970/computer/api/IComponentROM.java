package com.cout970.computer.api;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

/**
 * Created by cout970 on 03/06/2016.
 */
public interface IComponentROM {

    @CapabilityInject(IComponentROM.class)
    Capability<IComponentROM> CAPABILITY = null;

    /**
     * Creates a rom module for the computer using the this item
     *
     * @param item the item used to create the module
     * @param computer the computer where the module will be installed
     * @return the rom module assigned to the item
     */
    IModuleROM createModule(ItemStack item, IComputer computer);

    /**
     * Loads the rom module from an NBT
     * This method will be called from readFromNBT() in TileEntity, so IComputer.getWorld() will give a null value
     * This method doesn't have to deserialize the nbt and restore the data in the module
     *
     * @param item the item used to load the component
     * @param computer the computer there the component is loaded
     * @param nbt the NBTTagCompound used to load the component
     * @return the rom module
     */
    IModuleROM loadModule(ItemStack item, IComputer computer, NBTTagCompound nbt);
}
