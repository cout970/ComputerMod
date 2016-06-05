package com.cout970.computer.emulator.exception;

import com.cout970.computer.api.IExceptionCPU;

/**
 * Created by cout970 on 03/06/2016.
 */
public class InvalidInstruction implements IExceptionCPU {

    @Override
    public int getCode() {
        return 1;
    }

    @Override
    public String getDescription() {
        return "The CPU has found an unknown instruction";
    }

    @Override
    public String getName() {
        return "INVALID INSTRUCTION";
    }
}
