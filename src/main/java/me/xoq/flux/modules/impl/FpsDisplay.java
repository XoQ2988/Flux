package me.xoq.flux.modules.impl;

import me.xoq.flux.events.EventHandler;
import me.xoq.flux.events.render.Render2DEvent;
import me.xoq.flux.modules.Module;
import me.xoq.flux.settings.ColorSetting;
import me.xoq.flux.settings.IntSetting;
import me.xoq.flux.settings.Setting;
import me.xoq.flux.settings.StringSetting;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import static me.xoq.flux.FluxClient.mc;

public class FpsDisplay extends Module {
    public FpsDisplay() {
        super("fps-display", "Display the game's FPS");
        setEnabled(true);
    }

    private final Setting<String> hudText = settings.add(
            new StringSetting.Builder()
                    .name("text")
                    .description("The text to display in the HUD.")
                    .defaultValue("FPS: {FPS}")
                    .build()
    );

    private final Setting<Integer> posX = settings.add(
            new IntSetting.Builder()
                    .name("posX")
                    .description("Horizontal position of the HUD text.")
                    .defaultValue(2)
                    .range(0, 9999)
                    .build()
    );

    private final Setting<Integer> posY = settings.add(
            new IntSetting.Builder()
                    .name("posY")
                    .description("Vertical position of the HUD text.")
                    .defaultValue(2)
                    .range(0, 9999)
                    .build()
    );

    private final Setting<Integer> color = settings.add(
            new ColorSetting.Builder()
                    .name("color")
                    .description("ARGB color of the HUD text.")
                    .defaultValue(0xFFFFFFFF)
                    .build()
    );

    @EventHandler
    public void onRender2D(Render2DEvent event) {
        if (mc.options.hudHidden || !isEnabled()) return;

        DrawContext ctx = event.getContext();
        TextRenderer font = mc.textRenderer;
        String text = hudText.getValue().replace("{FPS}", String.valueOf(mc.getCurrentFps()));

        var window = mc.getWindow();
        int maxX = window.getScaledWidth()  - font.getWidth(text);
        int maxY = window.getScaledHeight() - font.fontHeight;

        int x = MathHelper.clamp(posX.getValue(), 0, maxX);
        int y = MathHelper.clamp(posY.getValue(), 0, maxY);

        ctx.drawTextWithShadow(font, Text.literal(text), x, y, color.getValue());
    }
}