package me.xoq.flux.utils.config;

import me.xoq.flux.settings.Setting;
import me.xoq.flux.settings.SettingsManager;

public class SettingHelper {
    private final SettingsManager mgr;
    private final String namespace;

    public SettingHelper(SettingsManager mgr, String namespace) {
        this.mgr = mgr;
        this.namespace = namespace;
    }

    public <T> Setting<T> add(Setting<T> original) {
        Setting<T> wrapped = new Setting<>() {
            @Override public String getKey()         { return namespace + "." + original.getKey(); }
            @Override public String getName()        { return original.getName(); }
            @Override public String getTitle()        { return original.getTitle(); }
            @Override public String getDescription() { return original.getDescription(); }
            @Override public T getDefault()          { return original.getDefault(); }
            @Override public T getValue()            { return original.getValue(); }
            @Override public void setValue(T v)      { original.setValue(v); }
        };
        mgr.register(wrapped);
        return wrapped;
    }
}