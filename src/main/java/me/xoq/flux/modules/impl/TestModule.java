package me.xoq.flux.modules.impl;

import me.xoq.flux.modules.Module;
import me.xoq.flux.settings.IntSetting;
import me.xoq.flux.settings.Setting;
import me.xoq.flux.settings.StringSetting;

public class TestModule extends Module {
    private final Setting<String> greeting = settings.add(
            new StringSetting.Builder()
                    .name("greeting")
                    .description("Message to print")
                    .defaultValue("Hello, World!")
                    .build()
    );

    private final Setting<Integer> repeatCount = settings.add(
            new IntSetting.Builder()
                    .name("repeat-count")
                    .description("How many times to repeat")
                    .defaultValue(1)
                    .range(1, 5)
                    .build()
    );

    public TestModule() {
        super("test-module", "Module meant for testing stuff");
    }

    @Override
    public void onEnabled() {
        for (int i = 0; i < repeatCount.getValue(); i++) {
            info(greeting.getValue());
        }
    }
}