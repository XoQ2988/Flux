package me.xoq.flux.modules.impl;

import me.xoq.flux.events.*;
import me.xoq.flux.modules.Module;

public class TestModule extends Module {
    public TestModule() {
        super("test-module", "Module meant for testing stuff");
    }

    @EventHandler
    private void onInteractBlock(BlockInteractEvent event) {
        event.cancel();
    }

    @EventHandler
    private void onAttackBlock(BlockAttackEvent event) {
        event.cancel();
    }

    @EventHandler
    private void onBreakBlock(BlockBreakEvent event) {
        event.cancel();
    }

    @EventHandler
    private void onPlaceBlock(BlockPlaceEvent event) {
        event.cancel();
    }
}