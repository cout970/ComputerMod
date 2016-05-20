package com.cout970.computer.gui

import com.cout970.computer.gui.guis.ContainerOldComputer
import com.cout970.computer.gui.guis.ContainerOldComputerBack
import com.cout970.computer.gui.guis.GuiOldComputer
import com.cout970.computer.gui.guis.GuiOldComputerBack
import com.cout970.computer.tileentity.TileOldComputer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.IGuiHandler

/**
 * Created by cout970 on 20/05/2016.
 */
object GuiHandler : IGuiHandler {

    override fun getClientGuiElement(ID: Int, player: EntityPlayer?, world: World?, x: Int, y: Int, z: Int): Any? {
        val tile = world?.getTileEntity(BlockPos(x,y,z))
        if(tile is TileOldComputer){
            if(ID == 0) {
                return GuiOldComputer(tile, player!!)
            }else{
                return GuiOldComputerBack(tile, player!!.inventory)
            }
        }
        return null
    }


    override fun getServerGuiElement(ID: Int, player: EntityPlayer?, world: World?, x: Int, y: Int, z: Int): Any? {
        val tile = world?.getTileEntity(BlockPos(x,y,z))
        if(tile is TileOldComputer){
            if(ID == 0) {
                return ContainerOldComputer(tile, player!!)
            }else{
                return ContainerOldComputerBack(tile, player!!.inventory)
            }
        }
        return null
    }

}