package me.xoq.flux.events.world;

import me.xoq.flux.events.CancellableEvent;
import net.minecraft.block.BlockState;

public class BlockInteractEvent extends CancellableEvent {
    private final BlockState blockState;

    public BlockInteractEvent(BlockState blockState) {
        this.blockState  = blockState;
    }

    public BlockState getBlockState() {
        return blockState;
    }
}
