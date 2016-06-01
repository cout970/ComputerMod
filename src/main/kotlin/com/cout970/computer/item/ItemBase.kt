package com.cout970.computer.item

import com.cout970.computer.misc.CreativeTab
import com.cout970.computer.misc.MOD_ID
import com.cout970.computer.util.resource
import net.minecraft.item.Item

/**
 * Created by cout970 on 19/05/2016.
 */
abstract class ItemBase(registryName: String) : Item() {

    init {
        this.registryName = resource(registryName)
        this.unlocalizedName = "$MOD_ID.$registryName"
        creativeTab = CreativeTab
    }
}