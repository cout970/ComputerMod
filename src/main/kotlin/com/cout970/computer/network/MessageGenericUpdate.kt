package com.cout970.computer.network

import com.cout970.computer.util.readBlockPos
import com.cout970.computer.util.writeBlockPos
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import net.minecraft.client.Minecraft
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side

/**
 * Created by cout970 on 20/05/2016.
 */
class MessageGenericUpdate(): IMessage, IMessageHandler<MessageGenericUpdate, IMessage> {

    var storage: IMessageStorage? = null
    var data: ByteArray? = null
    var dim: Int = 0
    var pos: BlockPos? = null
    var side: Side? = null

    constructor(storage: IMessageStorage, processSide: Side, dim: Int, pos: BlockPos) : this(){
        this.storage = storage
        this.dim = dim
        this.pos = pos
        this.side = processSide
    }

    override fun fromBytes(buf: ByteBuf?) {
        if(buf == null)return
        side = if (buf.readBoolean()) Side.SERVER else Side.CLIENT
        dim = buf.readInt()
        pos = buf.readBlockPos()
        data = ByteArray(buf.readInt())
        buf.readBytes(data)
    }

    override fun toBytes(buf: ByteBuf?) {
        if(buf == null)return
        buf.writeBoolean(side === Side.SERVER)
        buf.writeInt(dim)
        buf.writeBlockPos(pos!!)

        val aux = Unpooled.buffer()
        storage?.saveToMessage(aux, side)
        val arr = aux.array()

        buf.writeInt(arr.size)
        buf.writeBytes(arr)
    }

    override fun onMessage(message: MessageGenericUpdate?, ctx: MessageContext?): IMessage? {
        if(message == null)return null
        if (message.side === Side.SERVER) {
            ctx!!.serverHandler.playerEntity.mcServer.addScheduledTask({
                val world = ctx.serverHandler.playerEntity.mcServer.worldServerForDimension(message.dim)
                val tile = world.getTileEntity(message.pos)
                val storage = tile as? IMessageStorage
                if (storage != null) {
                    val aux = Unpooled.buffer()
                    aux.writeBytes(message.data)
                    storage.loadFromMessage(aux, message.side)
                }
            })
        } else {
            Minecraft.getMinecraft().addScheduledTask({
                val world = Minecraft.getMinecraft().theWorld
                val tile = world.getTileEntity(message.pos)
                val storage = tile as? IMessageStorage
                if (storage != null) {
                    val aux = Unpooled.buffer()
                    aux.writeBytes(message.data)
                    storage.loadFromMessage(aux, message.side)
                }
            })
        }
        return null
    }
}