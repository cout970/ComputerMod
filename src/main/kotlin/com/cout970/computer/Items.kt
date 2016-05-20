package com.cout970.computer
import net.minecraft.item.Item
import net.minecraftforge.fml.common.registry.GameRegistry

val items = listOf<Item>()

fun registerItems() {
    items.forEach {
        GameRegistry.register(it)
    }
}