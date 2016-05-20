package com.cout970.computer.api;


import net.minecraft.item.ItemStack;

/**
 * @author cout970
 */
public interface IPeripheralDiskDrive extends IPeripheralStorageDrive {

	/**
	 * Returns the disk on the drive
	 */
	ItemStack getDisk();

	IStorageDevice getStorage();

	boolean setDisk(ItemStack disk);
}
