package me.xoq.flux.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.xoq.flux.commands.Command;
import net.minecraft.command.CommandSource;

public class TestCommand extends Command {
    public TestCommand() {
        super("test", "Command meant for testing.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder
                .then(literal("info").executes(context -> {
                    info("Message");
                    return SINGLE_SUCCESS;
                }))
                .then(literal("warn").executes(context -> {
                    warn("Message");
                    return SINGLE_SUCCESS;
                })).then(literal("error").executes(context -> {
                    error("Message");
                    return SINGLE_SUCCESS;
                }));
    }
}