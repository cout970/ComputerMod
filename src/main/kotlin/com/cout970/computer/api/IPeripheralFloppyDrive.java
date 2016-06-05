package com.cout970.computer.api;


import net.minecraft.item.ItemStack;

/**
 * @author cout970
 */
public interface IPeripheralFloppyDrive extends IPeripheralStorageDrive {

	/**
	 * Returns the disk on the drive
	 */
	ItemStack getDisk();

	IStorageDevice getStorage();

	boolean setDisk(ItemStack disk);

	/**
	 * Checks if the disk is being reading or writing something and waiting for it to finish
	 *
	 * @return true if the disk is waiting for something to finish, false otherwise
     */
	boolean isWaiting();
}
