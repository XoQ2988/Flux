package me.xoq.flux.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.xoq.flux.commands.commands.*;
import net.minecraft.command.CommandSource;

import java.util.ArrayList;
import java.util.List;

import static me.xoq.flux.FluxClient.mc;

public class Commands {
    public static final CommandDispatcher<CommandSource> DISPATCHER = new CommandDispatcher<>();
    public static final List<Command> COMMANDS = new ArrayList<>();

    public static void init() {
        add(new HelpCommand());
        // add(new TestCommand());
        add(new BindCommand());
        add(new BindsCommand());
        add(new ListCommand());
        add(new SettingCommand());
        add(new ToggleCommand());
    }

    public static void add(Command command) {
        command.registerTo(DISPATCHER);
        COMMANDS.add(command);
    }

    public static void dispatch(String message) throws CommandSyntaxException {
        DISPATCHER.execute(message, mc.getNetworkHandler().getCommandSource());
    }
}
