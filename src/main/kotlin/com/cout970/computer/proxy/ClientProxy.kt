package com.cout970.computer.proxy

import com.cout970.computer.blocks
import com.cout970.computer.item.ItemFloppyDisk
import com.cout970.computer.items
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.Item
import net.minecraftforge.client.model.ModelLoader

/**
 * Created by cout970 on 19/05/2016.
 */
class ClientProxy : CommonProxy() {

    override fun preInit() {
        super.preInit()
        (items + blocks.map(Item::getItemFromBlock)).forEach {
            ModelLoader.setCustomModelResourceLocation(it, 0, ModelResourceLocation(it!!.registryName, "inventory"))
        }
        for(i in 1..6){
            ModelLoader.setCustomModelResourceLocation(ItemFloppyDisk, i, ModelResourceLocation(ItemFloppyDisk.registryName.toString() + "_" + i, "inventory"))
        }
    }

    override fun init() {
        super.init()
    }

    override fun postInit() {
        super.postInit()
    }
}