package br.com.kafkamanager.infrastructure.swing.util;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon.ColorFilter;
import java.awt.Color;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SetupColor {
    public static ColorFilter getIconColor() {
        return new ColorFilter((Color color) -> {
            if (FlatLaf.isLafDark()) {
                return Color.lightGray;
            } else {
                return Color.BLACK;
            }
        });
    }

    public static Color lightenColor(Color color, double percent) {
        // Get the RGB components of the color
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();

        // Calculate the amount to lighten each component
        int lightenAmount = (int) Math.round(255 * percent / 100);

        // Lighten each component by the calculated amount (clamping values to [0, 255])
        red = Math.min(red + lightenAmount, 255);
        green = Math.min(green + lightenAmount, 255);
        blue = Math.min(blue + lightenAmount, 255);

        // Create a new Color object with the lightened components
        return new Color(red, green, blue, color.getAlpha());
    }

    public static Color darkenColor(Color color, double percent) {
        // Get the RGB components of the color
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();

        // Calculate the amount to darken each component
        int lightenAmount = (int) Math.round(255 * percent / 100);

        // Darken each component by the calculated amount (clamping values to [0, 255])
        red = Math.max(red - lightenAmount, 0);
        green = Math.max(green - lightenAmount, 0);
        blue = Math.max(blue - lightenAmount, 0);

        // Create a new Color object with the darkened components
        return new Color(red, green, blue, color.getAlpha());
    }
}
