package com.cout970.computer.api;

/**
 * Created by cout970 on 03/06/2016.
 */
public interface IExceptionCPU {

    /**
     * 6: invalid jump point, null pointer exception
     * 5: not aligned with word boundary
     * 4: tlb miss
     * 3: syscall/trap
     * 2: arithmetic exception, divided by 0
     * 1: invalid instruction
     * 0: external (hardware failure)
     */
    int getCode();

    String getDescription();

    String getName();
}
