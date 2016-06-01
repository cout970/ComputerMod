package com.cout970.computer.item

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

/**
 * Created by cout970 on 30/05/2016.
 */
object ItemFloppyDisk : ItemStorageDevice("floppy_disk_tier_1") {

    const val DISK_SIZE = 0x40000 //256kB

    override fun getDiskSize(): Int = DISK_SIZE;

    override fun getSectorSize(i: ItemStack?): Int = 0x1000

    override fun getAccessTime(i: ItemStack): Int = 5

    override fun getSubItems(itemIn: Item?, tab: CreativeTabs?, subItems: MutableList<ItemStack>?) {
        super.getSubItems(itemIn, tab, subItems)
        for(i in 1..6){
            subItems?.add(ItemStack(this, 1, i))
        }
    }
}