package de.klochk.interchat.utility;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

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
     * Convert color to #RRGGBB format
     * @param color Color
     * @return Formatted string
     */
    public static String asHexColorString(Color color) {
        return String.format("#%02x%02x%02x",
                color.getRed(), color.getGreen(), color.getBlue());
    }

}
