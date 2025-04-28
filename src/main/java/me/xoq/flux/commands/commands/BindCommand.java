package me.xoq.flux.commands.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import me.xoq.flux.commands.Command;
import me.xoq.flux.modules.Module;
import me.xoq.flux.modules.Modules;
import net.minecraft.command.CommandSource;

public class BindCommand extends Command {
    public BindCommand() {
        super("bind", "Binds a module a key");
    }

    private static final SuggestionProvider<CommandSource> MODULE_SUGGESTIONS = (ctx, builder) -> {
        Modules.getAll().forEach(m -> builder.suggest(m.getName()));
        return builder.buildFuture();
    };

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder
                .then(argument("module", StringArgumentType.word())
                        .suggests(MODULE_SUGGESTIONS)
                        .executes(this::execute)
                );
    }

    private int execute(CommandContext<CommandSource> ctx) {
        String name = StringArgumentType.getString(ctx, "module");
        Module module = Modules.getByName(name);
        if (module == null) {
            info("§cModule not found: §6" + name + "§r");
        } else {
            Modules.get().setModuleToBind(module);
            info("§aPress a key to bind §6" + module.getTitle() + "§a, or §cESC §ato clear§r");
        }
        return SINGLE_SUCCESS;
    }
}