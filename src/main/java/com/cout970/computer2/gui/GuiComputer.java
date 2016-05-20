package com.cout970.computer2.gui;

import com.cout970.computer2.network.ChannelManager;
import com.cout970.computer2.network.MessageButtonClick;
import com.cout970.computer2.tileentity.TileComputer;
import net.darkaqua.blacksmith.api.common.entity.IPlayer;
import net.darkaqua.blacksmith.api.common.gui.IContainer;
import net.darkaqua.blacksmith.api.common.gui.IGui;
import net.darkaqua.blacksmith.api.common.gui.IGuiComponent;
import net.darkaqua.blacksmith.api.common.gui.defaults.DefaultSlotDefinition;
import net.darkaqua.blacksmith.api.common.gui.defaults.components.AbstractButton;
import net.darkaqua.blacksmith.api.common.gui.defaults.components.SimpleButton;
import net.darkaqua.blacksmith.api.common.util.vectors.Vect2i;
import net.darkaqua.blacksmith.api.common.util.WorldRef;

/**
 * Created by cout970 on 24/01/2016.
 */
public class GuiComputer extends GuiBase {

    protected TileComputer computer;

    public GuiComputer(IPlayer player, WorldRef ref) {
        super(player, ref);
        computer = (TileComputer) ref.getTileEntity().getTileEntityDefinition();
    }

    @Override
    public void init(IGui parent) {
        parent.addComponent(new SimpleButton(new Vect2i(10,54), new Vect2i(18, 18), buttonsTexture, this::onPress, Vect2i.nullVector()).setId(0));
        parent.addComponent(new SimpleButton(new Vect2i(10+18,54), new Vect2i(18, 18), buttonsTexture, this::onPress, new Vect2i(18,0)).setId(1));
        parent.addComponent(new SimpleButton(new Vect2i(10+18*2,54), new Vect2i(18, 18), buttonsTexture, this::onPress, new Vect2i(18*2,0)).setId(2));
    }

    public boolean onPress(AbstractButton button, Vect2i mouse, IGuiComponent.MouseButton mouseButton){
        MessageButtonClick msg = new MessageButtonClick(button.getId(), ref.getWorld().getWorldDimension(), ref.getPosition());
        ChannelManager.channel.sendToServer(msg);
        return true;
    }

    @Override
    protected String getBackground() {
        return "textures/gui/cpu.png";
    }

    @Override
    public void initContainer(IContainer container) {
        int y = 9;
        int x = 11;
        container.addSlot(new DefaultSlotDefinition(computer.getInv(), 0, new Vect2i(x, y)));
        container.addSlot(new DefaultSlotDefinition(computer.getInv(), 1, new Vect2i(x + 18, y)));
        container.addSlot(new DefaultSlotDefinition(computer.getInv(), 2, new Vect2i(x + 18 * 2, y)));
        container.addSlot(new DefaultSlotDefinition(computer.getInv(), 3, new Vect2i(x, y + 18)));
        container.addSlot(new DefaultSlotDefinition(computer.getInv(), 4, new Vect2i(x + 18, y + 18)));
        container.addSlot(new DefaultSlotDefinition(computer.getInv(), 5, new Vect2i(x + 18 * 2, y + 18)));
        bindPlayerInventory(container);
    }
}
