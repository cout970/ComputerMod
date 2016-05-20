
package com.cout970.computer.block

import com.cout970.computer.misc.CreativeTab
import com.cout970.computer.misc.MOD_ID
import com.cout970.computer.util.resource
import net.minecraft.block.Block
import net.minecraft.block.material.Material

/**
 * Created by cout970 on 19/05/2016.
 */
open class BlockBase(registryName : String, m : Material) : Block(m) {

    init{
        this.unlocalizedName = "$MOD_ID.$registryName"
        this.registryName = resource(registryName)
        setHardness(1.5F)
        setResistance(10.0F)
        setCreativeTab(CreativeTab)
    }
}