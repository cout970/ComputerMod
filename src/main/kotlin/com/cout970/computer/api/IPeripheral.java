package com.cout970.computer.api;


import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author cout970
 */
public interface IPeripheral extends IModule{

	/**
	 * @return The bus ID of this peripheral
	 */
	int getAddress();

	void setAddress(int address);

	/**
	 * @return If the peripheral is on or off
	 */
	boolean isActive();

	/**
	 * Reads or writes a single byte into a register or a buffer on this peripheral
	 * @param pointer
	 *            The position of the byte, inside of the bounds (0-4096)
	 */
	byte readByte(int pointer);

	void writeByte(int pointer, byte data);

	/**
	 * Gives information about the peripheral, this is used in the drivers to identify the peripheral and to
	 * know values like the version or to store the code of the driver to allow plug and play.
	 * @return A buffer with data to be read by programs
	 */
	byte[] peripheralData();

	/**
	 * @return Gives the position of the block where the peripheral is.
	 */
	BlockPos getPosition();

	/**
	 * @return Gives the world where the peripheral is.
	 */
	World getWorld();
}
