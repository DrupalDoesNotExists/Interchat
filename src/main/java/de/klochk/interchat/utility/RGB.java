package de.klochk.interchat.utility;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.awt.*;

/**
 * RGB Utility
 */
public class RGB {

    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    /**
     * Colorize string with RGB and chat colors
     * @param string Source string
     * @return Colorful string
     */
    public static Component colorize(String string) {
        return miniMessage.deserialize(string);
    }

    /**
     * Colorize string with RGB and custom tags
     * @param string Source string
     * @param resolver Tag resolver
     * @return Colorful string
     */
    public static Component colorize(String string, TagResolver resolver) {
        return miniMessage.deserialize(string, resolver);
    }

    /**
     * Convert color to TextColor
     * @param color Color
     * @return TextColor
     */
    public static TextColor asTextColor(Color color) {
        return TextColor.color(color.getRed(), color.getGreen(), color.getBlue());
    }

}
