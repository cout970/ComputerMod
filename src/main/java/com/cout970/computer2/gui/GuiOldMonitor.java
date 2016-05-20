package com.cout970.computer2.gui;

import com.cout970.computer2.ComputerMod;
import com.cout970.computer.api.IPeripheralMonitor;
import com.cout970.computer2.gui.components.MonitorComponent;
import com.cout970.computer2.tileentity.TileOldMonitor;
import net.darkaqua.blacksmith.api.common.entity.IPlayer;
import net.darkaqua.blacksmith.api.common.gui.IContainer;
import net.darkaqua.blacksmith.api.common.gui.IGui;
import net.darkaqua.blacksmith.api.common.gui.IGuiComponent;
import net.darkaqua.blacksmith.api.common.gui.defaults.DefaultGuiDefinition;
import net.darkaqua.blacksmith.api.common.util.ResourceReference;
import net.darkaqua.blacksmith.api.common.util.vectors.Vect2d;
import net.darkaqua.blacksmith.api.common.util.vectors.Vect2i;
import net.darkaqua.blacksmith.api.common.util.WorldRef;

/**
 * Created by cout970 on 07/02/2016.
 */
public class GuiOldMonitor extends DefaultGuiDefinition {

    private static final ResourceReference texture = new ResourceReference(ComputerMod.MOD_ID, "textures/gui/old_monitor.png");
    private TileOldMonitor tile;

    public GuiOldMonitor(IPlayer player, WorldRef ref) {
        super(player, ref);
        tile = (TileOldMonitor) ref.getTileEntity().getTileEntityDefinition();
    }

    public IPeripheralMonitor getMonitor() {
        return tile.monitor;
    }

    @Override
    public void initGui(IGui parent) {
        parent.addComponent(new Component());
        parent.addComponent(new MonitorComponent(tile.monitor) {
            public void sendKey(int key, int num) {
                super.sendKey(key, num);
                tile.sendMonitorDataToServer();
            }
        });
    }

    @Override
    public Vect2i getGuiSize() {
        return new Vect2i(350, 230);
    }

    @Override
    public void initContainer(IContainer container) {
    }

    @Override
    public void detectAndSendChanges() {
        tile.sendMonitorDataToClient(player);
    }

    private class Component implements IGuiComponent {

        @Override
        public void renderBackground(IGui gui, Vect2i mouse, float partialTicks) {
            gui.getGuiRenderer().bindTexture(texture);
            gui.getGuiRenderer().drawRectangleWithCustomSizedTexture(gui.getGuiStartingPoint(), gui.getGuiSize(), Vect2d.nullVector(), new Vect2d(512, 256));
        }

        @Override
        public void renderForeground(IGui gui, Vect2i mouse) {
        }

        @Override
        public void onMouseClick(IGui gui, Vect2i mouse, MouseButton button) {
            IPeripheralMonitor monitor = tile.monitor;
            mouse.sub(gui.getGuiStartingPoint()).sub(15, 15);
            Vect2i point = new Vect2i(mouse.getX() / 4, mouse.getY() / 4);
            if (point.getX() >= 0 && point.getY() >= 0 && point.getX() < monitor.getColumns() && point.getY() < monitor.getLines()){
                monitor.onCursorClick(point, button);
            }
        }

        @Override
        public boolean onKeyPressed(IGui gui, int code, char character) {
            return false;
        }
    }
}