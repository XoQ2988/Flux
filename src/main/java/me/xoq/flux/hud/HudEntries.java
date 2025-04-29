package me.xoq.flux.hud;

import net.minecraft.text.Text;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static me.xoq.flux.FluxClient.mc;

public final class HudEntries {
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static final HudEntry FPS = new HudEntry(
            "fps",
            () -> Text.literal("FPS: " + mc.getCurrentFps()),
            0xFFAAAAAA,
            10
    );

    public static final HudEntry MEMORY = new HudEntry(
            "memory",
            () -> {
                Runtime rt = Runtime.getRuntime();
                long used = (rt.totalMemory() - rt.freeMemory()) / (1024 * 1024);
                long max  = rt.maxMemory() / (1024 * 1024);
                return Text.literal("Mem: " + used + "MB/" + max + "MB");
            },
            0xFFAAAAAA,
            20
    );

    public static final HudEntry ENTITIES = new HudEntry(
            "entities",
            () -> {
                if (mc.world == null) return Text.literal("Entities: N/A");
                int cnt = (int) mc.world.getEntities().spliterator().getExactSizeIfKnown();
                return Text.literal("Entities: " + cnt);
            },
            0xFFAAAAAA,
            30
    );

    private static final HudEntry PING = new HudEntry(
            "ping",
            () -> {
                if (mc.getNetworkHandler() == null || mc.player == null)
                    return Text.literal("Ping: N/A");
                var e = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
                return Text.literal("Ping: " + (e != null ? e.getLatency() + "ms" : "N/A"));
            },
            0xFFAAAAAA,
            40
    );

    private static final HudEntry COORDS = new HudEntry(
            "coords",
            () -> {
                if (mc.player == null) return Text.literal("XYZ: ?, ?, ?");
                double x = mc.player.getX(), y = mc.player.getY(), z = mc.player.getZ();
                return Text.literal(String.format("XYZ: %.1f, %.1f, %.1f", x, y, z));
            },
            0xFFAAAAAA,
            50
    );

    private static final HudEntry NETHER = new HudEntry(
            "nether",
            () -> {
                if (mc.player == null) return Text.literal("Nether: ?, ?");
                double x = mc.player.getX()/8, z = mc.player.getZ()/8;
                return Text.literal(String.format("Nether: %.1f, %.1f", x, z));
            },
            0xFFAAAAAA,
            51
    );

    private static final HudEntry FACING = new HudEntry(
            "facing",
            () -> {
                if (mc.player == null) return Text.literal("Facing: ?");
                String face = mc.player.getHorizontalFacing().asString().toUpperCase();
                return Text.literal("Facing: " + face);
            },
            0xFFAAAAAA, 60
    );

    private static final HudEntry GAME_TIME = new HudEntry(
            "time-game",
            () -> {
                if (mc.world == null) return Text.literal("Time: ?");
                long dayTime = mc.world.getTimeOfDay() % 24000;
                long day = mc.world.getTimeOfDay() / 24000 + 1;
                int hour = (int)((dayTime / 1000 + 6) % 24);
                int min  = (int)((dayTime % 1000) * 60 / 1000);
                return Text.literal(String.format("Day %d, %02d:%02d", day, hour, min));
            },
            0xFFAAAAAA, 70
    );

    private static final HudEntry TIME_LOCAL = new HudEntry(
            "time-local",
            () -> Text.literal("Local: " + LocalTime.now().format(TIME_FMT)),
            0xFFAAAAAA, 80
    );

    public static void init() {
        HudManager m = HudManager.get();
        m.register(FPS);
        m.register(MEMORY);
        m.register(ENTITIES);
        m.register(PING);
        m.register(COORDS);
        m.register(NETHER);
        m.register(FACING);
        m.register(GAME_TIME);
        m.register(TIME_LOCAL);
    }
}