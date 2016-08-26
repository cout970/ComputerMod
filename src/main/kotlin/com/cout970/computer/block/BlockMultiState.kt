package com.cout970.computer.block

import net.minecraft.block.material.Material
import net.minecraft.block.properties.IProperty
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

/**
 * Created by cout970 on 21/06/2016.
 */
abstract class BlockMultiState(registryName: String, m: Material) : BlockBase(registryName, m) {


    override fun getStateFromMeta(meta: Int): IBlockState? {
        return getStateMap()[meta]
    }

    override fun getMetaFromState(state: IBlockState?): Int {
        return getStateMap().entries.indexOfFirst { it.value == state }
    }

    override fun createBlockState(): BlockStateContainer? = BlockStateContainer(this, *getProperties())


    @SideOnly(Side.CLIENT)
    override fun getSubBlocks(itemIn: Item?, tab: CreativeTabs?, list: MutableList<ItemStack>?) {
        if (itemIn == null || tab == null || list == null) return
        if(hasSubtypes()) {
            for (i in getStateMap().keys) {
                list.add(ItemStack(itemIn, 1, i))
            }
        }else{
            super.getSubBlocks(itemIn, tab, list)
        }
    }

    fun getStateName(state: IBlockState): String {
        return state.properties.map {
            val pName = it.key.getName()
            @Suppress("UNCHECKED_CAST")
            val vName = (it.key as IProperty<Comparable<Any>>).getName(it.value as Comparable<Any>)
            "$pName=$vName"
        }.joinToString(separator = ",")
    }

    abstract fun getProperties(): Array<IProperty<*>>

    abstract fun getStateMap(): Map<Int, IBlockState>

    abstract fun hasSubtypes():Boolean
}