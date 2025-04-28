package me.xoq.flux.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.xoq.flux.FluxClient;
import me.xoq.flux.commands.commands.*;
import net.minecraft.command.CommandSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Commands {
    private static final CommandDispatcher<CommandSource> DISPATCHER = new CommandDispatcher<>();
    private static final List<Command> COMMANDS = new ArrayList<>();

    public static void init() {
        register(new HelpCommand());
        register(new ExampleCommand());
        register(new BindCommand());
        register(new BindsCommand());
        register(new ListCommand());
        register(new SettingCommand());
        register(new ToggleCommand());
    }

    private static void register(Command command) {
        command.registerTo(DISPATCHER);
        COMMANDS.add(command);
    }

    public static CommandDispatcher<CommandSource> getDispatcher() {
        return DISPATCHER;
    }

    public static List<Command> getAll() {
        return Collections.unmodifiableList(COMMANDS);
    }

    public static void dispatch(String message) throws CommandSyntaxException {
        CommandSource src = FluxClient.mc.getNetworkHandler().getCommandSource();
        DISPATCHER.execute(message, src);    }
}