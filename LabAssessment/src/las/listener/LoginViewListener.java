/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package las.listener;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import las.view.ClientView;

/**
 *
 * @author megha
 */
public class LoginViewListener extends WindowAdapter {

    @Override
    public void windowClosing(WindowEvent e) {
        if (((JFrame) e.getWindow()).getTitle().equals("Server Login")) {
            int i = JOptionPane.showConfirmDialog(e.getWindow(), "Are you sure want to exit ?", "Confirm Exit", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (i == JOptionPane.OK_OPTION) {
                System.exit(0);
            }
        } else {
            ClientView.setLoginView(null);
            System.out.println();
            e.getWindow().dispose();
        }

    }
}
