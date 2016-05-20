package com.cout970.computer2.network;

import com.cout970.computer2.ComputerMod;
import net.darkaqua.blacksmith.api.common.network.INetworkChannel;
import net.darkaqua.blacksmith.api.common.network.NetworkChannelFactory;
import net.darkaqua.blacksmith.api.common.util.GameSide;

/**
 * Created by cout970 on 09/01/2016.
 */
public class ChannelManager {

    public static final INetworkChannel channel = NetworkChannelFactory.createNetworkChannel(ComputerMod.MOD_ID);

    public static void init(){
        channel.registerMessage(new MessageGenericUpdate(), MessageGenericUpdate.class, 0, GameSide.SERVER);
        channel.registerMessage(new MessageGenericUpdate(), MessageGenericUpdate.class, 1, GameSide.CLIENT);
        channel.registerMessage(new MessageButtonClick(), MessageButtonClick.class, 2, GameSide.SERVER);
    }

}
