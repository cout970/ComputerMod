package com.cout970.computer.api;

import net.minecraft.nbt.NBTTagCompound;

/**
 * @author cout970
 */
public interface IModule {

	/**
	 * The name of the module
	 * @return
	 */
	String getName();

	/**
	 * Saves and loads data related to the module: registers, buffers and any other data
	 */
	void load(NBTTagCompound data);

	void save(NBTTagCompound data);
}
