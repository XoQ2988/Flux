package me.xoq.flux.events.world;

import me.xoq.flux.events.CancellableEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class BlockAttackEvent extends CancellableEvent {
    private final BlockPos blockPos;
    private final Direction direction;

    public BlockAttackEvent(BlockPos blockPos, Direction direction) {
        this.blockPos  = blockPos;
        this.direction = direction;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public Direction getDirection() {
        return direction;
    }
}