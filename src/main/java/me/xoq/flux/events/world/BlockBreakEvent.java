package me.xoq.flux.events.world;

import me.xoq.flux.events.CancellableEvent;
import net.minecraft.util.math.BlockPos;

public class BlockBreakEvent extends CancellableEvent {
    private final BlockPos blockPos;

    public BlockBreakEvent(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }
}
