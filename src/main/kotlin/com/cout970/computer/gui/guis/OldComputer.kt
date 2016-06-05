package com.cout970.computer.gui.guis

import com.cout970.computer.gui.ContainerBase
import com.cout970.computer.gui.GuiBase
import com.cout970.computer.gui.IComponent
import com.cout970.computer.gui.IGui
import com.cout970.computer.gui.components.AbstractButton
import com.cout970.computer.gui.components.IButtonListener
import com.cout970.computer.gui.components.MonitorComponent
import com.cout970.computer.gui.components.SimpleButton
import com.cout970.computer.tileentity.TileOldComputer
import com.cout970.computer.util.resource
import com.cout970.computer.util.vector.Vec2d
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.IContainerListener
import net.minecraft.util.ResourceLocation
import net.minecraftforge.items.SlotItemHandler
import java.util.function.Predicate

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

    override fun initComponents() {
        val start = getStart()
        components.add(ComponentComputerLight(Vec2d(72, 13), Predicate { tile.assembed }))
        components.add(ComponentComputerLight(Vec2d(87, 13), Predicate { tile.running }))
        components.add(ComponentComputerLight(Vec2d(102, 13), Predicate { tile.waiting }))
        components.add(SimpleButton(0, start + Vec2d(-5 + 16, 55), this, Vec2d(0, 0)))
        components.add(SimpleButton(1, start + Vec2d(-3 + 16 * 2, 55), this, Vec2d(16, 0)))
        components.add(SimpleButton(2, start + Vec2d(-1 + 16 * 3, 55), this, Vec2d(16 * 2, 0)))

    }

    override fun onPress(button: AbstractButton, mouse: Vec2d, mouseButton: Int): Boolean {
        tile.onPress(button.ID)
        return true
    }

    override fun getBackground(): ResourceLocation? = resource("textures/gui/cpu.png")
}

class ContainerOldComputerBack(val tile: TileOldComputer, val inv: InventoryPlayer) : ContainerBase() {

    init {
        addSlotToContainer(SlotItemHandler(tile.motherboard, 0, 11, 9))
        addSlotToContainer(SlotItemHandler(tile.motherboard, 1, 11 + 18, 9))
        addSlotToContainer(SlotItemHandler(tile.motherboard, 2, 11 + 18 * 2, 9))

        addSlotToContainer(SlotItemHandler(tile.motherboard, 3, 11, 9 + 18))
        addSlotToContainer(SlotItemHandler(tile.motherboard, 4, 11 + 18, 9 + 18))
        addSlotToContainer(SlotItemHandler(tile.motherboard, 5, 11 + 18 * 2, 9 + 18))
        bindPlayerInventory(inv)
    }

    override fun detectAndSendChanges() {
        super.detectAndSendChanges()
        for (j in this.listeners.indices) {
            (this.listeners[j] as IContainerListener).sendProgressBarUpdate(this, 0, if (tile.motherboard.isAssembled) 1 else 0)
            (this.listeners[j] as IContainerListener).sendProgressBarUpdate(this, 1, if (tile.motherboard.cpu?.isRunning ?: false) 1 else 0)
            (this.listeners[j] as IContainerListener).sendProgressBarUpdate(this, 2, if (tile.motherboard.diskDrive?.isWaiting ?: false) 1 else 0)
        }
    }

    override fun updateProgressBar(id: Int, data: Int) {
        when(id) {
            0 -> tile.assembed = data == 1
            1 -> tile.running = data == 1
            2 -> tile.waiting = data == 1
        }
    }
}

class ComponentComputerLight(val pos: Vec2d, val func: Predicate<Void?>) : IComponent {

    override fun drawFirstLayer(gui: IGui, mouse: Vec2d, partialTicks: Float) {
        gui.bindTexture(resource("textures/gui/cpu.png"))
        if (func.test(null)) {
            gui.drawTexturedModalRect(gui.getStart() + pos, Vec2d(9, 9), Vec2d(0, 177))
        }
    }

}
