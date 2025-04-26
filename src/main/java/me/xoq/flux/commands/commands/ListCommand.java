package me.xoq.flux.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.xoq.flux.commands.Command;
import me.xoq.flux.modules.Module;
import me.xoq.flux.modules.Modules;
import net.minecraft.command.CommandSource;

import java.util.List;

public class ListCommand extends Command {
    public ListCommand() {
        super("list", "List all modules and their states.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            List<Module> all = Modules.getAll();
            info("Modules (" + all.size() + "):");
            for (Module module : all) {
                String status = module.isEnabled() ? "ON" : "OFF";
                info(" - " + module.getName() + ": " + status);
            }
            return SINGLE_SUCCESS;
        });
    }
}
