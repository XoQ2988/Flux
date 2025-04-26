package me.xoq.flux.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.xoq.flux.commands.Command;
import me.xoq.flux.commands.Commands;
import me.xoq.flux.modules.Module;
import me.xoq.flux.modules.Modules;
import net.minecraft.command.CommandSource;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help", "List all available commands");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            info("Available commands:");
            for (Command command : Commands.COMMANDS) {
                String line = "." + command.getName() + " – " + command.getDescription();
                info(line);
            }
            info("Available modules:");
            for (Module module : Modules.getAll()) {
                String line = "." + module.getName() + " – " + module.getDescription();
                info(line);
            }
            return SINGLE_SUCCESS;
        });
    }
}