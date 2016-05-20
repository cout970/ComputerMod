package com.cout970.computer.emulator;

import com.cout970.computer.api.IPeripheralDiskDrive;
import com.cout970.computer.api.IStorageDevice;
import com.cout970.computer.util.ObjectScannerKt;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Created by cout970 on 08/02/2016.
 */
public class PeripheralDiskDrive implements IPeripheralDiskDrive {

    protected ItemStack disk;
    protected byte[] diskBuffer;//4k bytes
    protected int sector;
    protected int regAction = -1;
    protected int address = 0x2;
    protected int accessTime = 0;
    protected TileEntity tile;

    public PeripheralDiskDrive(TileEntity t) {
        tile = t;
    }

    @Override
    public byte[] getRawBuffer() {
        if (diskBuffer == null || diskBuffer.length != getSectorSize()) diskBuffer = new byte[getSectorSize()];
        return diskBuffer;
    }

    @Override
    public int getSectorSize() {
        return 0x1000;// 4KB
    }

    @Override
    public int getSector() {
        return sector;
    }

    @Override
    public void setSector(int sector) {
        this.sector = sector;
    }

    @Override
    public void readToBuffer() {
        ItemStack item = getDisk();
        IStorageDevice storage = getStorage();
        File f = storage.getAssociateFile(item);
        if (f == null) return;
        RandomAccessFile acces = null;
        try {
            acces = new RandomAccessFile(f, "r");
            acces.seek(getSector() * getSectorSize());
            int readed = acces.read(getRawBuffer());
            regAction = 0;
        } catch (IOException e) {
            regAction = -1;
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
        ItemStack item = getDisk();
        IStorageDevice storage = getStorage();
        File f = storage.getAssociateFile(item);
        if (f == null) return;
        RandomAccessFile acces = null;
        try {
            acces = new RandomAccessFile(f, "rw");
            acces.seek(getSector() * getSectorSize());
            acces.write(getRawBuffer());
            regAction = 0;
        } catch (IOException e) {
            regAction = -1;
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
    public ItemStack getDisk() {
        return disk;
    }

    @Override
    public IStorageDevice getStorage() {
        ItemStack i = getDisk();
        if (i == null) return null;
        return ObjectScannerKt.findInItem(i.getItem(), IStorageDevice.IDENTIFIER, null);
    }

    @Override
    public boolean setDisk(ItemStack i) {
        if (i == null) {
            disk = null;
        } else {
            IStorageDevice device = ObjectScannerKt.findInItem(i.getItem(), IStorageDevice.IDENTIFIER, null);
            if (device != null && device.isCompatible(i, this)) {
                disk = i;
                return true;
            }
        }
        return false;
    }

    @Override
    public void load(NBTTagCompound data) {
        NBTTagCompound nbt = data.getCompoundTag("DiskDrive");
        diskBuffer = nbt.getByteArray("Buffer").clone();
        sector = nbt.getInteger("Sector");
        address = nbt.getInteger("Address");
        regAction = nbt.getInteger("Action");
        accessTime = nbt.getInteger("AccessTime");
    }

    @Override
    public void save(NBTTagCompound data) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setByteArray("Buffer", getRawBuffer());
        nbt.setInteger("Sector", sector);
        nbt.setInteger("Address", address);
        nbt.setInteger("Action", regAction);
        nbt.setInteger("AccessTime", accessTime);
        data.setTag("DiskDrive", nbt);
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
    public byte readByte(int pointer) {
        switch (pointer) {
            case 0://0 byte active
                return (byte) (isActive() ? 1 : 0);
            case 1://1 regAction
                return (byte) regAction;
            case 2://2 finished
                return (byte) (accessTime <= 0 && regAction == -1 ? 1 : 0);
            case 3://3 disk
                return (byte) (getDisk() == null ? 0 : 1);
            case 4://4-8 sector
                return (byte) (sector >>> 24);
            case 5:
                return (byte) (sector >>> 16);
            case 6:
                return (byte) (sector >>> 8);
            case 7:
                return (byte) (sector);
            case 8://8-c sector size
                return (byte) (getSectorSize() >>> 24);
            case 9:
                return (byte) (getSectorSize() >>> 16);
            case 10:
                return (byte) (getSectorSize() >>> 8);
            case 11:
                return (byte) (getSectorSize());
        }
        //c-100c buffer
        if (pointer >= 12 && pointer < 12 + getSectorSize()) {
            return getRawBuffer()[pointer - 12];
        }
        return 0;
    }

    @Override
    public void writeByte(int pointer, byte data) {
        switch (pointer) {
            case 0://0 byte active
                return;
            case 1://1 regAction
                regAction = data & 0xFF;
                return;
            case 2://2 finished
            case 3://3 disk
                return;
            case 4://4-8 sector
                sector = (sector & 0x00FFFFFF) | (data << 24);
                return;
            case 5:
                sector = (sector & 0xFF00FFFF) | (data << 16);
                return;
            case 6:
                sector = (sector & 0xFFFF00FF) | (data << 8);
                return;
            case 7:
                sector = (sector & 0xFFFFFF00) | data;
                return;
            case 8://8-c sector size
            case 9:
            case 10:
            case 11:
                return;
        }
        //c-100c buffer
        if (pointer >= 12 && pointer <= 12 + getSectorSize()) {
            getRawBuffer()[pointer - 12] = data;
        }
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
            if (regAction != -1 && getDisk() != null) {
                IStorageDevice drive = getStorage();
                if (regAction != 0) {
                    accessTime = drive.getAccessTime(getDisk());
                } else {
                    regAction = -1;
                }
                if (regAction == 1) {
                    //copy the disk label in the buffer
                    Arrays.fill(getRawBuffer(), (byte) 0);
                    byte[] label;
                    try {
                        label = drive.getDiskLabel(getDisk()).getBytes("US-ASCII");
                        System.arraycopy(label, 0, getRawBuffer(), 0, Math.min(label.length, 128));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    regAction = 0;
                } else if (regAction == 2) {
                    //set the disk label from the buffer
                    int length;
                    for (length = 0; getRawBuffer()[length] != 0 && length < 64; length++) {
                    }
                    String label;
                    try {
                        label = new String(getRawBuffer(), 0, length, "US-ASCII");
                        drive.setDiskLabel(getDisk(), label);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    regAction = 0;
                } else if (regAction == 3) {
                    //copy in the buffer the serial number
                    Arrays.fill(getRawBuffer(), (byte) 0);
                    drive.getAssociateFile(getDisk());
                    byte[] serial;
                    try {
                        serial = drive.getSerialNumber(getDisk()).getBytes("US-ASCII");
                        System.arraycopy(serial, 0, getRawBuffer(), 0, Math.min(serial.length, 128));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    regAction = 0;
                } else if (regAction == 4) {
                    //reads the file and copy it into the buffer
                    if (sector > drive.getSize(getDisk())) {
                        regAction = -1;
                        return;
                    }
                    readToBuffer();
                } else if (regAction == 5) {
                    //write the content of the buffer to the file
                    if (sector > drive.getSize(getDisk())) {
                        regAction = -1;
                        return;
                    }
                    writeFromBuffer();
                } else {
                    regAction = -1;
                }
            }
        } else {
            accessTime--;
        }
    }

    @Override
    public String getName() {
        return "DiskDrive";
    }

}
