package com.cout970.computer2.items;

import com.cout970.computer.api.IPeripheralStorageDrive;
import com.cout970.computer.api.IStorageDevice;
import net.darkaqua.blacksmith.api.common.intermod.IInterfaceIdentifier;
import net.darkaqua.blacksmith.api.common.intermod.IInterfaceProvider;
import net.darkaqua.blacksmith.api.common.intermod.InterModUtils;
import net.darkaqua.blacksmith.api.common.inventory.IItemStack;
import net.darkaqua.blacksmith.api.common.util.Direction;

import java.io.File;

/**
 * Created by cout970 on 08/02/2016.
 */
public class ItemFloppyDisk extends ItemBase implements IStorageDevice, IInterfaceProvider {

    public static final int DISK_SIZE = 0x40000; //256kB

    public ItemFloppyDisk() {
        super("unamed");
    }

    public ItemFloppyDisk(String unlocalizedname) {
        super(unlocalizedname);
    }

    @Override
    public File getAssociateFile(IItemStack i) {
        NBTUtils.sanityCheck(i);
        return ComputerUtils.getFileFromItemStack(i);
    }

    @Override
    public String getDiskLabel(IItemStack i) {
        return "" + NBTUtils.getString("Label", i);
    }

    @Override
    public int getSectorSize(IItemStack i) {
        return 0x1000;
    }

    @Override
    public int getSectorCount(IItemStack i) {
        return (int) (Math.ceil((double)DISK_SIZE)/getSectorSize(i));
    }

    @Override
    public boolean isCompatible(IItemStack i, IPeripheralStorageDrive drive) {
        return drive.getSectorSize() == getSectorSize(i);
    }

    @Override
    public String getSerialNumber(IItemStack i) {
        return "" + NBTUtils.getString("SerialNumber", i);
    }

    @Override
    public int getSize(IItemStack i) {
        return DISK_SIZE;
    }

    @Override
    public int getAccessTime(IItemStack i) {
        return 5;
    }

    @Override
    public void setDiskLabel(IItemStack i, String label) {
        NBTUtils.setString("Label", i, label);
    }

    @Override
    public boolean hasInterface(IInterfaceIdentifier<?> identifier, Direction side) {
        return InterModUtils.matches(identifier, IStorageDevice.IDENTIFIER);
    }

    @Override
    public <T> T getInterface(IInterfaceIdentifier<T> identifier, Direction side) {
        return (T) this;
    }
}
