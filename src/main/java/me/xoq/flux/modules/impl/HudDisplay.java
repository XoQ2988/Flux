package me.xoq.flux.modules.impl;

import me.xoq.flux.events.EventHandler;
import me.xoq.flux.events.render.Render2DEvent;
import me.xoq.flux.hud.HudEntry;
import me.xoq.flux.hud.HudManager;
import me.xoq.flux.modules.Module;
import me.xoq.flux.settings.BoolSetting;
import me.xoq.flux.settings.EnumSetting;
import me.xoq.flux.settings.IntSetting;
import me.xoq.flux.settings.Setting;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static me.xoq.flux.FluxClient.mc;

public class HudDisplay extends Module {
    public HudDisplay() {
        super("hud-display", "Render all registered HUD entries");
        for (HudEntry e : HudManager.get().getAllEntries()) {
            String id = e.getId();
            Setting<Boolean> s = settings.add(
                    new BoolSetting.Builder()
                            .name(id)
                            .description("Show “" + id + "” HUD line")
                            .defaultValue(true)
                            .build()
            );
            toggles.put(id, s);
        }
    }

    private final Setting<Anchor> anchor = settings.add(
            new EnumSetting.Builder<Anchor>()
                    .name("anchor")
                    .description("Which corner to draw the HUD from")
                    .defaultValue(Anchor.TOP_LEFT)
                    .build()
    );

    private final Setting<Integer> padding = settings.add(
            new IntSetting.Builder()
                    .name("padding")
                    .description("Distance in pixels from the screen edge")
                    .defaultValue(2)
                    .range(0, 50)
                    .build()
    );

    public final Map<String, Setting<Boolean>> toggles = new LinkedHashMap<>();

    @EventHandler
    private void onRender2D(Render2DEvent event) {
        if (mc.options.hudHidden || !isEnabled()) return;

        List<HudEntry> entries = new ArrayList<>(HudManager.get().getEnabledEntries());
        entries.removeIf(entry -> !isVisible(entry.getId()));
        if (entries.isEmpty()) return;

        DrawContext ctx = event.getContext();
        TextRenderer font = mc.textRenderer;
        int lineH = font.fontHeight + 2;

        int sw = mc.getWindow().getScaledWidth();
        int sh = mc.getWindow().getScaledHeight();
        int pad = padding.getValue();

        Anchor a = anchor.getValue();
        int baseX = switch (a) {
            case TOP_LEFT, BOTTOM_LEFT -> pad;
            case TOP_RIGHT, BOTTOM_RIGHT -> sw - pad;
        };
        int baseY = switch (a) {
            case TOP_LEFT, TOP_RIGHT -> pad;
            case BOTTOM_LEFT, BOTTOM_RIGHT -> sh - pad - lineH * entries.size();
        };

        for (int i = 0; i < entries.size(); i++) {
            HudEntry entry = entries.get(i);
            Text txt = entry.getText();
            int w = font.getWidth(txt);

            int x = switch (a) {
                case TOP_LEFT, BOTTOM_LEFT -> baseX;
                case TOP_RIGHT, BOTTOM_RIGHT -> baseX - w;
            };
            int y = baseY + lineH * i;

            ctx.drawTextWithShadow(font, txt, x, y, entry.getColor());
        }
    }

    public boolean isVisible(String id) {
        Setting<Boolean> s = toggles.get(id);
        return s == null || s.getValue();
    }

    public enum Anchor {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
    }
}
