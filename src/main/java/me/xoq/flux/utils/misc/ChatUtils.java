package me.xoq.flux.utils.misc;

import com.mojang.brigadier.StringReader;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

import static me.xoq.flux.FluxClient.mc;

public class ChatUtils {
    private static final MutableText PREFIX = Text.literal("[")
            .setStyle(Style.EMPTY.withFormatting(Formatting.GRAY))
            .append(Text.literal("Flux")
                    .setStyle(Style.EMPTY.withFormatting(Formatting.AQUA)))
            .append(Text.literal("] ")
                    .setStyle(Style.EMPTY.withFormatting(Formatting.GRAY)));

    private ChatUtils() { }

    public static void info(String message) {
        send(null, Formatting.GRAY, message);
    }

    public static void info(String prefix, String message) {
        send(prefix, Formatting.GRAY, message);
    }

    public static void send(@Nullable String moduleTitle, Formatting color, String content) {
        if (mc.world == null) return;

        MutableText msg = PREFIX.copy();

        if (moduleTitle != null) {
            msg.append(Text.literal("[")
                            .setStyle(Style.EMPTY.withFormatting(Formatting.GRAY)))
                    .append(Text.literal(moduleTitle)
                            .setStyle(Style.EMPTY.withFormatting(Formatting.DARK_AQUA)))
                    .append(Text.literal("] ")
                            .setStyle(Style.EMPTY.withFormatting(Formatting.GRAY)));
        }

        msg.append(parseFormatting(content, color));
        mc.inGameHud.getChatHud().addMessage(msg);
    }

    private static MutableText parseFormatting(String content, Formatting defaultColor) {
        StringReader reader = new StringReader(content);
        MutableText result = Text.empty();
        Style style = Style.EMPTY.withFormatting(defaultColor);
        StringBuilder buf = new StringBuilder();

        while (reader.canRead()) {
            char c = reader.read();
            if (c == '§' && reader.canRead()) {
                // flush buffered text
                if (!buf.isEmpty()) {
                    result.append(Text.literal(buf.toString()).setStyle(style));
                    buf.setLength(0);
                }
                // apply new code
                char code = reader.read();
                Formatting fmt = Formatting.byCode(code);
                if (fmt != null) {
                    style = Style.EMPTY.withFormatting(fmt);
                } else {
                    // unknown code, treat literally
                    buf.append('§').append(code);
                }
            } else {
                buf.append(c);
            }
        }
        // flush remainder
        if (!buf.isEmpty()) {
            result.append(Text.literal(buf.toString()).setStyle(style));
        }
        return result;
    }
}