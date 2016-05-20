package com.cout970.computer2;

import com.cout970.computer2.items.ItemBase;
import com.cout970.computer2.items.ItemFloppyDisk;
import net.darkaqua.blacksmith.api.Game;
import net.darkaqua.blacksmith.api.common.inventory.IItemStack;
import net.darkaqua.blacksmith.api.common.inventory.ItemStackFactory;
import net.darkaqua.blacksmith.api.common.item.IItem;

/**
 * Created by cout970 on 08/02/2016.
 */
public enum ManagerItems {

    FloppyDisk(new ItemFloppyDisk("floppy_disk"), "Floppy Disk");

    private ItemBase definition;
    private IItem item;
    private String identifier;

    ManagerItems(ItemBase definition, String englishName) {
        this.definition = definition;
        identifier = definition.getUnlocalizedName();
        LangHelper.addName("item." + definition.getUnlocalizedName(), englishName);
    }

    public static void initItems() {
        for (ManagerItems b : ManagerItems.values()) {
            b.item = Game.getCommonHandler().getItemRegistry().registerItemDefinition(b.definition, b.identifier);
        }
    }

    public static void initItemRenders() {
        for (ManagerItems b : ManagerItems.values()) {
            Game.getClientHandler().getRenderRegistry().registerItemModelProvider(b.definition, b.definition.getModelProvider());
        }
    }

    public ItemBase getDefinition() {
        return definition;
    }

    public IItem getItem() {
        return item;
    }

    public String getIdentifier() {
        return identifier;
    }

    public IItemStack toItemStack() {
        return ItemStackFactory.createItemStack(getItem());
    }
}
