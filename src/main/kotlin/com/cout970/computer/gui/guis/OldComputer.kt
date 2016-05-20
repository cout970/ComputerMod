package com.cout970.computer.gui.guis

import com.cout970.computer.gui.ContainerBase
import com.cout970.computer.gui.GuiBase
import com.cout970.computer.gui.components.AbstractButton
import com.cout970.computer.gui.components.IButtonListener
import com.cout970.computer.gui.components.MonitorComponent
import com.cout970.computer.gui.components.SimpleButton
import com.cout970.computer.tileentity.TileOldComputer
import com.cout970.computer.util.resource
import com.cout970.computer.util.vector.Vec2d
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.util.ResourceLocation

/**
 * Created by cout970 on 20/05/2016.
 */

// FRONT
class GuiOldComputer(val tile: TileOldComputer, player: EntityPlayer) : GuiBase(ContainerOldComputer(tile, player)) {

    init {
        xSize = 350
        ySize = 230
    }

    override fun initComponents() {
        components.add(MonitorComponent(tile.monitor))
    }

    override fun getBackground(): ResourceLocation? = resource("textures/gui/old_monitor.png")

    override fun getBackTexSize() = Vec2d(512, 512)

    override fun getBackgroundEnd() = Vec2d(350, 230)
}

class ContainerOldComputer(val tile: TileOldComputer, val player: EntityPlayer) : ContainerBase() {

    override fun detectAndSendChanges() {
        tile.sendMonitorDataToClient(player)
    }
}

//BACK
class GuiOldComputerBack(val tile: TileOldComputer, inv: InventoryPlayer) : GuiBase(ContainerOldComputerBack(tile, inv)), IButtonListener {

    override fun initComponents(){
        val start = getStart()
        components.add(SimpleButton(0, start + Vec2d(-5 + 16, 55), this, Vec2d(0,0)))
        components.add(SimpleButton(1, start + Vec2d(-3 + 16*2, 55), this, Vec2d(16,0)))
        components.add(SimpleButton(2, start + Vec2d(-1 + 16*3, 55), this, Vec2d(16*2,0)))
    }

    override fun onPress(button: AbstractButton, mouse: Vec2d, mouseButton: Int): Boolean {
        tile.onPress(button.ID)
        return true
    }

    override fun getBackground(): ResourceLocation? = resource("textures/gui/cpu.png")
}

class ContainerOldComputerBack(val tile: TileOldComputer, val inv: InventoryPlayer) : ContainerBase() {

    init {
        bindPlayerInventory(inv)
    }
}

