package me.xoq.flux.events.world;

import me.xoq.flux.events.CancellableEvent;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

public class BlockPlaceEvent extends CancellableEvent {
    private final BlockPos blockPos;
    private final Block block;

    public BlockPlaceEvent(BlockPos blockPos, Block block) {
        this.blockPos = blockPos;
        this.block = block;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public Block getBlock() {
        return block;
    }
}

