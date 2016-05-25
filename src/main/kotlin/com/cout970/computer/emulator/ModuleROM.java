package com.cout970.computer.emulator;

import com.cout970.computer.api.IModuleRAM;
import com.cout970.computer.api.IModuleROM;
import com.cout970.computer.util.ComputerUtilsKt;
import net.minecraft.nbt.NBTTagCompound;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by cout970 on 07/02/2016.
 */
public class ModuleROM implements IModuleROM {
    @Override
    public void loadBios(IModuleRAM ram) {
        InputStream archive;
        try {
            archive = ComputerUtilsKt.getInputStream("bios.bin");
            byte[] buffer = new byte[0x1000];
            int readed = archive.read(buffer, 0, buffer.length);
            for (int i = 0; i < readed; i++) {
                ram.writeByte(0x0400 + i, buffer[i]);
            }
            archive.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "rom";
    }

    @Override
    public void load(NBTTagCompound data) {

    }

    @Override
    public void save(NBTTagCompound data) {

    }
}
