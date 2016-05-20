package com.cout970.computer2.blocks;

import com.cout970.computer2.ComputerMod;
import net.darkaqua.blacksmith.api.client.creativetab.ICreativeTab;
import net.darkaqua.blacksmith.api.client.render.block.IBlockModelProvider;
import net.darkaqua.blacksmith.api.client.render.block.defaults.UniqueModelProvider;
import net.darkaqua.blacksmith.api.client.render.model.factory.SimpleBlockModelFactory;
import net.darkaqua.blacksmith.api.common.block.BlockMaterialFactory;
import net.darkaqua.blacksmith.api.common.block.IBlockMaterial;
import net.darkaqua.blacksmith.api.common.block.defaults.DefaultBlockDefinition;
import net.darkaqua.blacksmith.api.common.util.ResourceReference;

/**
 * Created by cout970 on 31/12/2015.
 */
public class BlockBase extends DefaultBlockDefinition {

    public BlockBase(String name) {
        super(name);
    }

    @Override
    public IBlockMaterial getBlockMaterial() {
        return BlockMaterialFactory.IRON;
    }

    @Override
    public ICreativeTab getCreativeTab() {
        return ComputerMod.COMPUTER_TAB;
    }

    public IBlockModelProvider getModelProvider(){
        return new UniqueModelProvider(new SimpleBlockModelFactory(ComputerMod.MOD_IDENTIFIER, new ResourceReference(ComputerMod.MOD_ID, "blocks/"+blockName.toLowerCase())));
    }
}
