package com.cout970.computer.emulator.exception;

import com.cout970.computer.api.IExceptionCPU;

/**
 * Created by cout970 on 03/06/2016.
 */
public class ArithmeticException implements IExceptionCPU {

    @Override
    public int getCode() {
        return 2;
    }

    @Override
    public String getDescription() {
        return "Attemp to divide by 0";
    }

    @Override
    public String getName() {
        return "ARITHMETIC EXCEPTION";
    }
}
