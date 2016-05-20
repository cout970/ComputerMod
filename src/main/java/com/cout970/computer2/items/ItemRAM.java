package com.cout970.computer2.items;

import com.cout970.computer.api.IChipRAM;
import com.cout970.computer.api.IComputer;
import com.cout970.computer.api.IModuleRAM;
import net.darkaqua.blacksmith.api.common.inventory.IItemStack;

/**
 * Created by cout970 on 09/03/2016.
 */
public class ItemRAM implements IChipRAM {

    @Override
    public IModuleRAM createModule(IItemStack item, IComputer computer) {
        return null;
    }
}
