package com.cout970.computer.network

import com.cout970.computer.util.readBlockPos
import com.cout970.computer.util.writeBlockPos
import io.netty.buffer.ByteBuf
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

/**
 * Created by cout970 on 20/05/2016.
 */
class MessageButtonClick() : IMessage, IMessageHandler<MessageButtonClick, IMessage>{

    var buttonId: Int = 0
    var dimension: Int = 0
    var pos: BlockPos? = null

    constructor(buttonId : Int, dimension : Int, pos: BlockPos): this(){
        this.buttonId = buttonId
        this.dimension = dimension
        this.pos = pos
    }

    override fun fromBytes(buf: ByteBuf?) {
        if(buf == null)return
        buttonId = buf.readInt();
        dimension = buf.readInt()
        pos = buf.readBlockPos()
    }

    override fun toBytes(buf: ByteBuf?) {
        if(buf == null)return
        buf.writeInt(buttonId)
        buf.writeInt(dimension)
        buf.writeBlockPos(pos!!)
    }

    override fun onMessage(message: MessageButtonClick?, ctx: MessageContext?): IMessage? {
        val context = ctx!!.serverHandler
        context.playerEntity.mcServer.addScheduledTask({
            val world = context.playerEntity.mcServer.worldServerForDimension(message!!.dimension)
            val tile = world.getTileEntity(message.pos)
            val listener = tile as? IServerButtonListener
            listener?.onPress(message.buttonId)
        })
        return null
    }
}