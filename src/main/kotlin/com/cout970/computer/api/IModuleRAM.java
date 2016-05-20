package com.cout970.computer.api;

/**
 * @author cout970
 */
public interface IModuleRAM extends IModule {

    /**
     * Access to an specific byte in the memory
     *
     * @param addr physical memory address
     */

    byte readByte(int addr);

    void writeByte(int addr, byte data);

    /**
     * This operation uses the Endianness to write the byte properly if the address is not multiple of 4 an
     * exception will occur
     */
    void writeWord(int addr, int data);

    int readWord(int addr);

    /**
     * https://en.wikipedia.org/wiki/Endianness
     */
    boolean isLittleEndian();

    void setLittleEndian(boolean little);

    /**
     * The amount of byte on the memory
     */
    int getMemorySize();

    /**
     * Sets all the memory to 0, this may take a lot of time
     */
    void clear();
}
