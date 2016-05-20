package com.cout970.computer.util.pathfinding;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by cout970 on 13/11/2015.
 */
public abstract class PathFinding {

    protected HashSet<Vec3i> scanned;
    protected Queue<PathNode> toScan;
    protected Vec3i start;
    protected Vec3i end;

    public PathFinding() {
        scanned = new HashSet<>();
        toScan = new LinkedList<>();
    }

    public void setStart(Vec3i vec) {
        start = vec;
    }

    public void setEnd(Vec3i vec) {
        end = vec;
    }

    public void addAdjacentNodes(PathNode node) {
        for(EnumFacing d : EnumFacing.values())
            addNode(node, d);
    }

    public PathNode scan(PathNode node) {

        scanned.add(node.getPosition());
        if (isEnd(node)) {
            return node;
        }
        addAdjacentNodes(node);
        return null;
    }

    public abstract void addNode(PathNode node, EnumFacing dir);

    protected abstract boolean isEnd(PathNode node);

    public LinkedList<Vec3i> getPath(){
        PathNode node = findEnd();

        if (node != null) {
            LinkedList<Vec3i> path = new LinkedList<>();
            for (PathNode current = node; current.getBefore() != null; current = current.getBefore()) {
                path.addFirst(current.getPosition());
            }
            return path;
        }

        return null;
    }

    public PathNode findEnd(){
        toScan.clear();
        scanned.clear();
        toScan.add(new PathNode(start, null));
        PathNode node = null;

        while (!toScan.isEmpty()) {
            node = scan(toScan.poll());
            if (node != null) {
                break;
            }
        }

        return node;
    }
}
