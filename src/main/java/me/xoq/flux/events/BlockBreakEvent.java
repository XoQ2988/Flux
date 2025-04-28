package me.xoq.flux.events;

import net.minecraft.util.math.BlockPos;

public class BlockBreakEvent {
    private final BlockPos blockPos;
    private boolean cancelled = false;

    public BlockBreakEvent(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public void cancel()              { this.cancelled = true; }
    public boolean isCancelled()      { return this.cancelled; }

    public BlockPos getBlockPos() {
        return blockPos;
    }
}
