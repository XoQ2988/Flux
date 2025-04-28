package me.xoq.flux;

import me.xoq.flux.commands.Commands;
import me.xoq.flux.events.EventBus;
import me.xoq.flux.modules.Modules;
import me.xoq.flux.utils.config.ConfigManager;
import me.xoq.flux.utils.misc.ChatUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class FluxClient implements ClientModInitializer {
	public static final String MOD_ID = "flux";
	public static final ModMetadata MOD_META;
	public static final String NAME;

	public static MinecraftClient mc;
	public static final EventBus EVENT_BUS = new EventBus();
	public static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve(MOD_ID + ".json");
	public static final Logger LOG;

	static {
		MOD_META = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow().getMetadata();

		NAME = MOD_META.getName();
		LOG = LoggerFactory.getLogger(NAME);
	}

	@Override
	public void onInitializeClient() {
		mc = MinecraftClient.getInstance();

		Modules.init();
		Commands.init();
		ConfigManager.load();
		ChatUtils.init();

		// Shutdown hook to save
		Runtime.getRuntime().addShutdownHook(new Thread(
				ConfigManager::save
		));

		LOG.info("{} initialized", NAME);
	}
}