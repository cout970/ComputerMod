package com.cout970.computer2.tileentity;

import com.cout970.computer.api.IPeripheral;
import com.cout970.computer.api.IPeripheralProvider;
import com.cout970.computer.emulator.PeripheralMonitor;
import com.cout970.computer2.network.ChannelManager;
import com.cout970.computer.network.IMessageStorage;
import com.cout970.computer2.network.MessageGenericUpdate;
import io.netty.buffer.ByteBuf;
import net.darkaqua.blacksmith.api.Game;
import net.darkaqua.blacksmith.api.common.entity.IPlayer;
import net.darkaqua.blacksmith.api.common.intermod.IInterfaceIdentifier;
import net.darkaqua.blacksmith.api.common.intermod.IInterfaceProvider;
import net.darkaqua.blacksmith.api.common.intermod.InterModUtils;
import net.darkaqua.blacksmith.api.common.storage.IDataCompound;
import net.darkaqua.blacksmith.api.common.tileentity.ITileEntity;
import net.darkaqua.blacksmith.api.common.util.Direction;
import net.darkaqua.blacksmith.api.common.util.GameSide;

/**
 * Created by cout970 on 31/12/2015.
 */
public class TileOldMonitor extends TileBase implements IPeripheralProvider, IInterfaceProvider, IMessageStorage {

    public PeripheralMonitor monitor;

    @Override
    public void bindParent(ITileEntity tile) {
        super.bindParent(tile);
        monitor = new PeripheralMonitor(tile);
    }

    @Override
    public void loadData(IDataCompound tag) {
        super.loadData(tag);
        monitor.load(tag);
    }

    @Override
    public void saveData(IDataCompound tag) {
        super.saveData(tag);
        monitor.save(tag);
    }

    @Override
    public IPeripheral[] getPeripherals() {
        return new IPeripheral[]{monitor};
    }

    @Override
    public boolean hasInterface(IInterfaceIdentifier<?> id, Direction side) {
        return InterModUtils.matches(IPeripheralProvider.IDENTIFIER, id);
    }

    @Override
    public <T> T getInterface(IInterfaceIdentifier<T> identifier, Direction side) {
        return (T) this;
    }

    public void sendMonitorDataToServer() {
        if (Game.isClient()) {
            MessageGenericUpdate msg = new MessageGenericUpdate(this, GameSide.SERVER, getWorld().getWorldDimension(), getPosition());
            ChannelManager.channel.sendToServer(msg);
        }
    }

    public void sendMonitorDataToClient(IPlayer player) {
        if (Game.isServer()) {
            MessageGenericUpdate msg = new MessageGenericUpdate(this, GameSide.CLIENT, getWorld().getWorldDimension(), getPosition());
            ChannelManager.channel.sendTo(msg, player);
        }
    }

    @Override
    public void saveToMessage(ByteBuf buff, GameSide processSide) {
        if (processSide == GameSide.SERVER) {// saving client info
            monitor.saveToMessage(buff, processSide);
        } else {// saving server info
            buff.writeInt(monitor.getCursorPos());
            buff.writeBytes(monitor.getBuffer());
        }
    }

    @Override
    public void loadFromMessage(ByteBuf buff, GameSide processSide) {
        if (processSide == GameSide.SERVER) { // reading client info
            monitor.loadFromMessage(buff, processSide);
        } else {// reading server info
            monitor.setCursorPos(buff.readInt());
            buff.readBytes(monitor.getBuffer());
        }
    }
}
