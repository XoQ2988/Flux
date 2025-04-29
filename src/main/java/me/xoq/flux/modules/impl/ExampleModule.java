package me.xoq.flux.modules.impl;

import me.xoq.flux.events.*;
import me.xoq.flux.events.misc.TickEvent;
import me.xoq.flux.modules.Module;
import me.xoq.flux.settings.BoolSetting;
import me.xoq.flux.settings.Setting;

public class ExampleModule extends Module {
    // 1. super call
    public ExampleModule() {
        super("example", "An example module", true);
    }

    // 2. settings
    private final Setting<Boolean> exampleToggle = settings.add(
            new BoolSetting.Builder()
                    .name("exampleToggle")
                    .description("An on/off toggle")
                    .defaultValue(true)
                    .build()
    );

    // 3. local vars
    private int counter;

    // 4. lifecycle overrides
    @Override
    protected void onEnabled() {
        info("ExampleModule enabled");
        counter = 0;
    }

    @Override
    protected void onDisabled() {
        info("ExampleModule disabled");
    }

    // 5. event handlers
    @EventHandler
    private void onTick(TickEvent event) {
        if (!exampleToggle.getValue()) return;
        counter++;
        if (counter % 20 == 0) {
            info("Ticked " + counter + " times");
        }
    }

    // 6. local helper methods
    private void doSomething() {
        // ...
    }
}