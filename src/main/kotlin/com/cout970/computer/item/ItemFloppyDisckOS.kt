package com.cout970.computer.item

import com.cout970.computer.util.getInputStream
import net.minecraft.item.ItemStack
import java.io.File
import java.io.FileOutputStream

/**
 * Created by cout970 on 18/08/2016.
 */
object ItemFloppyDisckOS : ItemStorageDevice("floppy_disk_os") {

    const val DISK_SIZE = 0x40000 //256kB

    override fun getAssociateFile(i: ItemStack): File {
        val file = super.getAssociateFile(i)
        getInputStream("os.bin").copyTo(FileOutputStream(file))
        return file
    }

    override fun getDiskSize(): Int = DISK_SIZE

    override fun getSectorSize(i: ItemStack?): Int = 0x1000

    override fun getAccessTime(i: ItemStack): Int = 5

}