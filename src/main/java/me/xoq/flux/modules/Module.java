package me.xoq.flux.modules;

import me.xoq.flux.FluxClient;
import me.xoq.flux.utils.input.Keybind;
import me.xoq.flux.utils.misc.ChatUtils;
import me.xoq.flux.utils.misc.Utils;

public abstract class Module {
    private final String name;
    private final String title;
    private final String description;

    private boolean enabled;

    public Keybind keybind = Keybind.none();

    protected Module(String name, String description) {
        this.name = name;
        this.title = Utils.nameToTitle(name);
        this.description = description;
    }

    protected void onTick() { }

    public void toggle() {
        ChatUtils.info("Toggled §3" + title + "§r " +(!enabled ? "§2on" : "§4off"));
        setEnabled(!enabled);
    }

    public void setEnabled(boolean value){
        if (enabled == value) return;
        enabled = value;
        if (value) {
            FluxClient.EVENT_BUS.register(this);
            onEnabled();
        } else {
            FluxClient.EVENT_BUS.unregister(this);
            onDisabled();
        }
    }

    protected void onEnabled() { }
    protected void onDisabled() { }

    public void info(String message) { ChatUtils.info(title, message); }

    // getters
    public String getName()        { return name; }
    public String getTitle()       { return title; }
    public String getDescription() { return description; }
    public boolean isEnabled()     { return enabled; }
}