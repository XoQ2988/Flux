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

import java.util.Arrays;
import java.util.Optional;

public class SettingCommand extends Command {
    public SettingCommand() {
        super("setting", "View or change a module setting");
    }

    private static final SuggestionProvider<CommandSource> MODULE_SUGGESTER = (context, suggestionsBuilder) -> {
        Modules.getAll().stream().map(Module::getName).forEach(suggestionsBuilder::suggest);
        return suggestionsBuilder.buildFuture();
    };

    private static final SuggestionProvider<CommandSource> KEY_SUGGESTER = (context, suggestionsBuilder) -> {
        String moduleName = StringArgumentType.getString(context, "module");
        for (String fullKey : SettingsManager.getInstance().getRegisteredKeys()) {
            if (!fullKey.startsWith(moduleName + ".")) continue;
            Setting<?> setting = SettingsManager.getInstance().get(fullKey);
            suggestionsBuilder.suggest(setting.getName());
        }
        return suggestionsBuilder.buildFuture();
    };

    private static final SuggestionProvider<CommandSource> VALUE_SUGGESTER = (context, suggestionsBuilder) -> {
        String moduleName = context.getArgument("module", String.class);
        String settingName = context.getArgument("setting", String.class);
        Optional<Setting<?>> optionalSetting = findSetting(moduleName, settingName);
        if (optionalSetting.isEmpty()) return suggestionsBuilder.buildFuture();

        Setting<?> setting = optionalSetting.get();
        Object settingDefault = setting.getDefault();

        if (settingDefault instanceof Boolean) {
            suggestionsBuilder.suggest("true").suggest("false");

        } else if (setting instanceof ColorSetting) {
            int color = (Integer) settingDefault;
            suggestionsBuilder.suggest(String.format("0x%08X", color)).suggest(String.format("#%08X", color));

        } else if (settingDefault instanceof Integer) {
            suggestionsBuilder.suggest(settingDefault.toString());

        } else if (setting instanceof EnumSetting<?> es) {
            Arrays.stream(es.getEnumType().getEnumConstants()).map(Enum::name).forEach(suggestionsBuilder::suggest);

        } else if (setting instanceof StringSetting) {
            suggestionsBuilder.suggest((String) settingDefault);
        }

        return suggestionsBuilder.buildFuture();
    };

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder
                .then(argument("module", StringArgumentType.word())
                        .suggests(MODULE_SUGGESTER)
                        .then(literal("reset")
                                .executes(this::resetAll)
                        )
                        .executes(this::listAll)
                        .then(argument("setting", StringArgumentType.word())
                                .suggests(KEY_SUGGESTER)
                                .then(argument("value", StringArgumentType.greedyString())
                                        .suggests(VALUE_SUGGESTER)
                                        .executes(this::setOne)
                                )
                                .executes(this::viewOne)
                        )
                );
    }

    private static Optional<Setting<?>> findSetting(String moduleName, String name) {
        String prefix = moduleName + ".";

        for (String fullKey : SettingsManager.getInstance().getRegisteredKeys()) {
            if (!fullKey.startsWith(prefix)) continue;

            Setting<?> setting = SettingsManager.getInstance().get(fullKey);

            if (setting.getName().equals(name)) {
                return Optional.of(setting);
            }
        }
        return Optional.empty();
    }

    private int listAll(CommandContext<CommandSource> context) {
        String moduleName = StringArgumentType.getString(context, "module");

        for (String fullKey : SettingsManager.getInstance().getRegisteredKeys()) {
            if (!fullKey.startsWith(moduleName + ".")) continue;

            Setting<?> setting = SettingsManager.getInstance().get(fullKey);

            info(String.format("§6%s§7: %s §7[§e%s§7]",
                    setting.getTitle(), setting.getDescription(), setting.getValue()));
        }
        return SINGLE_SUCCESS;
    }

    private int resetAll(CommandContext<CommandSource> context) {
        String moduleName = StringArgumentType.getString(context, "module");
        SettingsManager settingsManager = SettingsManager.getInstance();

        settingsManager.getRegisteredKeys().stream()
                .filter(k -> k.startsWith(moduleName + "."))
                .map(settingsManager::get)
                .forEach(Setting::reset);

        info("Reset all settings for §6" + Modules.getByName(moduleName).getTitle() + "§7 to defaults.");

        return SINGLE_SUCCESS;
    }

    private int viewOne(CommandContext<CommandSource> ctx) {
        String moduleName = StringArgumentType.getString(ctx, "module");
        String settingName = StringArgumentType.getString(ctx, "setting");
        var optionalSetting = findSetting(moduleName, settingName);

        if (optionalSetting.isEmpty()) {
            warn("Unknown setting: " + settingName);

        } else {
            Setting<?> setting = optionalSetting.get();
            info(String.format("§6%s§7 is set to §e%s§r", setting.getTitle(), setting.getValue()));
        }

        return SINGLE_SUCCESS;
    }

    @SuppressWarnings("SameReturnValue")
    private int setOne(CommandContext<CommandSource> context) {
        String moduleName = StringArgumentType.getString(context, "module");
        String settingName = StringArgumentType.getString(context, "setting");
        String input = StringArgumentType.getString(context, "value").trim();

        var optionalSetting = findSetting(moduleName, settingName);
        if (optionalSetting.isEmpty()) {
            warn("Unknown setting: " + settingName);
            return SINGLE_SUCCESS;
        }

        Setting<?> rawSetting = optionalSetting.get();
        Object rawSettingDefault = rawSetting.getDefault();
        Object parsedValue;

        try {
            if (rawSettingDefault instanceof Boolean) {
                parsedValue = Boolean.parseBoolean(input);
            } else if (rawSettingDefault instanceof Integer) {
                parsedValue = Integer.parseInt(input);
            } else if (rawSettingDefault instanceof Double || rawSettingDefault instanceof Float) {
                parsedValue = Double.parseDouble(input);
            } else if (rawSetting instanceof EnumSetting<?> enumSetting) {
                parsedValue = Enum.valueOf(enumSetting.getEnumType(), input.toUpperCase());
            } else {
                parsedValue = input;     // default to string
            }

        } catch (Exception e) {
            warn("Invalid value for " + settingName + ": " + input);
            return SINGLE_SUCCESS;
        }

        //noinspection unchecked
        ((Setting<Object>) rawSetting).setValue(parsedValue);

        info(String.format("Set §6%s§7 to §e%s§r", rawSetting.getTitle(), rawSetting.getValue()));

        return SINGLE_SUCCESS;
    }

    private int resetOne(CommandContext<CommandSource> context) {
        String moduleName = StringArgumentType.getString(context, "module");
        String settingName = StringArgumentType.getString(context, "setting");
        var optionalSetting = findSetting(moduleName, settingName);

        if (optionalSetting.isEmpty()) {
            warn("Unknown setting: " + settingName);

        } else {
            //noinspection unchecked
            Setting<Object> setting = (Setting<Object>) optionalSetting.get();
            setting.reset();
            info(String.format("Reset §6%s§7 to default §e%s§r",
                    setting.getTitle(), setting.getDefault()));
        }

        return SINGLE_SUCCESS;
    }
}