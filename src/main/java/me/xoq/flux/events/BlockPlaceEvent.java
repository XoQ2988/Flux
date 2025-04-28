package me.xoq.flux.events;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

public class BlockPlaceEvent {
    private final BlockPos blockPos;
    private final Block block;
    private boolean cancelled = false;

    public BlockPlaceEvent(BlockPos blockPos, Block block) {
        this.blockPos = blockPos;
        this.block = block;
    }

    public void cancel()              { this.cancelled = true; }
    public boolean isCancelled()      { return this.cancelled; }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public Block getBlock() {
        return block;
    }
}

