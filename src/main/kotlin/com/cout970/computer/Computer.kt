package com.cout970.computer

import com.cout970.computer.gui.GuiHandler
import com.cout970.computer.misc.LANG_ADAPTER
import com.cout970.computer.misc.MOD_ID
import com.cout970.computer.misc.MOD_NAME
import com.cout970.computer.misc.MOD_VERSION
import com.cout970.computer.proxy.CommonProxy
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.network.NetworkRegistry
import org.apache.logging.log4j.Logger
import java.io.File

/**
 * Created by cout970 on 19/05/2016.
 */

@Mod(
        modid = MOD_ID,
        name = MOD_NAME,
        version = MOD_VERSION,
        modLanguage = "kotlin",
        modLanguageAdapter = LANG_ADAPTER
)
object Computer {

    lateinit var log: Logger
    lateinit var configFile : File
    lateinit var modsFile: String

    @SidedProxy(
            clientSide = "com.cout970.computer.proxy.ClientProxy",
            serverSide = "com.cout970.computer.proxy.CommonProxy"
    )
    lateinit var proxy: CommonProxy;

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        log = event.modLog
        modsFile = event.sourceFile.parent
        configFile = event.suggestedConfigurationFile
        log.info("Starting pre-init")
//        ConfigHandler.load()
//        ConfigHandler.read()
//        ConfigHandler.save()

        registerBlocks()
        registerItems()
        registerTileEntities()
        registerCraftingRecipes()
        registerCapabilities()
        proxy.preInit()

        log.info("Pre-init done")
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        log.info("Starting init")

        proxy.init()
        NetworkRegistry.INSTANCE.registerGuiHandler(this, GuiHandler)

        log.info("Init done")
    }

    @Mod.EventHandler
    fun postInit(event: FMLPostInitializationEvent) {
        log.info("Starting post-init")

        proxy.postInit()

        log.info("Post-init done")
    }
}