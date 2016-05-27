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
    protected int address = 0x2;
    protected int regAction = -1;
    protected short regErrorCode = 0;
    protected int regSector;
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
        return regSector;
    }

    @Override
    public void setSector(int sector) {
        this.regSector = sector;
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

    private int getDiskSize() {
        return getDisk() == null ? 0 : getStorage().getSize(getDisk());
    }

    private int getSectorCount() {
        return getDisk() == null ? 0 : getStorage().getSectorCount(getDisk());
    }

//DEVICE STRUCTURE

//*    */    //device data
//* 00 */    byte active;        // active flag, shows if the device is connected;
//* 01 */    byte id;            // id, shows the type of the device, id: 0 Motherboard, id: 1 Monitor id: 2 Diskdrive id: 3-255 unknown
//*    */
//*    */    //controls
//* 02 */    byte action;        // the action that the drive should perform
//* 03 */    byte finished;      // stores a 0 if the last action has finished or the number of ticks remaining to finish the action
//* 04 */    uint16_t errorCode; // stores the error code of the last action, 0 means no error
//*    */
//*    */    //disk data
//* 06 */    byte disk;          // stores a 1 if the drive has a disk inside and 0 if not
//* 07 */    byte speed;         // the amount of tick to complete an action, if disk == 0 then this will be 0
//* 08 */    int diskSize;       // the space in the disk, if disk == 0 then this will be 0
//* 12 */    int numBlocks;      // the number of block in the disk, if disk == 0 then this will be 0
//*    */
//*    */    //access data
//* 16 */    int block;          // the current block loaded/to load
//* 20 */    int blockSize;      // the amount of bytes in a block
//* 24 */    byte buffer[0];     // the internal buffer of the drive, the size of the buffer is stored in blockSize
//*    */

    @Override
    public byte readByte(int pointer) {
        switch (pointer) {
            case 0://0 byte active
                return (byte) (isActive() ? 1 : 0);
            case 1://1 byte id
                return (byte) 2;
            case 2://2 byte action
                return (byte) regAction;
            case 3://3 byte finished
                return (byte) (accessTime <= 0 && regAction == -1 ? 0 : accessTime);
            case 4://4 short errorCode
                return (byte) (regErrorCode >>> 8);
            case 5:
                return (byte) (regErrorCode);
            case 6://6 byte disk
                return (byte) (getDisk() == null ? 0 : 1);
            case 7://7 byte speed
                return (byte) (getDisk() != null ? getStorage().getAccessTime(getDisk()) : 0);
            case 8://8 int disk Size
                return (byte) (getDiskSize() >>> 24);
            case 9:
                return (byte) (getDiskSize() >>> 16);
            case 10:
                return (byte) (getDiskSize() >>> 8);
            case 11:
                return (byte) (getDiskSize());
            case 12://12 int amount fo blocks/sectors
                return (byte) (getSectorCount() >>> 24);
            case 13:
                return (byte) (getSectorCount() >>> 16);
            case 14:
                return (byte) (getSectorCount() >>> 8);
            case 15:
                return (byte) (getSectorCount());
            case 16://12 int current blocks/sectors
                return (byte) (getSector() >>> 24);
            case 17:
                return (byte) (getSector() >>> 16);
            case 18:
                return (byte) (getSector() >>> 8);
            case 19:
                return (byte) (getSector());
            case 20://20 int blocks/sectors size
                return (byte) (getSectorSize() >>> 24);
            case 21:
                return (byte) (getSectorSize() >>> 16);
            case 22:
                return (byte) (getSectorSize() >>> 8);
            case 23:
                return (byte) (getSectorSize());
        }
        //24-(0x1000)+24 buffer
        pointer -= 24;
        if (pointer >= 0 && pointer < getSectorSize()) {
            return getRawBuffer()[pointer];
        }
        return 0;
    }

    @Override
    public void writeByte(int pointer, byte data) {
        switch (pointer) {
            case 0://0 byte active
                return;
            case 1://1 byte id
                return;
            case 2://2 byte action
                regAction = data & 0xFF;
                return;
            case 3://3 byte finished
                return;
            case 4://4 short regErrorCode
                regErrorCode = (short) ((regErrorCode & 0x00FF) | (data << 8));
                return;
            case 5:
                regErrorCode = (short) ((regErrorCode & 0xFF00) | data);
                return;
            case 6://6 byte disk
            case 7://7 byte speed
            case 8://8 int diskSize
            case 9:
            case 10:
            case 11:
            case 12://12 int numBlocks
            case 13:
            case 14:
            case 15:
                return;
            case 16://16 int block/sector
                regSector = (regSector & 0x00FFFFFF) | (data << 24);
                return;
            case 17:
                regSector = (regSector & 0xFF00FFFF) | (data << 16);
                return;
            case 18:
                regSector = (regSector & 0xFFFF00FF) | (data << 8);
                return;
            case 19:
                regSector = (regSector & 0xFFFFFF00) | data;
            case 20://20 int block/sector size
            case 21:
            case 22:
            case 23:
                return;
        }
        //24-(0x1000)+24 buffer
        pointer -= 24;
        if (pointer >= 0 && pointer < getSectorSize()) {
            getRawBuffer()[pointer] = data;
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
            if (regAction != -1) {
                if (getDisk() == null) {
                    regErrorCode = 1;
                    return;
                }
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
                        regErrorCode = 2;
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
                        regErrorCode = 2;
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
                        regErrorCode = 2;
                    }
                    regAction = 0;
                } else if (regAction == 4) {
                    //reads the file and copy it into the buffer
                    if (checkSector()) return;
                    readToBuffer();
                } else if (regAction == 5) {
                    //write the content of the buffer to the file
                    if (checkSector()) return;
                    writeFromBuffer();
                } else {
                    regAction = -1;
                }
            }
        } else {
            accessTime--;
        }
    }

    private boolean checkSector() {
        if (regSector > getDiskSize()) {
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
    public String getName() {
        return "DiskDrive";
    }

    @Override
    public void load(NBTTagCompound data) {
        NBTTagCompound nbt = data.getCompoundTag("DiskDrive");
        diskBuffer = nbt.getByteArray("Buffer").clone();
        regSector = nbt.getInteger("Sector");
        address = nbt.getInteger("Address");
        regAction = nbt.getInteger("Action");
        accessTime = nbt.getInteger("AccessTime");
        regErrorCode = nbt.getShort("ErrorCode");
    }

    @Override
    public void save(NBTTagCompound data) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setByteArray("Buffer", getRawBuffer());
        nbt.setInteger("Sector", regSector);
        nbt.setInteger("Address", address);
        nbt.setInteger("Action", regAction);
        nbt.setInteger("AccessTime", accessTime);
        nbt.setShort("ErrorCode", regErrorCode);
        data.setTag("DiskDrive", nbt);
    }

}
