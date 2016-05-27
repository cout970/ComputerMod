package com.cout970.computer.emulator;

import com.cout970.computer.api.IPeripheralMonitor;
import com.cout970.computer.network.IMessageStorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by cout970 on 31/12/2015.
 */
public class PeripheralMonitor implements IPeripheralMonitor, IMessageStorage {

    //the address used in the computer to access to this peripheral
    public static final int KEYBOARD_MASK = 0xff010000;
    protected TileEntity parent;
    protected int address = 0x1;
    protected int regKeyBufferPtr;
    protected int regKeyBufferSize;
    protected byte[] keyBuffer;
    protected int regMouseButton;
    protected int regMouseX;
    protected int regMouseY;
    protected int regCursor;
    protected int regCursorMark;
    protected byte[] buffer;
    protected boolean hasKeyPressed;
    protected int keyPressed;

    public PeripheralMonitor(TileEntity parent) {
        this.parent = parent;
    }

    @Override
    public String getName() {
        return "old_monitor";
    }

    @Override
    public int getAddress() {
        return address;
    }

    @Override
    public void setAddress(int address) {
        this.address = address;
    }

    @Override
    public boolean isActive() {
        return !parent.isInvalid();
    }

//DEVICE STRUCTURE

    //*    */        //device data
//* 00 */        byte active;            // active flag, shows if the device is connected;
//* 01 */        byte id;                // id, shows the type of the device, id: 0 Motherboard, id: 1 Monitor id: 2 Diskdrive id: 3-255 unknown
//*    */
//*    */        //keyboard data
//* 02 */        byte keyBufferPtr;      // saves the position of the keyBuffer the where the las key has been stored
//* 03 */        byte keyBufferSize;     // stores the amount of keys stored in the buffer, the max value is 8
//* 04 */        char keyBuffer[8];      // stores the keys pressed by the keyboard in a circular array, where keyBufferPtr indicates
//*    */                                // the first value and (keyBufferPtr+keyBufferSize)%8 indicates the last value
//*    */
//*    */        //mouse data
//* 12 */        int mouseButton;        // the button used by the mouse in the last click on the screen
//* 16 */        int mouseX;             // the xCoord of the last mouse click on the screen
//* 20 */        int mouseY;             // the yCoord of the last mouse click on the screen
//*    */
//*    */        //screen data
//* 24 */        int size;               // size of the screen (lines*columns)
//* 28 */        int lines;              // number of lines in the monitor
//* 32 */        int columns;            // size of the lines in the monitor
//*    */
//*    */        //text buffer
//* 36 */        int cursor;             // the position of the screen where the cursor should be displayed
//* 40 */        int cursorMark;         // the cursor that marks when the next char should be placed
//* 44 */        byte buffer[0];         // buffer stores the chars in the monitor, the size of the buffer may change
//*    */                                // between monitors, to get the size use Monitor.size

    @Override
    public byte readByte(int pointer) {
        switch (pointer) {
            case 0://0 byte active
                return (byte) (isActive() ? 1 : 0);
            case 1://1 byte id
                return (byte) 1;
            case 2://2 byte keyBufferPtr
                return (byte) regKeyBufferPtr;
            case 3://3 byte keyBufferSize
                return (byte) regKeyBufferSize;
            case 12://12 int mouse button
                return (byte) (regMouseButton >>> 24);
            case 13:
                return (byte) (regMouseButton >>> 16);
            case 14:
                return (byte) (regMouseButton >>> 8);
            case 15:
                return (byte) (regMouseButton);
            case 16://16 int mouse X
                return (byte) (regMouseX >>> 24);
            case 17:
                return (byte) (regMouseX >>> 16);
            case 18:
                return (byte) (regMouseX >>> 8);
            case 19:
                return (byte) (regMouseX);
            case 20://20 int mouse Y
                return (byte) (regMouseY >>> 24);
            case 21:
                return (byte) (regMouseY >>> 16);
            case 22:
                return (byte) (regMouseY >>> 8);
            case 23:
                return (byte) (regMouseY);
            case 24://24 int size
                return (byte) (getBufferSize() >>> 24);
            case 25:
                return (byte) (getBufferSize() >>> 16);
            case 26:
                return (byte) (getBufferSize() >>> 8);
            case 27:
                return (byte) (getBufferSize());
            case 28://28 int lines
                return (byte) (getLines() >>> 24);
            case 29:
                return (byte) (getLines() >>> 16);
            case 30:
                return (byte) (getLines() >>> 8);
            case 31:
                return (byte) (getLines());
            case 32://32 int columns
                return (byte) (getColumns() >>> 24);
            case 33:
                return (byte) (getColumns() >>> 16);
            case 34:
                return (byte) (getColumns() >>> 8);
            case 35:
                return (byte) (getColumns());
            case 36://36 int cursor
                return (byte) (regCursor >>> 24);
            case 37:
                return (byte) (regCursor >>> 16);
            case 38:
                return (byte) (regCursor >>> 8);
            case 39:
                return (byte) (regCursor);
            case 40://40 int cursor
                return (byte) (regCursorMark >>> 24);
            case 41:
                return (byte) (regCursorMark >>> 16);
            case 42:
                return (byte) (regCursorMark >>> 8);
            case 43:
                return (byte) (regCursorMark);
        }
        if (pointer >= 4 && pointer < 12) {//4 char keyBuffer[8]
            return getKeyBuffer()[pointer - 4];
        }
        pointer -= 44;
        if (pointer >= 0 && pointer < getBufferSize()) {
            return getBuffer()[pointer];
        }
        return 0;
    }

    @Override
    public void writeByte(int pointer, byte data) {
        switch (pointer) {
            case 0://0 byte active
            case 1://1 byte id
                return;
            case 2://2 byte keyBufferPtr
                regKeyBufferPtr = data;
                return;
            case 3://3 byte keyBufferSize
                regKeyBufferSize = data;
                return;
            case 12://12 int mouseButton
                regMouseButton = (regMouseButton & 0x00FFFFFF) | (data << 24);
                return;
            case 13:
                regMouseButton = (regMouseButton & 0xFF00FFFF) | (data << 16);
                return;
            case 14:
                regMouseButton = (regMouseButton & 0xFFFF00FF) | (data << 8);
                return;
            case 15:
                regMouseButton = (regMouseButton & 0xFFFFFF00) | data;
                return;
            case 16://16 int mouseX
                regMouseX = (regMouseX & 0x00FFFFFF) | (data << 24);
                return;
            case 17:
                regMouseX = (regMouseX & 0xFF00FFFF) | (data << 16);
                return;
            case 18:
                regMouseX = (regMouseX & 0xFFFF00FF) | (data << 8);
                return;
            case 19:
                regMouseX = (regMouseX & 0xFFFFFF00) | data;
                return;
            case 20://int mouseY
                regMouseY = (regMouseY & 0x00FFFFFF) | (data << 24);
                return;
            case 21:
                regMouseY = (regMouseY & 0xFF00FFFF) | (data << 16);
                return;
            case 22:
                regMouseY = (regMouseY & 0xFFFF00FF) | (data << 8);
                return;
            case 23:
                regMouseY = (regMouseY & 0xFFFFFF00) | data;
                return;
            case 36://36 int cursor
                regCursor = (regCursor & 0x00FFFFFF) | (data << 24);
                return;
            case 37:
                regCursor = (regCursor & 0xFF00FFFF) | (data << 16);
                return;
            case 38:
                regCursor = (regCursor & 0xFFFF00FF) | (data << 8);
                return;
            case 39:
                regCursor = (regCursor & 0xFFFFFF00) | data;
                return;
            case 40://40 int cursorMark
                regCursorMark = (regCursorMark & 0x00FFFFFF) | (data << 24);
                return;
            case 41:
                regCursorMark = (regCursorMark & 0xFF00FFFF) | (data << 16);
                return;
            case 42:
                regCursorMark = (regCursorMark & 0xFFFF00FF) | (data << 8);
                return;
            case 43:
                regCursorMark = (regCursorMark & 0xFFFFFF00) | data;
                return;
        }
        if (pointer >= 4 && pointer < 12) {//4 char keyBuffer[8]
            getKeyBuffer()[pointer - 4] = data;
        }
        pointer -= 44;
        if (pointer >= 0 && pointer < getBufferSize()) {
            getBuffer()[pointer] = data;
        }
    }

    @Override
    public byte[] getBuffer() {
        if (buffer == null || buffer.length != 4000) buffer = new byte[4000];
        return buffer;
    }

    public byte[] getKeyBuffer() {
        if (keyBuffer == null || keyBuffer.length != 8) keyBuffer = new byte[8];
        return keyBuffer;
    }

    @Override
    public int getChar(int pos) {
        return getBuffer()[pos];
    }

    @Override
    public void setChar(int pos, byte character) {
        getBuffer()[pos] = character;
    }

    @Override
    public void onKeyPressed(int key) {
        hasKeyPressed = true;
        keyPressed = key;
    }

    @Override
    public void onKeyRelease(int key) {
    }

    @Override
    public void onCursorClick(int x, int y, int button) {
        regMouseX = x;
        regMouseY = y;
        regMouseButton = 1 << button;
    }

    @Override
    public int getBufferSize() {
        return 4000;
    }

    @Override
    public int getCursorPos() {
        return regCursor;
    }

    public void setCursorPos(int pos) {
        regCursor = pos;
    }

    @Override
    public int getLines() {
        return 50;
    }

    @Override
    public int getColumns() {
        return 80;
    }

    @Override
    public byte[] peripheralData() {
        return new byte[0];
    }

    @Override
    public BlockPos getPosition() {
        return parent.getPos();
    }

    @Override
    public World getWorld() {
        return parent.getWorld();
    }


    @Override
    public void load(NBTTagCompound main) {
        NBTTagList list = main.getTagList("OldMonitor", 11);
        NBTTagCompound nbt = list.getCompoundTagAt(0);
        address = nbt.getInteger("Address");
        regKeyBufferPtr = nbt.getInteger("KeyBufferPtr");
        regKeyBufferSize = nbt.getInteger("KeyBufferSize");
        keyBuffer = nbt.getByteArray("KeyBuffer");
        regMouseButton = nbt.getInteger("MouseButton");
        regMouseX = nbt.getInteger("MouseX");
        regMouseY = nbt.getInteger("MouseY");
        regCursor = nbt.getInteger("Cursor");
        regCursorMark = nbt.getInteger("CursorMark");
        buffer = nbt.getByteArray("Buffer").clone();
        getBuffer();
    }

    @Override
    public void save(NBTTagCompound main) {
        NBTTagList list = new NBTTagList();
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("Address", address);
        nbt.setInteger("KeyBufferPtr", regKeyBufferPtr);
        nbt.setInteger("KeyBufferSize", regKeyBufferSize);
        nbt.setByteArray("KeyBuffer", keyBuffer);
        nbt.setInteger("MouseButton", regMouseButton);
        nbt.setInteger("MouseX", regMouseX);
        nbt.setInteger("MouseY", regMouseY);
        nbt.setInteger("Cursor", regCursor);
        nbt.setInteger("CursorMark", regCursorMark);
        nbt.setByteArray("Buffer", getBuffer());
        list.appendTag(nbt);
        main.setTag("OldMonitor", list);
    }

    @Override
    public void saveToMessage(ByteBuf buff, Side processSide) {
        buff.writeByte(hasKeyPressed ? 1 : 0);
        buff.writeByte(keyPressed);
        buff.writeByte(regMouseButton);
        buff.writeInt(regMouseX);
        buff.writeInt(regMouseY);
    }

    @Override
    public void loadFromMessage(ByteBuf buff, Side processSide) {
        int hasPressed = buff.readByte();
        int key  = buff.readByte();

        if(hasPressed == 1) {
            if (regKeyBufferSize != getKeyBuffer().length) {
                System.out.println((regKeyBufferPtr + regKeyBufferSize) % keyBuffer.length + " " + regKeyBufferSize);
                getKeyBuffer()[(regKeyBufferPtr + regKeyBufferSize) % keyBuffer.length] = (byte) key;
                regKeyBufferSize++;
            }
        }

        regMouseButton = buff.readInt();
        regMouseX = buff.readInt();
        regMouseY = buff.readInt();
    }
}
