package me.xoq.flux.utils.config;

import me.xoq.flux.settings.BaseSetting;
import me.xoq.flux.settings.SettingsManager;

public class SettingHelper {
    private final SettingsManager settingsManager;
    private final String namespacePrefix;

    public SettingHelper(SettingsManager settingsManager, String namespace) {
        this.settingsManager = settingsManager;
        this.namespacePrefix = namespace + ".";
    }

    public <T> BaseSetting<T> add(BaseSetting<T> setting) {
        setting.setKey(namespacePrefix + setting.getKey());
        settingsManager.register(setting);
        return setting;
    }
}