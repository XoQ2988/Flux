package me.xoq.flux.utils.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.xoq.flux.FluxClient;
import me.xoq.flux.modules.Module;
import me.xoq.flux.modules.Modules;
import me.xoq.flux.settings.SettingsManager;
import me.xoq.flux.utils.input.Keybind;

import java.io.*;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static FluxConfig config;

    public static void load() {
        try (Reader reader = new FileReader(FluxClient.FILE.toFile())) {
            config = GSON.fromJson(reader, FluxConfig.class);
        } catch (IOException e) {
            config = new FluxConfig();
        }

        // apply module entries…
        for (FluxConfig.ModuleEntry moduleEntry : config.modules) {
            Module module = Modules.getByName(moduleEntry.name);
            if (module == null) continue;
            module.setEnabled(moduleEntry.enabled);
            module.keybind = moduleEntry.keybind != null
                    ? moduleEntry.keybind
                    : Keybind.none();
        }

        // load all registered settings
        SettingsManager.getInstance().load(config);
    }

    public static void save() {
        // rebuild module list
        FluxConfig newCfg = new FluxConfig();
        for (Module m : Modules.getAll()) {
            FluxConfig.ModuleEntry e = new FluxConfig.ModuleEntry();
            e.name    = m.getName();
            e.enabled = m.isEnabled();
            e.keybind = m.keybind;
            newCfg.modules.add(e);
        }

        // snapshot settings into config
        SettingsManager.getInstance().save(newCfg);

        try (Writer writer = new FileWriter(FluxClient.FILE.toFile())) {
            GSON.toJson(newCfg, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
