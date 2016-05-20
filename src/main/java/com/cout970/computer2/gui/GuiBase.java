package com.cout970.computer2.gui;

import com.cout970.computer2.ComputerMod;
import net.darkaqua.blacksmith.api.common.entity.IPlayer;
import net.darkaqua.blacksmith.api.common.gui.IGui;
import net.darkaqua.blacksmith.api.common.gui.defaults.components.BackgroundComponent;
import net.darkaqua.blacksmith.api.common.gui.defaults.DefaultGuiDefinition;
import net.darkaqua.blacksmith.api.common.util.ResourceReference;
import net.darkaqua.blacksmith.api.common.util.WorldRef;

/**
 * Created by cout970 on 24/01/2016.
 */
public abstract class GuiBase extends DefaultGuiDefinition{

    public static final ResourceReference buttonsTexture = new ResourceReference(ComputerMod.MOD_ID, "textures/gui/buttons.png");

    public GuiBase(IPlayer player, WorldRef ref) {
        super(player, ref);
    }

    @Override
    public void initGui(IGui parent) {
        parent.addComponent(new BackgroundComponent(new ResourceReference(ComputerMod.MOD_ID, getBackground())));
        init(parent);
    }

    public abstract void init(IGui parent);

    protected abstract String getBackground();
}
