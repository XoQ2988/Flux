package me.xoq.flux.commands.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import me.xoq.flux.commands.Command;
import me.xoq.flux.modules.Module;
import me.xoq.flux.modules.Modules;
import net.minecraft.command.CommandSource;

public class ToggleCommand extends Command {
    private static final SuggestionProvider<CommandSource> MODULE_SUGGESTIONS =
            (CommandContext<CommandSource> ctx, SuggestionsBuilder builder) -> {
                for (Module mod : Modules.getAll()) {
                    builder.suggest(mod.getName());
                }
                return builder.buildFuture();
            };

    public ToggleCommand() {
        super("toggle", "Enable or disable a module");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder
                .then(argument("module", StringArgumentType.word())
                        .suggests(MODULE_SUGGESTIONS)
                        .executes(this::toggleModule)
                        .then(literal("on") .executes(context -> setModuleState(context, true)))
                        .then(literal("off").executes(context -> setModuleState(context, false)))
        );
    }

    private int toggleModule(CommandContext<CommandSource> context) {
        String moduleName = StringArgumentType.getString(context, "module");
        Module module = Modules.getByName(moduleName);
        if (module == null) {
            info("§cModule not found: §6" + moduleName + "§r");
        } else {
            module.toggle();
        }
        return SINGLE_SUCCESS;
    }

    private int setModuleState(CommandContext<CommandSource> context, boolean enable) {
        String moduleName = StringArgumentType.getString(context, "module");
        Module module = Modules.getByName(moduleName);

        if (module == null) {
            info("§cModule not found: §6" + moduleName + "§r");
        } else if (module.isEnabled() == enable) {
            info("§e" + module.getTitle() +
                    " is already " + (enable ? "§2ON§e" : "§4OFF") + "§r");
        } else {
            module.toggle();
        }
        return SINGLE_SUCCESS;
    }
}