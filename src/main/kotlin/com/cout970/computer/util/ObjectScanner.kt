package com.cout970.computer.util

import net.minecraft.item.Item
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ICapabilityProvider

/**
 * Created by cout970 on 20/05/2016.
 */


fun <T> findInTileEntity(tile: TileEntity?, cap: Capability<T>?, dir: EnumFacing?): T? {
    if(tile?.hasCapability(cap, dir) ?: false){
        return tile?.getCapability(cap, dir)
    }
    return null
}

fun <T> findInItem(tile: Item?, cap: Capability<T>?, dir: EnumFacing?): T?{
    if(tile is ICapabilityProvider && tile.hasCapability(cap, dir)){
        return tile.getCapability(cap, dir)
    }
    return null
}