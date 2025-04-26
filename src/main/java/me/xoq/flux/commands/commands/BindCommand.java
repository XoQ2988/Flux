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

public class BindCommand extends Command {
    private static final SuggestionProvider<CommandSource> MODULE_SUGGESTIONS =
            (CommandContext<CommandSource> ctx, SuggestionsBuilder builder) -> {
                for (Module mod : Modules.getAll()) {
                    builder.suggest(mod.getName());
                }
                return builder.buildFuture();
            };

    public BindCommand() {
        super("bind", "Binds a module a key.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder
            .then(argument("module", StringArgumentType.word())
                .suggests(MODULE_SUGGESTIONS)
                .executes(context -> {
                    String name  = StringArgumentType.getString(context, "module");
                    Module module = Modules.getByName(name);
                    if (module == null) {
                        info("Module not found: " + name);
                        return 0;
                    }
                    Modules.get().setModuleToBind(module);
                    return SINGLE_SUCCESS;
                })
            );
    }
}