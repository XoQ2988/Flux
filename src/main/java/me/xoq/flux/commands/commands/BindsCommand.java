package me.xoq.flux.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.xoq.flux.commands.Command;
import me.xoq.flux.modules.Module;
import me.xoq.flux.modules.Modules;
import me.xoq.flux.utils.misc.Utils;
import net.minecraft.command.CommandSource;

public class BindsCommand extends Command {
    public BindsCommand() {
        super("binds", "List all module keybinds");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            info("Module keybinds (" + Modules.getAll().size() + "):");
            for (Module module : Modules.getAll()) {
                info(" - " + module.getTitle() + ": " + Utils.getKeyName(module.keybind.getValue()));
            }
            return SINGLE_SUCCESS;
        });
    }
}