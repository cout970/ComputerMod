package com.cout970.computer.api;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * @author cout970
 */
public interface IModule extends INBTSerializable<NBTTagCompound> {

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

	@Override
	default NBTTagCompound serializeNBT(){
		NBTTagCompound nbt = new NBTTagCompound();
		save(nbt);
		return nbt;
	}

	@Override
	default void deserializeNBT(NBTTagCompound nbt){
		save(nbt);
	}
}
