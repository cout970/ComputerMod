package com.cout970.computer
import com.cout970.computer.item.ItemFloppyDisk
import net.minecraft.item.Item
import net.minecraftforge.fml.common.registry.GameRegistry

val items = listOf<Item>(ItemFloppyDisk)

fun registerItems() {
    items.forEach {
        GameRegistry.register(it)
    }
}