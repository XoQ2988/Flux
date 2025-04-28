package me.xoq.flux.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import me.xoq.flux.utils.misc.ChatUtils;
import me.xoq.flux.utils.misc.Utils;
import net.minecraft.command.CommandSource;

@SuppressWarnings("unused")
public abstract class Command {
    protected static final int SINGLE_SUCCESS = com.mojang.brigadier.Command.SINGLE_SUCCESS;

    private final String name;
    private final String title;
    private final String description;

    public Command(String name, String description) {
        this.name = name;
        this.title = Utils.nameToTitle(name);
        this.description = description;
    }

    protected static <T> RequiredArgumentBuilder<CommandSource, T> argument(String name, ArgumentType<T> argumentType) {
        return RequiredArgumentBuilder.argument(name, argumentType);
    }

    protected static LiteralArgumentBuilder<CommandSource> literal( String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    public final void registerTo(CommandDispatcher<CommandSource> commandDispatcher) {
        register(commandDispatcher, name);
    }

    public void register(CommandDispatcher<CommandSource> commandDispatcher, String alias) {
        LiteralArgumentBuilder<CommandSource> builder = LiteralArgumentBuilder.literal(alias);
        build(builder);
        commandDispatcher.register(builder);
    }

    public abstract void build(LiteralArgumentBuilder<CommandSource> builder);

    public void info(String message) { ChatUtils.info(title, message); }
    public void warn(String message) { ChatUtils.info(title, "§e" + message); }
    public void error(String message) { ChatUtils.info(title, "§l§c" + message); }

    public String getName() { return name; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
}