package com.cout970.computer.util.pathfinding;

import com.cout970.computer.api.IComputer;
import com.cout970.computer.api.IModule;
import com.cout970.computer.api.IPeripheral;
import com.cout970.computer.api.IPeripheralProvider;
import com.cout970.computer.util.ObjectScannerKt;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by cout970 on 13/11/2015.
 */
public class ComputerPathFinder extends PathFinding {

    private IPeripheral result;
    private World world;
    private int address;

    public ComputerPathFinder(World w, int address) {
        world = w;
        this.address = address;
    }

    @Override
    public void addNode(PathNode node, EnumFacing dir) {
        BlockPos vec = new BlockPos(node.getPosition()).add(dir.getDirectionVec());

        if (scanned.contains(vec)) return;

        TileEntity tile = world.getTileEntity(vec);
        if (tile != null) {//TODO add a cable to connect peripherals
            toScan.add(new PathNode(vec, node));
        }
    }

    @Override
    protected boolean isEnd(PathNode node) {
        TileEntity tile = world.getTileEntity(new BlockPos(node.getPosition()));

        if (tile != null) {
            IPeripheralProvider provider = ObjectScannerKt.findInTileEntity(tile, IPeripheralProvider.IDENTIFIER, getDirection(node));
            IComputer computer = ObjectScannerKt.findInTileEntity(tile, IComputer.IDENTIFIER, getDirection(node));

            if (provider != null) {
                for (IPeripheral per : provider.getPeripherals()) {
                    if (per.getAddress() == address) {
                        result = per;
                        return true;
                    }
                }
            } else if (computer != null) {
                IModule[] modules = computer.getModules();
                if (modules != null) {
                    for (IModule mod : modules) {
                        if (mod instanceof IPeripheral) {
                            IPeripheral per = (IPeripheral) mod;
                            if (per.getAddress() == address) {
                                result = per;
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private EnumFacing getDirection(PathNode node) {
        PathNode before = node.getBefore();
        if (before == null) {
            return null;
        }
        BlockPos dir = new BlockPos(node.getPosition()).subtract(before.getPosition());
        for (EnumFacing d : EnumFacing.values()) {
            if (d.getDirectionVec().equals(dir)) {
                return d;
            }
        }
        return null;
    }

    public IPeripheral getResult() {
        return result;
    }
}
