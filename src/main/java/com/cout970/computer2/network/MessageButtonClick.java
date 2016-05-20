package com.cout970.computer2.network;

import com.cout970.computer.network.IButtonListener;
import io.netty.buffer.ByteBuf;
import net.darkaqua.blacksmith.api.common.network.ExtendedByteBuf;
import net.darkaqua.blacksmith.api.common.network.INetworkContext;
import net.darkaqua.blacksmith.api.common.network.INetworkMessage;
import net.darkaqua.blacksmith.api.common.network.INetworkMessageHandler;
import net.darkaqua.blacksmith.api.common.tileentity.ITileEntity;
import net.darkaqua.blacksmith.api.common.util.ObjectScanner;
import net.darkaqua.blacksmith.api.common.util.vectors.Vect3i;
import net.darkaqua.blacksmith.api.common.world.IWorld;

/**
 * Created by cout970 on 07/02/2016.
 */
public class MessageButtonClick implements INetworkMessage, INetworkMessageHandler<MessageButtonClick, INetworkMessage> {

    private int buttonId;
    private int dimension;
    private Vect3i pos;

    public MessageButtonClick() {}

    public MessageButtonClick(int buttonId, int dimension, Vect3i pos) {
        this.buttonId = buttonId;
        this.dimension = dimension;
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf, ExtendedByteBuf helper) {
        buttonId = buf.readInt();
        dimension = buf.readInt();
        pos = helper.readVect3i();
    }

    @Override
    public void toBytes(ByteBuf buf, ExtendedByteBuf helper) {
        buf.writeInt(buttonId);
        buf.writeInt(dimension);
        helper.writeVect3i(pos);
    }

    @Override
    public INetworkMessage onMessage(MessageButtonClick message, INetworkContext context) {
        context.getServerContext().addScheduledTask(new Runnable() {
            @Override
            public void run() {
                IWorld world = context.getServerContext().getServer().getWorld(message.dimension);
                ITileEntity tile = world.getTileEntity(message.pos);
                IButtonListener listener = ObjectScanner.findInTileEntity(tile, IButtonListener.class);
                listener.onPress(message.buttonId);
            }
        });
        return null;
    }
}
