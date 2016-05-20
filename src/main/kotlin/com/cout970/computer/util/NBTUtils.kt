package com.cout970.computer.util

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound

/**
 * Created by cout970 on 20/05/2016.
 */


fun sanityCheck(i: ItemStack) {
    if (i.tagCompound == null) {
        i.tagCompound = NBTTagCompound()
    }
}

fun ItemStack.getDouble(string: String): Double {
    sanityCheck(this)
    return this.tagCompound?.getDouble(string)!!
}

fun ItemStack.setDouble(string: String, n: Double) {
    sanityCheck(this)
    this.tagCompound?.setDouble(string, n)!!
}

fun ItemStack.getInteger(string: String): Int {
    sanityCheck(this)
    return this.tagCompound?.getInteger(string)!!
}

fun ItemStack.setInteger(string: String, n: Int) {
    sanityCheck(this)
    this.tagCompound?.setInteger(string, n)!!
}

fun ItemStack.getString(string: String): String {
    sanityCheck(this)
    return this.tagCompound?.getString(string)!!
}

fun ItemStack.setString(string: String, label: String) {
    sanityCheck(this)
    this.tagCompound?.setString(string, label)!!
}

fun ItemStack.getBoolean(string: String): Boolean {
    sanityCheck(this)
    return this.tagCompound?.getBoolean(string)!!
}

fun ItemStack.setBoolean(string: String, label: Boolean) {
    sanityCheck(this)
    this.tagCompound?.setBoolean(string, label)!!
}