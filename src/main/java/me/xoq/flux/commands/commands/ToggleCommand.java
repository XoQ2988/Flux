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
        builder.then(
            argument("module", StringArgumentType.word()).suggests(MODULE_SUGGESTIONS)
                .then(literal("on")
                    .executes(context -> {
                        String name  = StringArgumentType.getString(context, "module");
                        Module module = Modules.getByName(name);
                        if (module == null) {
                            info("Module not found: " + name);
                            return 0;
                        }
                        if (!module.isEnabled()) module.toggle();
                        return SINGLE_SUCCESS;
                    }))
                .then(literal("off")
                    .executes(context -> {
                        String name  = StringArgumentType.getString(context, "module");
                        Module module = Modules.getByName(name);
                        if (module == null) {
                            info("Module not found: " + name);
                            return 0;
                        }
                        if (module.isEnabled()) module.toggle();
                        return SINGLE_SUCCESS;
                    })
                )
        );
    }
}