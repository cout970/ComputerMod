package com.cout970.computer.api;

/**
 * @author cout970
 */
public interface IModuleMMU extends IModule {

    int translate(int addr, boolean read, boolean write, boolean execute);

    byte readByte(int addr);

    void writeByte(int addr, byte data);

    void writeWord(int addr, int data);

    int readWord(int addr);

    int readInstruction(int addr);

    boolean isTranslationActive();

    void activeTranslation(boolean active);

    IModuleCPU getCPU();

    void setCPU(IModuleCPU cpu);

    IModuleRAM getRAM();

    void setRAM(IModuleRAM ram);

    IComputer getComputer();

    void setComputer(IComputer computer);
}
