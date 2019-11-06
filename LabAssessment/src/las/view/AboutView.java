/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package las.view;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import las.listener.AboutViewListener;

/**
 *
 * @author megha
 */
public class AboutView extends JDialog {

    private JLabel label = null;
    private JPanel panel = null;

    public AboutView(JFrame owner, String title) {
        super(owner, title);
    }

    public AboutView() {
    }

    public void showAbout() {
        panel = new JPanel();
        panel.setBorder(new CompoundBorder(new EmptyBorder(50, 50, 50, 50), new TitledBorder("About")));
        label = new JLabel("<html><b>Name : </b>Lab Assessment<br/>"
                + "<b>Version : </b>1.0<br/>"
                + "<b>Vendor : </b>Santhosh Y. K.(1BY09MCA47)");
        panel.add(label);
        this.add(panel);
        this.pack();
        this.setVisible(true);
        this.addWindowListener(new AboutViewListener());
    }
}
