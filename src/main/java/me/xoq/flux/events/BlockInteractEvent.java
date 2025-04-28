package me.xoq.flux.events;

import net.minecraft.block.BlockState;

public class BlockInteractEvent {
    private final BlockState blockState;
    private boolean cancelled = false;

    public BlockInteractEvent(BlockState blockState) {
        this.blockState  = blockState;
    }

    public BlockState getBlockState() {
        return blockState;
    }

    public void cancel()              { this.cancelled = true; }
    public boolean isCancelled()      { return this.cancelled; }
}
