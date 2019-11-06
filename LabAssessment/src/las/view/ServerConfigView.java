/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package las.view;

import las.listener.ServerConfigViewListener;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import las.config.ServerConfiguration;

/**
 *
 * @author megha
 */
public class ServerConfigView extends JFrame {

    private JPanel mainPanel = null;
    private JLabel ipLabel = null;
    private JLabel portLabel = null;
    private JLabel dummyLabel = null;
    public JTextField ipTextField = null;
    public JTextField portTextField = null;
    private JButton applyButton = null;
    private JButton exitButton = null;

    public ServerConfigView() throws HeadlessException {
    }

    public ServerConfigView(String title) throws HeadlessException {
        super(title);
    }

    public void showServerConfigView() {
        mainPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        mainPanel.setBorder(new CompoundBorder(new EmptyBorder(20, 20, 20, 20), new TitledBorder("Server Configuration")));
        ipLabel = new JLabel("IP Address : ");
        ipLabel.setBorder(new EmptyBorder(0, 30, 0, 0));
        mainPanel.add(ipLabel);
        try {
            ipTextField = new JTextField(ServerConfiguration.getInetAddress().getHostAddress());
        } catch (UnknownHostException ex) {
            Logger.getLogger(ServerConfigView.class.getName()).log(Level.SEVERE, null, ex);
        }
        ipTextField.setBorder(new EmptyBorder(0, 0, 0, 30));
        mainPanel.add(ipTextField);
        portLabel = new JLabel("Port No. : ");
        portLabel.setBorder(new EmptyBorder(0, 30, 0, 0));
        mainPanel.add(portLabel);
        portTextField = new JTextField(Integer.toString(ServerConfiguration.getPort()));
        portTextField.setBorder(new EmptyBorder(0, 0, 0, 30));
        mainPanel.add(portTextField);
        exitButton = new JButton("Exit !");
        if (this.getTitle().equals("Server Configuration")) {
            dummyLabel = new JLabel();
            mainPanel.add(dummyLabel);
            ipTextField.setEditable(false);
            portTextField.setEditable(false);
            exitButton.setActionCommand("ExitServer");
        } else {
            applyButton = new JButton("Apply");
            applyButton.addActionListener(new ServerConfigViewListener());
            portTextField.setEditable(false);
            mainPanel.add(applyButton);
            exitButton.setActionCommand("ExitClient");
        }
        exitButton.addActionListener(new ServerConfigViewListener());
        mainPanel.add(exitButton);
        add(mainPanel);
        this.getRootPane().setDefaultButton(applyButton);
        //JFrame method calls......
        this.addWindowListener(new ServerConfigViewListener());
        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }
}
