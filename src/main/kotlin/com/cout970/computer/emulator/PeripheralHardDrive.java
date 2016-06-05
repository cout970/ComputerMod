package com.cout970.computer.emulator;

import com.cout970.computer.api.IPeripheralHardDrive;
import com.cout970.computer.api.IStorageDevice;
import com.cout970.computer.util.ObjectScannerKt;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Created by cout970 on 05/06/2016.
 */
public class PeripheralHardDrive extends PeripheralStorageDrive implements IPeripheralHardDrive {

    private ItemStack hardDrive;

    public PeripheralHardDrive(TileEntity t) {
        super(t, 3);
    }

    @Override
    protected void runAction() {
        if (getHardDrive() == null) {
            regErrorCode = 1;
            return;
        }
        IStorageDevice drive = getStorage();
        if (regAction != 0) {
            accessTime = drive.getAccessTime(getHardDrive());
        } else {
            regAction = -1;
        }
        if (regAction == 1) {
            //copy the disk label in the buffer
            Arrays.fill(getRawBuffer(), (byte) 0);
            byte[] label;
            try {
                label = drive.getDiskLabel(getHardDrive()).getBytes("US-ASCII");
                System.arraycopy(label, 0, getRawBuffer(), 0, Math.min(label.length, 128));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                regErrorCode = 2;
            }
            regAction = 0;
        } else if (regAction == 2) {
            //set the disk label from the buffer
            int length;
            for (length = 0; getRawBuffer()[length] != 0 && length < 64;) {
                length++;
            }
            String label;
            try {
                label = new String(getRawBuffer(), 0, length, "US-ASCII");
                drive.setDiskLabel(getHardDrive(), label);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                regErrorCode = 2;
            }
            regAction = 0;
        } else if (regAction == 3) {
            //copy in the buffer the serial number
            Arrays.fill(getRawBuffer(), (byte) 0);
            drive.getAssociateFile(getHardDrive());
            byte[] serial;
            try {
                serial = drive.getSerialNumber(getHardDrive()).getBytes("US-ASCII");
                System.arraycopy(serial, 0, getRawBuffer(), 0, Math.min(serial.length, 128));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                regErrorCode = 2;
            }
            regAction = 0;
        } else if (regAction == 4) {
            //reads the file and copy it into the buffer
            if (checkSector()) { return; }
            readToBuffer();
        } else if (regAction == 5) {
            //write the content of the buffer to the file
            if (checkSector()) { return; }
            writeFromBuffer();
        } else {
            regAction = -1;
        }
    }

    @Override
    protected File getStorageFile() {
        return getStorage().getAssociateFile(getHardDrive());
    }

    @Override
    protected int getTotalSize() {
        return getHardDrive() == null ? 0 : getStorage().getSize(getHardDrive());
    }

    protected int getSectorCount(){
        return getStorage().getSectorCount(getHardDrive());
    }

    @Override
    public boolean isActive() {
        return !tile.isInvalid() && hardDrive != null;
    }

    @Override
    public String getName() {
        return "HardDrive";
    }

    @Override
    public ItemStack getHardDrive() {
        return hardDrive;
    }

    @Override
    public IStorageDevice getStorage() {
        ItemStack i = getHardDrive();
        if (i == null) { return null; }
        return ObjectScannerKt.findInItem(i.getItem(), IStorageDevice.IDENTIFIER, null);
    }

    @Override
    public boolean setHardDrive(ItemStack i) {
        if (i == null) {
            hardDrive = null;
        } else {
            IStorageDevice device = ObjectScannerKt.findInItem(i.getItem(), IStorageDevice.IDENTIFIER, null);
            if (device != null && device.isCompatible(i, this)) {
                hardDrive = i;
                return true;
            }
        }
        return false;
    }

    //DEVICE STRUCTURE

    //*    */    //device data
    //* 00 */    byte active;        // active flag, shows if the device is connected and if there is a storage device in the hard drive slot
    //* 01 */    byte id;            // id, shows the type of the device, id: 0 Motherboard, id: 1 Monitor id: 2 FloppyDrive id: 3 HardDrive
    //*    */
    //*    */    //controls
    //* 02 */    byte action;        // the action that the drive should perform
    //* 03 */    byte finished;      // stores a 0 if the last action has finished or the number of ticks remaining to finish the action
    //* 04 */    int errorCode;     // stores the error code of the last action, 0 means no error
    //*    */
    //*    */    //disk data
    //* 08 */    int totalSize;       // the space in the hard drive
    //* 12 */    int numBlocks;      // the number of block in the hard drive
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
                return (byte) 3;
            case 2://2 byte action
                return (byte) regAction;
            case 3://3 byte finished
                return (byte) (accessTime <= 0 && regAction == -1 ? 0 : accessTime);
            case 4://4 int errorCode
                return (byte) (getSectorSize() >>> 24);
            case 5:
                return (byte) (getSectorSize() >>> 16);
            case 6:
                return (byte) (getSectorSize() >>> 8);
            case 7:
                return (byte) (getSectorSize());
            case 8://8 int disk Size
                return (byte) (getTotalSize() >>> 24);
            case 9:
                return (byte) (getTotalSize() >>> 16);
            case 10:
                return (byte) (getTotalSize() >>> 8);
            case 11:
                return (byte) (getTotalSize());
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
                if (getHardDrive() == null) {
                    regErrorCode = 1;
                    return;
                }
                accessTime = 0;
                return;
            case 3://3 byte finished
                return;
            case 4://4 int error code
                regErrorCode = (regErrorCode & 0x00FFFFFF) | (data << 24);
                return;
            case 5:
                regErrorCode = (regErrorCode & 0xFF00FFFF) | (data << 16);
                return;
            case 6:
                regErrorCode = (regErrorCode & 0xFFFF00FF) | (data << 8);
                return;
            case 7:
                regErrorCode = (regErrorCode & 0xFFFFFF00) | data;
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
}
