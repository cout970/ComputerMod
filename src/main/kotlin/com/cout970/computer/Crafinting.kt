package com.cout970.computer

import com.cout970.computer.block.OldComputer
import com.cout970.computer.item.*
import net.minecraft.init.Blocks.*
import net.minecraft.init.Items.*
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.registry.GameRegistry

/**
 * Created by cout970 on 02/06/2016.
 */

fun registerCraftingRecipes() {

    //@formatter:off
    GameRegistry.addRecipe(ItemStack(ItemTransistor),   "WRW", "PCP", "RWR", 'W', ItemStack(WOOL, 1, 15), 'R', REDSTONE, 'P', REPEATER, 'C', COMPARATOR)
    GameRegistry.addRecipe(ItemStack(OldComputer),      "III", "GLI", "ICI", 'I', IRON_INGOT, 'G', ItemStack(STAINED_GLASS, 1, 5), 'L', REDSTONE_LAMP, 'C', ItemMotherboard)
    GameRegistry.addRecipe(ItemStack(ItemMotherboard),  "###", "GCG", "III", 'I', HEAVY_WEIGHTED_PRESSURE_PLATE, 'G', LIGHT_WEIGHTED_PRESSURE_PLATE, 'C', ItemStack(CARPET, 1, 13))
    GameRegistry.addRecipe(ItemStack(ItemFloppyDisk),   "PRP", "PCP", "PPP", 'P', PAPER, 'R', REDSTONE, 'C', COMPASS)
    GameRegistry.addRecipe(ItemStack(ItemHardDrive),    "IRI", "IDI", "III", 'I', IRON_INGOT, 'D', DIAMOND, 'R', REDSTONE)
    GameRegistry.addRecipe(ItemStack(ItemCPUMips),      "WWW", "DCD", "QGQ", 'W', ItemStack(WOOL, 1, 15), 'D', DIAMOND, 'C', CLOCK, 'G', LIGHT_WEIGHTED_PRESSURE_PLATE, 'Q', QUARTZ)
    GameRegistry.addRecipe(ItemStack(ItemROM),          "GWG", "GTG", "GWG", 'G', LIGHT_WEIGHTED_PRESSURE_PLATE, 'T', ItemTransistor, 'W', ItemStack(WOOL, 1, 15))
    GameRegistry.addRecipe(ItemStack(ItemRAM64K),       "GTG", "GDG", "GTG", 'G', LIGHT_WEIGHTED_PRESSURE_PLATE, 'T', ItemTransistor, 'D', DIAMOND)
    //@formatter:on
}