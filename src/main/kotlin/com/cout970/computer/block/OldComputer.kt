package com.cout970.computer.block

import com.cout970.computer.Computer
import com.cout970.computer.tileentity.TileOldComputer
import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

/**
 * Created by cout970 on 19/05/2016.
 */

object OldComputer : BlockBase("old_computer", Material.CIRCUITS), ITileEntityProvider {

    override fun createNewTileEntity(worldIn: World?, meta: Int): TileEntity? = TileOldComputer()

    override fun onBlockActivated(worldIn: World?, pos: BlockPos?, state: IBlockState?, playerIn: EntityPlayer?, hand: EnumHand?, heldItem: ItemStack?, side: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (pos != null && worldIn != null && !worldIn.isRemote) {
            playerIn?.openGui(Computer, if (playerIn.isSneaking) 1 else 0, worldIn, pos.x, pos.y, pos.z)
        }
        return true;
    }
}