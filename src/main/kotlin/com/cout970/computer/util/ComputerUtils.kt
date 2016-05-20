package com.cout970.computer.util

import com.cout970.computer.Computer
import com.cout970.computer.api.IPeripheral
import com.cout970.computer.misc.MOD_ID
import com.cout970.computer.util.pathfinding.ComputerPathFinder
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.*

/**
 * Created by cout970 on 20/05/2016.
 */

fun getBitsFromInt(inst: Int, start: Int, end: Int, signed: Boolean): Int {
    var inst = inst
    var start = start
    var end = end
    if (start > end) {
        val temp = end
        end = start
        start = temp
    }
    val max = 0xFFFFFFFF.toInt()
    val mask = max.ushr(31 - end) and (max shl start)
    inst = inst and mask
    if (signed) {
        inst = inst shl 31 - end
        return inst shr start + (31 - end)
    }
    return inst.ushr(start)
}

fun getInputStream(file: String): InputStream {
    return Computer::class.java.getResourceAsStream("/assets/$MOD_ID/cpu/$file")
}

fun getFileFromItemStack(i: ItemStack): File? {
    val nbt = i.tagCompound ?: return null
    if (nbt.hasKey("SerialNumber")) {
        return File(getSaveDirectory(), "disk_" + nbt.getString("SerialNumber") + ".img")
    } else {
        var aux: String
        while (true) {
            aux = genSerialNumber()
            val f = File(getSaveDirectory(), "disk_$aux.img")
            try {
                if (f.createNewFile()) {
                    nbt.setString("SerialNumber", aux)
                    return f
                }
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }

        }
    }
}

fun genSerialNumber(): String {
    var serial = ""
    val r = Random()
    for (i in 0..15) {
        serial += Integer.toHexString(r.nextInt(16))
    }
    return serial
}

fun getSaveDirectory(): File {
    val f = File(Computer.modsFile, MOD_ID)
    f.mkdirs()
    return f
}

fun getBusByAddress(w: World, pos: BlockPos, addr: Int): IPeripheral? {
    val p = ComputerPathFinder(w, addr)
    p.setStart(pos)
    p.findEnd()
    return p.result
}

fun getFileFromName(file: String): File {
    return File("/assets/magneticraft/cpu/" + file)
}