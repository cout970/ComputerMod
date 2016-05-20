package com.cout970.computer.api;

public interface IPeripheralStorageDrive extends IPeripheral {

	/**
	 * Can't be null
	 * @return a byte buffer
	 */
	byte[] getRawBuffer();

	/**
	 * Thi returns the number of bytes in the internal buffer
	 */
	int getSectorSize();

	/**
	 * @return current sector to read or write
	 */
	int getSector();

	/**
	 * @param sector,
	 *            the sector to read or write
	 */
	void setSector(int sector);

	/**
	 * Read from the disk to the buffer using the current sector
	 */
	void readToBuffer();

	/**
	 * Write the buffer into the disk in the current sector
	 */
	void writeFromBuffer();

	void iterate();
}
