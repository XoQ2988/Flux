package me.xoq.flux.utils.config;

import com.google.gson.JsonElement;
import me.xoq.flux.utils.input.Keybind;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FluxConfig {
    public List<ModuleEntry> modules = new ArrayList<>();
    public Map<String, JsonElement> settings = new LinkedHashMap<>();

    public static class ModuleEntry {
        public String name;
        public boolean enabled;
        public Keybind keybind;
    }
}
