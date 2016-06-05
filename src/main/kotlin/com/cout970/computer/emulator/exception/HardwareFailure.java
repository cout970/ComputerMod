package com.cout970.computer.emulator.exception;

import com.cout970.computer.api.IExceptionCPU;

/**
 * Created by cout970 on 03/06/2016.
 */
public class HardwareFailure implements IExceptionCPU {

    @Override
    public int getCode() {
        return 0;
    }

    @Override
    public String getDescription() {
        return "An unknown error has occurred with some hardware component";
    }

    @Override
    public String getName() {
        return "HARDWARE ERROR";
    }
}
