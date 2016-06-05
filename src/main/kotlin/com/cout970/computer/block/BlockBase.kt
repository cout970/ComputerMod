package com.cout970.computer.block

import com.cout970.computer.misc.CreativeTab
import com.cout970.computer.misc.MOD_ID
import com.cout970.computer.util.resource
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.items.CapabilityItemHandler

/**
 * Created by cout970 on 19/05/2016.
 */
open class BlockBase(registryName: String, m: Material) : Block(m) {

    init {
        this.unlocalizedName = "$MOD_ID.$registryName"
        this.registryName = resource(registryName)
        setHardness(1.5F)
        setResistance(10.0F)
        setCreativeTab(CreativeTab)
    }

    override fun breakBlock(worldIn: World?, pos: BlockPos?, state: IBlockState?) {
        val tile = worldIn?.getTileEntity(pos)
        if (tile != null) {
            if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
                val handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)
                for (i in 0..handler.slots - 1) {
                    val slot = handler.getStackInSlot(i)
                    if (slot != null) {
                        spawnAsEntity(worldIn, pos, slot)
                    }
                }
            }
        }
        super.breakBlock(worldIn, pos, state)
    }
}