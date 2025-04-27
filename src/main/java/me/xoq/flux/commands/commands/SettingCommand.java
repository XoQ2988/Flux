package me.xoq.flux.commands.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import me.xoq.flux.commands.Command;
import me.xoq.flux.modules.Module;
import me.xoq.flux.modules.Modules;
import me.xoq.flux.settings.*;
import net.minecraft.command.CommandSource;

import java.util.Optional;

public class SettingCommand extends Command {
    private static final SuggestionProvider<CommandSource> MODULE_SUGGESTER = (context, sb) -> {
        for (Module module : Modules.getAll()) {
            sb.suggest(module.getName());
        }
        return sb.buildFuture();
    };

    private static final SuggestionProvider<CommandSource> KEY_SUGGESTER = (context, sb) -> {
        String module = StringArgumentType.getString(context, "module");
        SettingsManager mgr = SettingsManager.getInstance();
        for (String key : mgr.getRegisteredKeys()) {
            if (!key.startsWith(module + ".")) continue;
            Setting<?> s = mgr.get(key);
            sb.suggest(s.getName());
        }
        return sb.buildFuture();
    };

    private static final SuggestionProvider<CommandSource> VALUE_SUGGESTER = (context, sb) -> {
        String module = context.getArgument("module", String.class);
        String key    = context.getArgument("setting", String.class);
        Optional<Setting<?>> opt = findSetting(module, key);
        if (opt.isEmpty()) return sb.buildFuture();
        Setting<?> s = opt.get();
        Object def = s.getDefault();
        if (def instanceof Boolean) {
            sb.suggest("true");
            sb.suggest("false");
        } else if (s instanceof ColorSetting) {
            int col = (Integer) def;
            sb.suggest(String.format("0x%08X", col));
            sb.suggest(String.format("#%08X", col));
        } else if (def instanceof Integer) {
            sb.suggest(def.toString());
        } else if (s instanceof EnumSetting<?> es) {
            for (Enum<?> constant : es.getEnumType().getEnumConstants()) {
                sb.suggest(constant.name());
            }
        } else if (s instanceof StringSetting) {
            sb.suggest((String) def);
        }
        return sb.buildFuture();
    };


    public SettingCommand() {
        super("setting", "View or change a module setting");
    }


    @Override
    public void build(LiteralArgumentBuilder<CommandSource> root) {
        root
                .then(argument("module", StringArgumentType.word())
                        .suggests(MODULE_SUGGESTER)
                        // reset all settings for module
                        .then(literal("reset")
                                .executes(this::resetModuleSettings)
                        )
                        .executes(this::listModuleSettings)
                        .then(argument("setting", StringArgumentType.word())
                                .suggests(KEY_SUGGESTER)
                                .executes(this::viewSetting)
                                // reset subcommand
                                .then(literal("reset")
                                        .executes(this::resetSetting)
                                )
                                .then(argument("value", StringArgumentType.greedyString())
                                        .suggests(VALUE_SUGGESTER)
                                        .executes(this::setSetting)
                                )
                        )
                );
    }

    private static Optional<Setting<?>> findSetting(String module, String name) {
        String prefix = module + ".";
        for (String key : SettingsManager.getInstance().getRegisteredKeys()) {
            if (!key.startsWith(prefix)) continue;
            Setting<?> s = SettingsManager.getInstance().get(key);
            if (s.getName().equals(name)) return Optional.of(s);
        }
        return Optional.empty();
    }

    private int resetModuleSettings(CommandContext<CommandSource> ctx) {
        String module = StringArgumentType.getString(ctx, "module");
        SettingsManager mgr = SettingsManager.getInstance();
        mgr.getRegisteredKeys().stream()
                .filter(k -> k.startsWith(module + "."))
                .map(mgr::get)
                .forEach(Setting::reset);
        info("Reset all settings for module §6" + Modules.getByName(module).getTitle() + "§r§7 to defaults.");
        return SINGLE_SUCCESS;
    }

    private int listModuleSettings(CommandContext<CommandSource> context) {
        String module = context.getArgument("module", String.class);
        SettingsManager mgr = SettingsManager.getInstance();
        mgr.getRegisteredKeys().stream()
                .filter(k -> k.startsWith(module + "."))
                .map(mgr::get)
                .forEach(s -> {

                    String line = "§6" + s.getTitle() +
                            "§r§7: " + s.getDescription() +
                            " §7[Current: §e" + s.getValue() +
                            "§7]§r";

                    info(line);
                });
        return SINGLE_SUCCESS;
    }

    @SuppressWarnings("SameReturnValue")
    private int viewSetting(CommandContext<CommandSource> context) {
        String module = StringArgumentType.getString(context, "module");
        String key    = StringArgumentType.getString(context, "setting");
        Optional<Setting<?>> opt = findSetting(module, key);
        if (opt.isEmpty()) {
            warn("Unknown setting: " + key);
            return SINGLE_SUCCESS;
        }
        Setting<?> s = opt.get();
        info("§6" + s.getTitle() + "§r§7 is set to §e" + s.getValue() + "§r.");
        return SINGLE_SUCCESS;
    }

    @SuppressWarnings("SameReturnValue")
    private int setSetting(CommandContext<CommandSource> context) {
        String module = StringArgumentType.getString(context, "module");
        String key    = StringArgumentType.getString(context, "setting");
        String input  = StringArgumentType.getString(context, "value").trim();

        Optional<Setting<?>> opt = findSetting(module, key);
        if (opt.isEmpty()) {
            info("§cUnknown setting: §6" + key + "§r");
            return SINGLE_SUCCESS;
        }
        Setting<?> raw = opt.get();
        Object parsed;
        Object def = raw.getDefault();

        try {
            if (def instanceof Boolean) {
                parsed = Boolean.parseBoolean(input);
            } else if (def instanceof Integer) {
                parsed = Integer.parseInt(input);
            } else if (def instanceof Double || def instanceof Float) {
                parsed = Double.parseDouble(input);
            } else if (raw instanceof EnumSetting<?> es) {
                parsed = Enum.valueOf(es.getEnumType(), input.toUpperCase());
            } else {
                parsed = input;
            }
        } catch (Exception e) {
            info("Invalid value for §6" + key + "§c: §e" + input + "§r");
            return SINGLE_SUCCESS;
        }
        @SuppressWarnings("unchecked")
        Setting<Object> s = (Setting<Object>) raw;
        s.setValue(parsed);

        info("Set §6" + s.getTitle() + "§r§7 to §e" + s.getValue() + "§r.");
        return SINGLE_SUCCESS;
    }

    @SuppressWarnings("SameReturnValue")
    private int resetSetting(CommandContext<CommandSource> context) {
        String module = context.getArgument("module", String.class);
        String name   = context.getArgument("setting", String.class);
        Optional<Setting<?>> opt = findSetting(module, name);
        if (opt.isEmpty()) {
            warn("Unknown setting: §6" + name + "§r");
            return SINGLE_SUCCESS;
        }

        Setting<?> s = opt.get();
        s.reset();

        info("§aReset §6" + s.getTitle() + "§a to its default §e" + s.getDefault() + "§r.");
        return SINGLE_SUCCESS;
    }
}