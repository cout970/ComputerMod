package com.cout970.computer.api;

/**
 * Created by cout970 on 04/06/2016.
 */
public interface IMotherboard {

    /**
     * The CPU of the computer
     */
    IModuleCPU getCPU();

    /**
     * The main memory of the computer
     */
    IModuleRAM getMemory();

    /**
     * The ROM module of the computer
     */
    IModuleROM getROM();
}
