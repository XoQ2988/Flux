package me.xoq.flux.events;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class BlockAttackEvent {
    private final BlockPos blockPos;
    private final Direction direction;
    private boolean cancelled = false;

    public BlockAttackEvent(BlockPos blockPos, Direction direction) {
        this.blockPos  = blockPos;
        this.direction = direction;
    }

    public BlockPos getBlockPos()     { return blockPos; }
    public Direction getDirection()   { return direction; }

    public void cancel()              { this.cancelled = true; }
    public boolean isCancelled()      { return this.cancelled; }
}