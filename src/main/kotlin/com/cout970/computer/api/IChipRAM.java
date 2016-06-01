package com.cout970.computer.api;


import net.minecraft.item.ItemStack;

/**
 * Created by cout970 on 09/03/2016.
 */
public interface IChipRAM extends IChip {

    IModuleRAM createModule(ItemStack item, IComputer computer);


}
