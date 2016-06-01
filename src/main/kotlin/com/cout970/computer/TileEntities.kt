package com.cout970.computer

import com.cout970.computer.misc.MOD_ID
import com.cout970.computer.tileentity.TileOldComputer
import net.minecraftforge.fml.common.registry.GameRegistry

/**
 * Created by cout970 on 28/05/2016.
 */
val tiles = listOf(TileOldComputer::class.java)

fun registerTileEntities() {
    tiles.forEach {
        GameRegistry.registerTileEntity(it, MOD_ID + "_" + it.simpleName)
    }
}