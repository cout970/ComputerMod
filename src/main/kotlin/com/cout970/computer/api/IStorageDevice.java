package com.cout970.computer.api;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import java.io.File;

/**
 * @author cout970
 */
public interface IStorageDevice {

	@CapabilityInject(IStorageDevice.class)
	Capability<IStorageDevice> IDENTIFIER = null;

	/**
	 * If returns null, an IOException has occurred
	 * @param item
	 *            the ItemStack linked to the data
	 * @return the file that stores the information of this device
	 */
	File getAssociateFile(ItemStack item);

	/**
	 * This method returns the name of the disk that is going to be displayed on the item, also can be modify
	 * using a PC. Can be null.
	 * @param i
	 *            the ItemStack linked to the data
	 * @return the disk label
	 */
	String getDiskLabel(ItemStack i);

	void setDiskLabel(ItemStack i, String label);

	/**
	 * It's a 16 digits number in hexadecimal. Must not be null and must no change in execution.
	 * @param i
	 *            the ItemStack linked to the data
	 * @return the unique serial number used to get the save file, "disk_"+serialNumber+".img"
	 */
	String getSerialNumber(ItemStack i);

	/**
	 * Returns the number of bytes in the device
	 */
	int getSize(ItemStack i);

	/**
	 * The size of the sectors in the device
	 */
	int getSectorSize(ItemStack i);

	/**
	 * Returns the number of sectors in this device
	 */
	int getSectorCount(ItemStack i);

	/**
	 * Returns the amount of GAME ticks of delay to read or write in the disk
	 */
	int getAccessTime(ItemStack i);

	/**
	 * Returns true if this drive can be used by the IModuleStorageDrive
	 */
	boolean isCompatible(ItemStack i, IPeripheralStorageDrive drive);
}
