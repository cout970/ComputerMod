package com.cout970.computer2.blocks;

import com.cout970.computer2.ComputerMod;
import com.cout970.computer2.tileentity.TileComputer;
import net.darkaqua.blacksmith.api.common.block.IBlockContainerDefinition;
import net.darkaqua.blacksmith.api.common.block.blockdata.IBlockData;
import net.darkaqua.blacksmith.api.common.block.methods.BlockMethod;
import net.darkaqua.blacksmith.api.common.entity.IPlayer;
import net.darkaqua.blacksmith.api.common.tileentity.ITileEntityDefinition;
import net.darkaqua.blacksmith.api.common.util.Direction;
import net.darkaqua.blacksmith.api.common.util.vectors.Vect3d;
import net.darkaqua.blacksmith.api.common.util.WorldRef;
import net.darkaqua.blacksmith.api.common.world.IWorld;

/**
 * Created by cout970 on 11/11/2015.
 */
public class BlockComputer extends BlockBase implements IBlockContainerDefinition, BlockMethod.OnActivated{

    public BlockComputer() {
        super("computer_block");
    }

    @Override
    public ITileEntityDefinition createTileEntity(IWorld world, IBlockData state) {
        return new TileComputer();
    }

    @Override
    public boolean onActivated(WorldRef ref, IBlockData data, IPlayer player, Direction side, Vect3d ray) {
        player.openGui(ComputerMod.MOD_IDENTIFIER, 0, ref);
        return true;
    }
}
