package com.cout970.computer2.network;

import com.cout970.computer.network.IMessageStorage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.darkaqua.blacksmith.api.common.network.ExtendedByteBuf;
import net.darkaqua.blacksmith.api.common.network.INetworkContext;
import net.darkaqua.blacksmith.api.common.network.INetworkMessage;
import net.darkaqua.blacksmith.api.common.network.INetworkMessageHandler;
import net.darkaqua.blacksmith.api.common.tileentity.ITileEntity;
import net.darkaqua.blacksmith.api.common.util.GameSide;
import net.darkaqua.blacksmith.api.common.util.ObjectScanner;
import net.darkaqua.blacksmith.api.common.util.vectors.Vect3i;
import net.darkaqua.blacksmith.api.common.world.IWorld;

import java.util.Arrays;

/**
 * Created by cout970 on 09/01/2016.
 */
public class MessageGenericUpdate implements INetworkMessage, INetworkMessageHandler<MessageGenericUpdate, INetworkMessage> {

    private IMessageStorage storage;
    private byte[] data;
    private int dim;
    private Vect3i pos;
    private GameSide side;

    public MessageGenericUpdate() {
    }

    public MessageGenericUpdate(IMessageStorage storage, GameSide processSide, int dim, Vect3i pos) {
        this.storage = storage;
        this.dim = dim;
        this.pos = pos.copy();
        this.side = processSide;
    }

    @Override
    public void fromBytes(ByteBuf buf, ExtendedByteBuf helper) {
        side = buf.readBoolean() ? GameSide.SERVER : GameSide.CLIENT;
        dim = buf.readInt();
        pos = helper.readVect3i();
        data = new byte[buf.readInt()];
        buf.readBytes(data);
    }

    @Override
    public void toBytes(ByteBuf buf, ExtendedByteBuf helper) {
        buf.writeBoolean(side == GameSide.SERVER);
        buf.writeInt(dim);
        helper.writeVect3i(pos);

        ByteBuf aux = Unpooled.buffer();
        storage.saveToMessage(aux, side);
        byte[] arr = aux.array();

        buf.writeInt(arr.length);
        buf.writeBytes(arr);
    }

    @Override
    public INetworkMessage onMessage(MessageGenericUpdate message, INetworkContext context) {
        if (message.side == GameSide.SERVER) {
            context.getServerContext().addScheduledTask(new Runnable() {

                @Override
                public void run() {
                    IWorld world = context.getServerContext().getServer().getWorld(message.dim);
                    ITileEntity tile = world.getTileEntity(message.pos);
                    IMessageStorage storage = ObjectScanner.findInTileEntity(tile, IMessageStorage.class);
                    if (storage != null) {
                        ByteBuf aux = Unpooled.buffer();
                        aux.writeBytes(message.data);
                        storage.loadFromMessage(aux, message.side);
                    }
                }
            });
        } else {
            context.getClientContext().addScheduledTask(new Runnable() {

                @Override
                public void run() {
                    IWorld world = context.getClientContext().getWorld();
                    ITileEntity tile = world.getTileEntity(message.pos);
                    IMessageStorage storage = ObjectScanner.findInTileEntity(tile, IMessageStorage.class);
                    if (storage != null) {
                        ByteBuf aux = Unpooled.buffer();
                        aux.writeBytes(message.data);
                        storage.loadFromMessage(aux, message.side);
                    }
                }
            });
        }
        return null;
    }

    @Override
    public String toString() {
        return "MessageGenericUpdate{" +
                "storage=" + storage +
                ", data=" + Arrays.toString(data) +
                ", dim=" + dim +
                ", pos=" + pos +
                ", sides=" + side +
                '}';
    }
}
