package com.cout970.computer2.items;

import com.cout970.computer2.ComputerMod;
import net.darkaqua.blacksmith.api.common.item.defaults.DefaultItemDefinition;
import net.darkaqua.blacksmith.api.client.render.item.IItemModelProvider;
import net.darkaqua.blacksmith.api.client.render.item.defaults.PlaneItemModelProvider;
import net.darkaqua.blacksmith.api.common.util.ResourceReference;

/**
 * Created by cout970 on 08/02/2016.
 */
public class ItemBase extends DefaultItemDefinition {

    public ItemBase(String name) {
        super(name);
    }

    public IItemModelProvider getModelProvider() {
        return new PlaneItemModelProvider(ComputerMod.MOD_IDENTIFIER, new ResourceReference(ComputerMod.MOD_ID, "items/" + name.toLowerCase()));
    }
}
