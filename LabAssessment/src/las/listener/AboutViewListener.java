/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package las.listener;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 * @author megha
 */
public class AboutViewListener extends WindowAdapter {

    @Override
    public void windowClosing(WindowEvent e) {
        e.getWindow().dispose();
    }
}
