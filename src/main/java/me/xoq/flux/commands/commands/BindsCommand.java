package me.xoq.flux.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import me.xoq.flux.commands.Command;
import me.xoq.flux.modules.Module;
import me.xoq.flux.modules.Modules;
import me.xoq.flux.utils.input.Keybind;
import me.xoq.flux.utils.misc.Utils;
import net.minecraft.command.CommandSource;

public class BindsCommand extends Command {
    public BindsCommand() {
        super("binds", "List all module keybinds");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(this::execute);
    }

    private int execute(CommandContext<CommandSource> context) {
        var all = Modules.getAll();
        var bound = all.stream()
                .filter(m -> !m.keybind.equals(Keybind.none()))
                .toList();

        info("§eModule keybinds (" + bound.size() + "):");
        for (Module module : bound) {
            String keyName = Utils.getKeyName(module.keybind.getValue());
            info("§7[ §6" + module.getTitle() + " §7] §e" + keyName + "§7");
        }
        return SINGLE_SUCCESS;
    }
}