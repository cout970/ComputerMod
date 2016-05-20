package com.cout970.computer

import com.cout970.computer.block.OldComputer
import com.cout970.computer.block.itemblock.ItemBlockBase
import net.minecraftforge.fml.common.registry.GameRegistry

val blocks = listOf(OldComputer)

fun registerBlocks() {
    blocks.forEach {
        GameRegistry.register(it)
        GameRegistry.register(ItemBlockBase(it))
    }
}
