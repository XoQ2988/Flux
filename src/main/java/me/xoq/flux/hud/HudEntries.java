package me.xoq.flux.hud;

import net.minecraft.text.Text;

import static me.xoq.flux.FluxClient.mc;

public final class HudEntries {
    public static final HudEntry FPS = new HudEntry(
            "fps",
            () -> Text.literal("FPS: " + mc.getCurrentFps()),
            0xFFAAAAAA,
            10
    );

    public static final HudEntry PING = new HudEntry(
            "ping",
            () -> {
                if (mc.getNetworkHandler() == null || mc.player == null)
                    return Text.literal("Ping: N/A");
                var e = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
                return Text.literal("Ping: " + (e != null ? e.getLatency() + "ms" : "N/A"));
            },
            0xFFAAAAAA,
            20
    );

    public static final HudEntry COORDS = new HudEntry(
            "coords",
            () -> {
                if (mc.player == null) return Text.literal("XYZ: ?, ?, ?");
                double x = mc.player.getX(), y = mc.player.getY(), z = mc.player.getZ();
                return Text.literal(String.format("XYZ: %.1f, %.1f, %.1f", x, y, z));
            },
            0xFFAAAAAA,
            30
    );

    public static final HudEntry NETHER = new HudEntry(
            "nether",
            () -> {
                if (mc.player == null) return Text.literal("Nether: ?, ?");
                double x = mc.player.getX()/8, z = mc.player.getZ()/8;
                return Text.literal(String.format("Nether: %.1f, %.1f", x, z));
            },
            0xFFAAAAAA,
            31
    );

    public static void init() {
        HudManager m = HudManager.get();
        m.register(FPS);
        m.register(PING);
        m.register(COORDS);
        m.register(NETHER);
    }
}