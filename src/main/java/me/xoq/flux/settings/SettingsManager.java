package me.xoq.flux.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import me.xoq.flux.utils.config.FluxConfig;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class SettingsManager {
    private static final SettingsManager INSTANCE = new SettingsManager();
    private final Map<String, Setting<?>> settings = new LinkedHashMap<>();

    private SettingsManager() {}

    public static SettingsManager getInstance() {
        return INSTANCE;
    }

    public <T> void register(Setting<T> setting) {
        if (settings.containsKey(setting.getKey()))
            throw new IllegalArgumentException("Duplicate setting: " + setting.getKey());
        settings.put(setting.getKey(), setting);
    }

    @SuppressWarnings("unchecked")
    public <T> Setting<T> get(String key) {
        return (Setting<T>) settings.get(key);
    }

    @SuppressWarnings("unchecked")
    public void load(FluxConfig cfg) {
        for (Map.Entry<String, Setting<?>> e : settings.entrySet()) {
            String key = e.getKey();
            JsonElement el = cfg.settings.get(key);
            if (el == null) continue;
            Setting<?> s = e.getValue();
            Object val;
            if (s.getDefault() instanceof Boolean) {
                val = el.getAsBoolean();
            } else if (s.getDefault() instanceof Integer) {
                val = el.getAsInt();
            } else if (s.getDefault() instanceof Number) {
                val = el.getAsDouble();
            } else if (s.getDefault() instanceof Enum<?>) {
                String name = el.getAsString();
                EnumSetting<?> es = (EnumSetting<?>) s;
                val = Enum.valueOf(es.getEnumType(), name);
            } else {
                // fallback to string
                val = el.getAsString();
            }
            ((Setting<Object>) s).setValue(val);
        }
    }

    public void save(FluxConfig cfg) {
        cfg.settings.clear();
        for (Map.Entry<String, Setting<?>> e : settings.entrySet()) {
            String key = e.getKey();
            Object v = e.getValue().getValue();
            JsonElement el;
            if (v instanceof Boolean b) {
                el = new JsonPrimitive(b);
            } else if (v instanceof Number n) {
                el = new JsonPrimitive(n);
            } else if (v instanceof Enum<?> en) {
                el = new JsonPrimitive(en.name());
            } else {
                el = new JsonPrimitive(v.toString());
            }
            cfg.settings.put(key, el);
        }
    }

    public Set<String> getRegisteredKeys() {
        return settings.keySet();
    }

}
