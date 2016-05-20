package com.cout970.computer2.gui;

import com.cout970.computer2.tileentity.TileComputer;
import com.cout970.computer2.tileentity.TileOldMonitor;
import net.darkaqua.blacksmith.api.common.entity.IPlayer;
import net.darkaqua.blacksmith.api.common.gui.IGuiCreationHandler;
import net.darkaqua.blacksmith.api.common.gui.IGuiDefinition;
import net.darkaqua.blacksmith.api.common.tileentity.ITileEntityDefinition;
import net.darkaqua.blacksmith.api.common.util.GameSide;
import net.darkaqua.blacksmith.api.common.util.WorldRef;

/**
 * Created by cout970 on 24/01/2016.
 */
public class GuiHandler implements IGuiCreationHandler {

    public static final GuiHandler INSTANCE = new GuiHandler();

    @Override
    public IGuiDefinition getGuiDefinition(IPlayer player, WorldRef ref, int id, GameSide side) {
        ITileEntityDefinition def = ref.getTileEntity().getTileEntityDefinition();
        if (def instanceof TileComputer) {
            return new GuiComputer(player, ref);
        }else if(def instanceof TileOldMonitor){
            return new GuiOldMonitor(player, ref);
        }
        return null;
    }
}
