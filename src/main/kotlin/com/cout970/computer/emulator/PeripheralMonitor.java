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
    protected byte[] buffer;
    protected int address = 0x1;
    protected int regReady;
    protected int regChar;
    protected int regCursor;
    protected int regCursorClick;
    protected int regCursorPosX;
    protected int regCursorPosY;

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

    @Override
    public byte readByte(int pointer) {
        switch (pointer) {
            case 0:// active byte
                return (byte) (isActive() ? 1 : 0);
            case 1:// key pulsed flag
                return (byte) regReady;
            case 2:// key pulsed code
                return (byte) regChar;
            case 3:// cursor clicked button
                // 0: none, 1: left, 2: right, 3: center
                return (byte) regCursorClick;
            case 4:// lines
                return (byte) (getLines() >>> 24);
            case 5:
                return (byte) (getLines() >>> 16);
            case 6:
                return (byte) (getLines() >>> 8);
            case 7:
                return (byte) (getLines());
            case 8:// columns
                return (byte) (getColumns() >>> 24);
            case 9:
                return (byte) (getColumns() >>> 16);
            case 10:
                return (byte) (getColumns() >>> 8);
            case 11:
                return (byte) (getColumns());
            case 12:// cursor
                return (byte) (regCursor >>> 24);
            case 13:
                return (byte) (regCursor >>> 16);
            case 14:
                return (byte) (regCursor >>> 8);
            case 15:
                return (byte) (regCursor);
            case 16:// cursor click X
                return (byte) (regCursorPosX >>> 24);
            case 17:
                return (byte) (regCursorPosX >>> 16);
            case 18:
                return (byte) (regCursorPosX >>> 8);
            case 19:
                return (byte) (regCursorPosX);
            case 20:// cursor click Y
                return (byte) (regCursorPosY >>> 24);
            case 21:
                return (byte) (regCursorPosY >>> 16);
            case 22:
                return (byte) (regCursorPosY >>> 8);
            case 23:
                return (byte) (regCursorPosY);
        }
        if (pointer >= 24 && pointer < 24 + getBufferSize()) {
            return getBuffer()[pointer - 24];
        }
        return 0;
    }

    @Override
    public void writeByte(int pointer, byte data) {
        switch (pointer) {
            case 0:
                return;
            case 1:
                regReady = data;
                return;
            case 2:
                regChar = data;
                return;
            case 3:
                regCursorClick = data;
                return;
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
                return;
            case 12:// cursor
                regCursor = (regCursor & 0x00FFFFFF) | (data << 24);
                return;
            case 13:
                regCursor = (regCursor & 0xFF00FFFF) | (data << 16);
                return;
            case 14:
                regCursor = (regCursor & 0xFFFF00FF) | (data << 8);
                return;
            case 15:
                regCursor = (regCursor & 0xFFFFFF00) | data;
                return;
            case 16:// cursorX
                regCursorPosX = (regCursorPosX & 0x00FFFFFF) | (data << 24);
                return;
            case 17:
                regCursorPosX = (regCursorPosX & 0xFF00FFFF) | (data << 16);
                return;
            case 18:
                regCursorPosX = (regCursorPosX & 0xFFFF00FF) | (data << 8);
                return;
            case 19:
                regCursorPosX = (regCursorPosX & 0xFFFFFF00) | data;
                return;
            case 20:// cursorY
                regCursorPosY = (regCursorPosY & 0x00FFFFFF) | (data << 24);
                return;
            case 21:
                regCursorPosY = (regCursorPosY & 0xFF00FFFF) | (data << 16);
                return;
            case 22:
                regCursorPosY = (regCursorPosY & 0xFFFF00FF) | (data << 8);
                return;
            case 23:
                regCursorPosY = (regCursorPosY & 0xFFFFFF00) | data;
                return;
        }
        if (pointer >= 24 && pointer < 24 + getBufferSize()) {
            getBuffer()[pointer - 24] = data;
        }
    }

    @Override
    public byte[] getBuffer() {
        if (buffer == null || buffer.length != 4000) buffer = new byte[4000];
        return buffer;
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
        regReady |= 1;
        regChar = key;
    }

    @Override
    public void onKeyRelease(int key) {
    }

    @Override
    public void onCursorClick(int x, int y, int button) {
        regCursorPosX = x;
        regCursorPosY = y;
        regCursorClick = 1 << button;
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
        regChar = nbt.getInteger("Char");
        regCursor = nbt.getInteger("Cursor");
        regCursorClick = nbt.getInteger("CursorClick");
        regCursorPosX = nbt.getInteger("CursorX");
        regCursorPosY = nbt.getInteger("CursorY");
        regReady = nbt.getInteger("Ready");
        buffer = nbt.getByteArray("Buffer").clone();
        getBuffer();
    }

    @Override
    public void save(NBTTagCompound main) {
        NBTTagList list = new NBTTagList();
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("Address", address);
        nbt.setInteger("Char", regChar);
        nbt.setInteger("Cursor", regCursor);
        nbt.setInteger("CursorClick", regCursorClick);
        nbt.setInteger("CursorX", regCursorPosX);
        nbt.setInteger("CursorY", regCursorPosY);
        nbt.setInteger("Ready", regReady);
        nbt.setByteArray("Buffer", getBuffer());
        list.appendTag(nbt);
        main.setTag("OldMonitor", list);
    }

    @Override
    public void saveToMessage(ByteBuf buff, Side processSide) {
        buff.writeByte(regReady);
        buff.writeByte(regChar);
        buff.writeByte(regCursorClick);
        buff.writeInt(regCursorPosX);
        buff.writeInt(regCursorPosY);
    }

    @Override
    public void loadFromMessage(ByteBuf buff, Side processSide) {
        regReady = buff.readByte();
        regChar = buff.readByte();
        regCursorClick = buff.readInt();
        regCursorPosX = buff.readInt();
        regCursorPosY = buff.readInt();
    }
}
