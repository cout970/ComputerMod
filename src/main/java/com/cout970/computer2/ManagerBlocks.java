package com.cout970.computer2;

import com.cout970.computer2.blocks.BlockBase;
import com.cout970.computer2.blocks.BlockComputer;
import com.cout970.computer2.blocks.BlockOldMonitor;
import com.cout970.computer2.tileentity.TileComputer;
import com.cout970.computer2.tileentity.TileOldMonitor;
import net.darkaqua.blacksmith.api.Game;
import net.darkaqua.blacksmith.api.client.render.tileentity.ITileEntityRenderer;
import net.darkaqua.blacksmith.api.common.block.IBlock;
import net.darkaqua.blacksmith.api.common.block.IBlockDefinition;
import net.darkaqua.blacksmith.api.common.tileentity.ITileEntityDefinition;

/**
 * Created by cout970 on 16/12/2015.
 */
public enum ManagerBlocks {

    Computer(new BlockComputer(), "Computer", TileComputer.class),
    OldMonitor(new BlockOldMonitor(), "Old Monitor", TileOldMonitor.class);

    private BlockBase definition;
    private IBlock block;
    private String identifier;
    private Class<? extends ITileEntityDefinition> tileEntityClass;
    private ITileEntityRenderer<?> tileRenderer;

    ManagerBlocks(BlockBase def, String englishName){
        definition = def;
        identifier = def.getUnlocalizedName();
    }

    ManagerBlocks(BlockBase def, String englishName, Class<? extends ITileEntityDefinition> tile){
        this(def, englishName);
        tileEntityClass = tile;
    }

    ManagerBlocks(BlockBase def, String englishName, Class<? extends ITileEntityDefinition> tile, ITileEntityRenderer renderer){
        this(def, englishName, tile);
        tileRenderer = renderer;
    }

    public static void initBlocks(){
        for(ManagerBlocks b : ManagerBlocks.values()){
            b.block = Game.getCommonHandler().getBlockRegistry().registerBlockDefinition(b.definition, b.identifier);
            if (b.tileEntityClass != null) {
                Game.getCommonHandler().getTileEntityRegistry().registerTileEntityDefinition(b.tileEntityClass, b.identifier);
            }
        }
    }

    public static void initBlockRenders(){
        for(ManagerBlocks b : ManagerBlocks.values()){
            Game.getClientHandler().getRenderRegistry().registerBlockModelProvider(b.definition, b.definition.getModelProvider());
            if (b.tileRenderer != null){
                Game.getClientHandler().getRenderRegistry().registerTileEntityRenderer(b.tileEntityClass, b.tileRenderer);
            }
        }
    }

    public IBlockDefinition getDefinition() {
        return definition;
    }

    public String getIdentifier() {
        return identifier;
    }

    public IBlock getBlock() {
        return block;
    }
}
