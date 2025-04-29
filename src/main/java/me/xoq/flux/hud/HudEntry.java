package me.xoq.flux.hud;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.function.Supplier;

// single line (or block) of HUD text
public class HudEntry {
    private final String id;
    private final Supplier<Text> supplier;
    private boolean enabled = true;
    private int color;
    private int priority;

    public HudEntry(String id,
                    Supplier<Text> supplier,
                    int color,
                    int priority) {
        this.id = id;
        this.supplier = supplier;
        this.color = color;
        this.priority = priority;
    }

    public String getId() { return id; }
    public Text getText() { return supplier.get(); }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean e) { enabled = e; }
    public int getColor() { return color; }
    public void setColor(int c) { color = c; }
    public int getPriority() { return priority; }
    public void setPriority(int p) { priority = p; }
}