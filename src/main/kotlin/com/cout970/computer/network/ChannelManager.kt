package com.cout970.computer.network

import com.cout970.computer.misc.MOD_ID
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
import net.minecraftforge.fml.relauncher.Side

/**
 * Created by cout970 on 20/05/2016.
 */
object ChannelManager {

    val channel = SimpleNetworkWrapper(MOD_ID)

    init {
        channel.registerMessage(MessageGenericUpdate(), MessageGenericUpdate::class.java, 0, Side.SERVER)
        channel.registerMessage(MessageGenericUpdate(), MessageGenericUpdate::class.java, 1, Side.CLIENT)
        channel.registerMessage(MessageButtonClick(), MessageButtonClick::class.java, 2, Side.SERVER)
    }
}