package com.cout970.computer
import com.cout970.computer.item.*
import net.minecraft.item.Item
import net.minecraftforge.fml.common.registry.GameRegistry

val items = listOf<Item>(ItemFloppyDisk, ItemCPUMips, ItemRAM64K, ItemROMDefault, ItemHardDrive)

fun registerItems() {
    items.forEach {
        GameRegistry.register(it)
    }
}