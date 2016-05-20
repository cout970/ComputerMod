package com.cout970.computer.block.itemblock

import com.cout970.computer.block.BlockBase
import net.minecraft.item.ItemBlock

class ItemBlockBase(block: BlockBase) : ItemBlock(block) {

    init {
        registryName = block.registryName
        unlocalizedName = block.unlocalizedName
        hasSubtypes = true
    }
}
