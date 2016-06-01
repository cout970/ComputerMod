package com.cout970.computer.misc

import com.cout970.computer.item.ItemFloppyDisk
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item

/**
 * Created by cout970 on 19/05/2016.
 */
object CreativeTab : CreativeTabs(MOD_ID) {

    override fun getTabIconItem(): Item? = ItemFloppyDisk
}