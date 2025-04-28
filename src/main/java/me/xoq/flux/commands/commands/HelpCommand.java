package me.xoq.flux.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
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
        builder.executes(this::execute);
    }

    private int execute(CommandContext<CommandSource> context) {
        info("§eAvailable commands:");
        for (Command cmd : Commands.getAll()) {
            info("§7. §6" + cmd.getTitle() + " §7– §f" + cmd.getDescription());
        }
        info("§eAvailable modules:");
        for (Module mod : Modules.getAll()) {
            info("§7. §6" + mod.getTitle() + " §7– §f" + mod.getDescription());
        }
        return SINGLE_SUCCESS;
    }
}