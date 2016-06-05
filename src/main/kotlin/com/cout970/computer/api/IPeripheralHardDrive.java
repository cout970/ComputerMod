package com.cout970.computer.api;

import net.minecraft.item.ItemStack;

/**
 * Created by cout970 on 05/06/2016.
 */
public interface IPeripheralHardDrive extends IPeripheralStorageDrive {

    ItemStack getHardDrive();

    IStorageDevice getStorage();

    boolean setHardDrive(ItemStack drive);
}
