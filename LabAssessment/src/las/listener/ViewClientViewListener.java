/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package las.listener;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import las.view.ServerView;

/**
 *
 * @author megha
 */
public class ViewClientViewListener extends WindowAdapter {

    @Override
    public void windowClosing(WindowEvent e) {
        ServerView.setClient(null);
        e.getWindow().dispose();
    }
}
