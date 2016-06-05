package com.cout970.computer.emulator.exception;

import com.cout970.computer.api.IExceptionCPU;

/**
 * Created by cout970 on 03/06/2016.
 */
public class Syscall implements IExceptionCPU {

    @Override
    public int getCode() {
        return 3;
    }

    @Override
    public String getDescription() {
        return "The current process has made a system call";
    }

    @Override
    public String getName() {
        return "SYSCALL";
    }
}
