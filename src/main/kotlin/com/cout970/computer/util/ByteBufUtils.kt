package com.cout970.computer.util

import io.netty.buffer.ByteBuf
import net.minecraft.network.PacketBuffer
import net.minecraft.util.math.BlockPos

/**
 * Created by cout970 on 20/05/2016.
 */


fun ByteBuf.readBlockPos(): BlockPos = PacketBuffer(this).readBlockPos()

fun ByteBuf.writeBlockPos(pos: BlockPos) = PacketBuffer(this).writeBlockPos(pos)