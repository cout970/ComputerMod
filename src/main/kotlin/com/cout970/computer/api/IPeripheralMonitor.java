package com.cout970.computer.api;

/**
 * Created by cout970 on 07/02/2016.
 */
public interface IPeripheralMonitor extends IPeripheral {

    int getChar(int pos);

    void setChar(int pos, byte character);

    void onKeyPressed(int key);

    void onKeyRelease(int key);

    void onCursorClick(int x, int y, int button);

    int getBufferSize();

    byte[] getBuffer();

    int getCursorPos();

    int getLines();

    int getColumns();
}
