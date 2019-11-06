/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package las.style;

import java.awt.Font;

/**
 *
 * @author megha
 */
public class ButtonFont {

    private static Font buttonFont = new Font("Ariel", Font.BOLD, 18);

    public static Font getButtonFont() {
        return buttonFont;
    }

    public static void setButtonFont(Font buttonFont) {
        ButtonFont.buttonFont = buttonFont;
    }
}
