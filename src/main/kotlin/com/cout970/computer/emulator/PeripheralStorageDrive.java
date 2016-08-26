package com.cout970.computer.emulator;

import com.cout970.computer.api.IPeripheralStorageDrive;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 * Created by cout970 on 08/02/2016.
 */
public abstract class PeripheralStorageDrive implements IPeripheralStorageDrive {

    protected byte[] diskBuffer;//4k bytes
    protected int address;
    protected int regAction = -1;
    protected int regErrorCode = 0;
    protected int regSector;
    protected int accessTime = 0;
    protected TileEntity tile;

    public PeripheralStorageDrive(TileEntity t, int addr) {
        tile = t;
        address = addr;
    }

    @Override
    public byte[] getRawBuffer() {
        if (diskBuffer == null || diskBuffer.length != getSectorSize()) { diskBuffer = new byte[getSectorSize()]; }
        return diskBuffer;
    }

    @Override
    public int getSectorSize() {
        return 0x1000;// 4KB
    }

    @Override
    public int getSector() {
        return regSector;
    }

    @Override
    public void setSector(int sector) {
        this.regSector = sector;
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
        return !tile.isInvalid();
    }

    @Override
    public byte[] peripheralData() {
        return new byte[0];
    }

    @Override
    public BlockPos getPosition() {
        return tile.getPos();
    }

    @Override
    public World getWorld() {
        return tile.getWorld();
    }

    /**
     * regAction:
     * -1 no action
     * 0 waiting
     * 1 copy the disk label into the buffer
     * 2 set the disk label from the buffer
     * 3 copy the serial number into the buffer
     * 4 reads the file and copy it into the buffer
     * 5 write the content of the buffer to the file
     */
    @Override
    public void iterate() {
        if (accessTime <= 0) {
            if (regAction != -1) {
                runAction();
            }
        } else {
            accessTime--;
        }
    }

    protected abstract void runAction();

    protected boolean checkSector() {
        if (regSector > getTotalSize()) {
            regAction = -1;
            regErrorCode = 3;
            return true;
        } else if (regSector < 0) {
            regAction = -1;
            regErrorCode = 4;
            return true;
        }
        return false;
    }

    @Override
    public void readToBuffer() {
        File f = getStorageFile();
        if (f == null) { return; }
        RandomAccessFile acces = null;
        try {
            acces = new RandomAccessFile(f, "r");
            acces.seek(getSector() * getSectorSize());
            int read = acces.read(getRawBuffer());
            if (read == -1) {
                Arrays.fill(getRawBuffer(), (byte)0);
            } else if (read < getSectorSize()) {
                for (int i = read; i < getSectorSize(); i++) {
                    getRawBuffer()[i] = 0;
                }
            }
            regAction = 0;
        } catch (IOException e) {
            regAction = -1;
            regErrorCode = 2;
            e.printStackTrace();
        } finally {
            try {
                if (acces != null) {
                    acces.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void writeFromBuffer() {
        File f = getStorageFile();
        if (f == null) { return; }
        RandomAccessFile acces = null;
        try {
            acces = new RandomAccessFile(f, "rw");
            acces.seek(getSector() * getSectorSize());
            acces.write(getRawBuffer());
            regAction = 0;
        } catch (IOException e) {
            regAction = -1;
            regErrorCode = 2;
            e.printStackTrace();
        } finally {
            try {
                if (acces != null) {
                    acces.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected abstract File getStorageFile();

    protected abstract int getTotalSize();

    @Override
    public void load(NBTTagCompound nbt) {
        diskBuffer = nbt.getByteArray("Buffer").clone();
        regSector = nbt.getInteger("Sector");
        address = nbt.getInteger("Address");
        regAction = nbt.getInteger("Action");
        accessTime = nbt.getInteger("AccessTime");
        regErrorCode = nbt.getInteger("ErrorCode");
    }

    @Override
    public void save(NBTTagCompound nbt) {
        nbt.setByteArray("Buffer", getRawBuffer());
        nbt.setInteger("Sector", regSector);
        nbt.setInteger("Address", address);
        nbt.setInteger("Action", regAction);
        nbt.setInteger("AccessTime", accessTime);
        nbt.setInteger("ErrorCode", regErrorCode);
    }
}
