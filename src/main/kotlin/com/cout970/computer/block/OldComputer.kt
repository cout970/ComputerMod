package com.cout970.computer.block

import com.cout970.computer.Computer
import com.cout970.computer.tileentity.TileOldComputer
import com.google.common.base.Predicate
import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.material.Material
import net.minecraft.block.properties.IProperty
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
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


object OldComputer : BlockMultiState("old_computer", Material.IRON), ITileEntityProvider {

    lateinit var propertyDirection: IProperty<EnumFacing>

    override fun createNewTileEntity(worldIn: World?, meta: Int): TileEntity? = TileOldComputer()

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand?, heldItem: ItemStack?, side: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (!worldIn.isRemote) {
            val facing = state.getValue(propertyDirection).opposite
            playerIn.openGui(Computer, if (facing == side) 1 else 0, worldIn, pos.x, pos.y, pos.z)
        }
        return true
    }

    override fun onBlockPlacedBy(worldIn: World?, pos: BlockPos?, state: IBlockState?, placer: EntityLivingBase?, stack: ItemStack?) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack)
        val newState = state?.withProperty(propertyDirection, placer?.adjustedHorizontalFacing?.opposite)
        if (newState != null) {
            worldIn?.setBlockState(pos, newState)
        }
    }

    override fun getProperties(): Array<IProperty<*>> {
        propertyDirection = PropertyEnum.create("facing", EnumFacing::class.java, Predicate { it?.frontOffsetY == 0 })
        return arrayOf(propertyDirection)
    }

    override fun getStateMap(): Map<Int, IBlockState> {
        return mapOf(
                0 to defaultState.withProperty(propertyDirection, EnumFacing.NORTH),
                1 to defaultState.withProperty(propertyDirection, EnumFacing.SOUTH),
                2 to defaultState.withProperty(propertyDirection, EnumFacing.WEST),
                3 to defaultState.withProperty(propertyDirection, EnumFacing.EAST)
        )
    }

    override fun hasSubtypes(): Boolean = false
}