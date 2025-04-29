package me.xoq.flux.utils.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.xoq.flux.FluxClient;
import me.xoq.flux.modules.Module;
import me.xoq.flux.modules.Modules;
import me.xoq.flux.settings.SettingsManager;
import me.xoq.flux.utils.input.Keybind;

import java.io.*;

public final class ConfigManager {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static FluxConfig config;

    private ConfigManager() { }

    public static void load() {
        File configFile = FluxClient.CONFIG_FILE.toFile();

        if (configFile.exists()) {
            try (Reader fileReader = new FileReader(configFile)) {
                config = GSON.fromJson(fileReader, FluxConfig.class);
            } catch (IOException ioException) {
                FluxClient.LOG.error("Failed to read config, using defaults", ioException);
                config = new FluxConfig();
            }
        } else {
            // first run or deleted file
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
        FluxConfig newCfg = new FluxConfig();

        for (Module module : Modules.getAll()) {
            FluxConfig.ModuleEntry moduleEntry = new FluxConfig.ModuleEntry();
            moduleEntry.name    = module.getName();
            moduleEntry.enabled = module.isEnabled();
            moduleEntry.keybind = module.keybind;
            newCfg.modules.add(moduleEntry);
        }

        // snapshot settings into config
        SettingsManager.getInstance().save(newCfg);

        // ensure parent directory exists
        File configFile = FluxClient.CONFIG_FILE.toFile();
        File configDirectory = configFile.getParentFile();
        if (configDirectory != null && !configDirectory.exists()) {
            configDirectory.mkdirs();
        }

        // write JSON out
        try (Writer writer = new FileWriter(configFile)) {
            GSON.toJson(newCfg, writer);
        } catch (IOException ioException) {
            FluxClient.LOG.error("Failed to save config", ioException);
        }
    }
}