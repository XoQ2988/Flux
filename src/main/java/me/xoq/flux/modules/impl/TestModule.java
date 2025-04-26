package me.xoq.flux.modules.impl;

import me.xoq.flux.modules.Module;

public class TestModule extends Module {
    public TestModule() {
        super("test-module", "Module meant for testing stuff.");
    }

    @Override
    public void onEnabled() {
        info("Hello, World!");
    }
}