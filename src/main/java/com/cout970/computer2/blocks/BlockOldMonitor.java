package com.cout970.computer2.blocks;

import com.cout970.computer2.ComputerMod;
import com.cout970.computer2.tileentity.TileOldMonitor;
import net.darkaqua.blacksmith.api.client.render.block.IBlockModelProvider;
import net.darkaqua.blacksmith.api.client.render.block.defaults.UniqueModelProvider;
import net.darkaqua.blacksmith.api.client.render.model.factory.RotableBlockModelFactory;
import net.darkaqua.blacksmith.api.common.block.IBlockContainerDefinition;
import net.darkaqua.blacksmith.api.common.block.IRotableBlock;
import net.darkaqua.blacksmith.api.common.block.blockdata.IBlockData;
import net.darkaqua.blacksmith.api.common.block.methods.BlockMethod;
import net.darkaqua.blacksmith.api.common.entity.IPlayer;
import net.darkaqua.blacksmith.api.common.tileentity.ITileEntityDefinition;
import net.darkaqua.blacksmith.api.common.util.Direction;
import net.darkaqua.blacksmith.api.common.util.ResourceReference;
import net.darkaqua.blacksmith.api.common.util.WorldRef;
import net.darkaqua.blacksmith.api.common.util.vectors.Vect3d;
import net.darkaqua.blacksmith.api.common.world.IWorld;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cout970 on 31/12/2015.
 */
public class BlockOldMonitor extends BlockBase implements IBlockContainerDefinition, IRotableBlock, BlockMethod.OnActivated {

    public BlockOldMonitor() {
        super("old_monitor");
    }

    @Override
    public ITileEntityDefinition createTileEntity(IWorld world, IBlockData state) {
        return new TileOldMonitor();
    }

    public IBlockModelProvider getModelProvider() {
        Map<Direction, ResourceReference> textures = new HashMap<>();
        for(Direction dir : Direction.values()){
            if (dir == Direction.NORTH){
                textures.put(dir, new ResourceReference(ComputerMod.MOD_ID, "blocks/old_monitor_front"));
            }else{
                textures.put(dir, new ResourceReference(ComputerMod.MOD_ID, "blocks/old_monitor"));
            }
        }
        return new UniqueModelProvider(new RotableBlockModelFactory(ComputerMod.MOD_IDENTIFIER, this, textures));
    }

    @Override
    public Direction[] getValidRotations() {
        return Direction.Axis.Y.getPerpendicularDirections();
    }

    @Override
    public Direction getActualRotation(IBlockData data) {
        return Direction.NORTH;
    }

    @Override
    public IBlockData stepRotation(IBlockData data) {
        return data;
    }

    @Override
    public IBlockData withRotation(IBlockData data, Direction dir) {
        return parent.getDefaultBlockData();
    }

    @Override
    public boolean onActivated(WorldRef ref, IBlockData data, IPlayer player, Direction side, Vect3d ray) {
        player.openGui(ComputerMod.MOD_IDENTIFIER, 0, ref);
        return true;
    }
}
