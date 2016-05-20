package com.cout970.computer.util.pathfinding;

import net.minecraft.util.math.Vec3i;

/**
 * Created by cout970 on 13/11/2015.
 */
public class PathNode {

    private Vec3i position;
    private PathNode before;

    public PathNode(Vec3i position, PathNode node) {
        this.position = position;
        before = node;
    }

    public boolean isStart(){
        return before == null;
    }

    public PathNode getBefore() {
        return before;
    }

    public void setBefore(PathNode before) {
        this.before = before;
    }

    public Vec3i getPosition() {
        return position;
    }

    public void setPosition(Vec3i position) {
        this.position = position;
    }
}
