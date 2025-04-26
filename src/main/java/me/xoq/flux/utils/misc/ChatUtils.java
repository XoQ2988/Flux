package me.xoq.flux.utils.misc;

import com.mojang.brigadier.StringReader;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

import static me.xoq.flux.FluxClient.mc;

public class ChatUtils {
    private static Text PREFIX;

    private ChatUtils() {}

    public static void init() {
        PREFIX = Text.empty()
                .setStyle(Style.EMPTY.withFormatting(Formatting.GRAY))
                .append("[")
                .append(Text.literal("Flux").setStyle(Style.EMPTY.withFormatting(Formatting.AQUA)))
                .append("] ");
    }

    public static void info(String message) {
        sendMsg(null, Formatting.GRAY, message);
    }

    public static void info(String prefix, String message) {
        sendMsg(prefix, Formatting.GRAY, message);
    }

    public static void sendMsg(@Nullable String prefixTitle, @Nullable Formatting color, String messageContent) {
        if (mc.world == null) return;
        MutableText message = Text.empty();
        message.append(PREFIX);
        if (prefixTitle != null) message.append(getCustomPrefix(prefixTitle));
        message.append(formatMsg(messageContent, color));

        mc.inGameHud.getChatHud().addMessage(message);
    }

    private static MutableText getCustomPrefix(String prefixTitle) {
        MutableText prefix = Text.empty();
        prefix.setStyle(prefix.getStyle().withFormatting(Formatting.GRAY));

        prefix.append("[");

        MutableText moduleTitle = Text.literal(prefixTitle);
        moduleTitle.setStyle(moduleTitle.getStyle().withFormatting(Formatting.DARK_AQUA));
        prefix.append(moduleTitle);

        prefix.append("] ");

        return prefix;
    }

    private static MutableText formatMsg(String message, Formatting defaultColor) {
        StringReader reader = new StringReader(message);
        MutableText resultText = Text.empty();
        Style currentStyle = Style.EMPTY.withFormatting(defaultColor);
        StringBuilder segment = new StringBuilder();

        while (reader.canRead()) {
            char c = reader.read();
            if (c == '§' && reader.canRead()) {
                // flush accumulated segment
                if (!segment.isEmpty()) {
                    resultText.append(Text.literal(segment.toString()).setStyle(currentStyle));
                    segment.setLength(0);
                }
                // parse formatting code
                char code = reader.read();
                Formatting fmt = Formatting.byCode(code);
                if (fmt != null) {
                    // update style: color resets previous formatting except bold/italic/etc.
                    currentStyle = Style.EMPTY.withFormatting(fmt);
                } else {
                    // unknown code: treat literally
                    segment.append('§').append(code);
                }
            } else {
                segment.append(c);
            }
        }

        // flush remainder
        if (!segment.isEmpty()) {
            resultText.append(Text.literal(segment.toString()).setStyle(currentStyle));
        }

        return resultText;
    }
}
