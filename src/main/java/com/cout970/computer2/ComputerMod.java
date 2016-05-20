package com.cout970.computer2;

import com.cout970.computer.api.IComputer;
import com.cout970.computer.api.IPeripheralProvider;
import com.cout970.computer.api.IStorageDevice;
import com.cout970.computer2.gui.GuiHandler;
import com.cout970.computer2.items.ItemFloppyDisk;
import com.cout970.computer2.network.ChannelManager;
import com.cout970.computer2.tileentity.TileComputer;
import com.cout970.computer2.tileentity.TileOldMonitor;
import net.darkaqua.blacksmith.api.Game;
import net.darkaqua.blacksmith.api.client.creativetab.CreativeTabFactory;
import net.darkaqua.blacksmith.api.client.creativetab.ICreativeTab;
import net.darkaqua.blacksmith.api.common.event.EventSubscribe;
import net.darkaqua.blacksmith.api.common.event.modloader.IInitEvent;
import net.darkaqua.blacksmith.api.common.event.modloader.IPostInitEvent;
import net.darkaqua.blacksmith.api.common.event.modloader.IPreInitEvent;
import net.darkaqua.blacksmith.api.common.intermod.IInterfaceIdentifier;
import net.darkaqua.blacksmith.api.common.inventory.ItemStackFactory;
import net.darkaqua.blacksmith.api.common.item.Items;
import net.darkaqua.blacksmith.api.common.modloader.BlacksmithMod;
import net.darkaqua.blacksmith.api.common.modloader.IModIdentifier;
import net.darkaqua.blacksmith.api.common.modloader.ModIdentifier;
import net.darkaqua.blacksmith.api.common.modloader.ModInstance;
import net.darkaqua.blacksmith.api.common.storage.IDataCompound;
import net.darkaqua.blacksmith.api.common.util.Direction;

import java.io.File;

@BlacksmithMod(id = ComputerMod.MOD_ID, name = ComputerMod.MOD_NAME, version = ComputerMod.MOD_VERSION)
public class ComputerMod {

    public static final String MOD_ID = "computer";
    public static final String MOD_NAME = "Computer";
    public static final String MOD_VERSION = "0.0.0";
    private static boolean DEBUG = Game.isDeobfuscatedEnvironment();
    public static File modsFile;
    public static ICreativeTab COMPUTER_TAB;
    public static String DEV_HOME;

    @ModInstance
    public static ComputerMod INSTANCE;
    @ModIdentifier
    public static IModIdentifier MOD_IDENTIFIER;

    @EventSubscribe
    public void preInit(IPreInitEvent e) {
        Log.LOGGER = e.getModLog();
        Log.info("Starting PreInit...");
        //Save File Parent
        modsFile = e.getSourceFile().getParentFile();
        //Mod creative tab
        COMPUTER_TAB = CreativeTabFactory.createCreativeTab("computer", ItemStackFactory.createItemStack(Items.IRON_INGOT.getItem()));
        //Blocks/Items
        ManagerBlocks.initBlocks();
        ManagerItems.initItems();
        //Network
        ChannelManager.init();
        //GUI
        Game.getCommonHandler().getGuiRegistry().registerGuiCreationHandler(GuiHandler.INSTANCE);
        //Renders
        if(Game.isClient()){
            ManagerBlocks.initBlockRenders();
            ManagerItems.initItemRenders();
        }
        //APIs
        initAPIs();

        //Language file
        if (DEBUG) {
            //BEGIN FINDING OF SOURCE DIR
            File temp = e.getModConfigurationDirectory();
            while (temp != null && temp.isDirectory()) {
                Log.debug(temp);
                if (new File(temp, "build.gradle").exists()) {
                    DEV_HOME = temp.getAbsolutePath();
                    Log.info("Found source code directory at " + DEV_HOME);
                    break;
                }
                temp = temp.getParentFile();
            }
            if (DEV_HOME == null) {
                throw new RuntimeException("Could not find source code directory!");
            }
            //END FINDING OF SOURCE DIR
            LangHelper.addTexts();
            LangHelper.setupLangFile();
        }
        Log.info("PreInit done");
    }

    @EventSubscribe
    public void init(IInitEvent e) {}

    @EventSubscribe
    public void postInit(IPostInitEvent e) {}

    private static void initAPIs(){
        Game.getCommonHandler().getInterModRegistry().registerInterface(IPeripheralProvider.class, new IInterfaceIdentifier.IStorageHandler() {
            @Override
            public IDataCompound saveData(IInterfaceIdentifier identifier, Object instance, Direction dir) {
                return null;
            }

            @Override
            public void loadData(IInterfaceIdentifier identifier, Object instance, Direction dir, IDataCompound data) {

            }
        }, TileOldMonitor::new);

        Game.getCommonHandler().getInterModRegistry().registerInterface(IComputer.class, new IInterfaceIdentifier.IStorageHandler() {
            @Override
            public IDataCompound saveData(IInterfaceIdentifier identifier, Object instance, Direction dir) {
                return null;
            }

            @Override
            public void loadData(IInterfaceIdentifier identifier, Object instance, Direction dir, IDataCompound data) {

            }
        }, TileComputer::new);

        Game.getCommonHandler().getInterModRegistry().registerInterface(IStorageDevice.class, new IInterfaceIdentifier.IStorageHandler() {
            @Override
            public IDataCompound saveData(IInterfaceIdentifier identifier, Object instance, Direction dir) {
                return null;
            }

            @Override
            public void loadData(IInterfaceIdentifier identifier, Object instance, Direction dir, IDataCompound data) {

            }
        }, ItemFloppyDisk::new);
    }
}

