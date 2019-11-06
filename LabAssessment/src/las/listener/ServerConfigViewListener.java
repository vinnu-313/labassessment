/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package las.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;
import las.config.Configuration;
import las.view.ClientView;
import las.view.ServerView;

/**
 *
 * @author megha
 */
public class ServerConfigViewListener extends WindowAdapter implements ActionListener {

    public ServerConfigViewListener() {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Apply")) {
            if (Configuration.storeCofig()) {
                JOptionPane.showMessageDialog(null, "Changes Saved !");
            } else {
                JOptionPane.showMessageDialog(null, "Unable to save changes !", "Error - Lab Assessment v1.0", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getActionCommand().equals("ExitServer")) {
            ServerView.getServerConfigView().dispose();
            ServerView.setServerConfigView(null);
        } else {
            ClientView.getServerConfigView().dispose();
            ClientView.setServerConfigView(null);
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        ServerView.getServerConfigView().dispose();
        ServerView.setServerConfigView(null);
    }
}
