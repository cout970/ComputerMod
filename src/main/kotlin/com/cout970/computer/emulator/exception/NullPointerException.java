package com.cout970.computer.emulator.exception;

import com.cout970.computer.api.IExceptionCPU;

/**
 * Created by cout970 on 03/06/2016.
 */
public class NullPointerException implements IExceptionCPU {

    @Override
    public int getCode() {
        return 6;
    }

    @Override
    public String getDescription() {
        return "Attempt to jump to an invalid location (addr == 0)";
    }

    @Override
    public String getName() {
        return "NULL POINTER EXCEPTION";
    }
}
