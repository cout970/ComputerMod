package com.cout970.computer.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by cout970 on 09/01/2016.
 */
public interface IMessageStorage {

    void saveToMessage(ByteBuf buff, Side processSide);

    void loadFromMessage(ByteBuf buff, Side processSide);
}
