package me.xoq.flux.utils.config;

import me.xoq.flux.settings.BaseSetting;
import me.xoq.flux.settings.SettingsManager;

public class SettingHelper {
    private final SettingsManager mgr;
    private final String namespace;

    public SettingHelper(SettingsManager mgr, String namespace) {
        this.mgr = mgr;
        this.namespace = namespace;
    }

    public <T> BaseSetting<T> add(BaseSetting<T> setting) {
        setting.setKey(namespace + "." + setting.getKey());
        mgr.register(setting);
        return setting;
    }
}