package com.cout970.computer.emulator.exception;

import com.cout970.computer.api.IExceptionCPU;

/**
 * Created by cout970 on 03/06/2016.
 */
public class WordBoundaryException implements IExceptionCPU {

    @Override
    public int getCode() {
        return 4;
    }

    @Override
    public String getDescription() {
        return "Attempt to write or read a word with and address not aligned with the word boundary ((addr & 3) != 0)";
    }

    @Override
    public String getName() {
        return "READ/WRITE NOT ALIGNED WITH WORD BOUNDARIES";
    }
}
