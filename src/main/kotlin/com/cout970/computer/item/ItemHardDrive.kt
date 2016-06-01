package com.cout970.computer.item

import net.minecraft.item.ItemStack

/**
 * Created by cout970 on 01/06/2016.
 */
object ItemHardDrive : ItemStorageDevice("hard_drive") {

    const val DISK_SIZE = 0x40000 //256kB

    override fun getDiskSize(): Int = DISK_SIZE;

    override fun getSectorSize(i: ItemStack?): Int = 0x1000

    override fun getAccessTime(i: ItemStack): Int = 0
}