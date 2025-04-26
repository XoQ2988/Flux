package me.xoq.flux.utils.config;

import me.xoq.flux.utils.input.Keybind;

import java.util.ArrayList;
import java.util.List;

public class FluxConfig {
    public List<ModuleEntry> modules = new ArrayList<>();

    public static class ModuleEntry {
        public String name;
        public boolean enabled;
        public Keybind keybind;
    }
}
