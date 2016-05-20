package com.cout970.computer.gui

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.Container
import net.minecraft.inventory.Slot

/**
 * Created by cout970 on 20/05/2016.
 */
open class ContainerBase : Container() {

    override fun canInteractWith(playerIn: EntityPlayer?): Boolean = true

    fun bindPlayerInventory(playerInventory: InventoryPlayer) {

        for (i in 0..2) {
            for (j in 0..8) {
                this.addSlotToContainer(Slot(playerInventory, j + i * 9 + 9,
                        8 + j * 18,
                        84 + i * 18))
            }
        }

        for (i in 0..8) {
            this.addSlotToContainer(Slot(playerInventory, i,
                    8 + i * 18,
                    142))
        }
    }
}