/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package las.view;

import java.awt.GridLayout;
import java.awt.HeadlessException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import las.listener.AddUserViewLister;

/**
 *
 * @author megha
 */
public class AddUserView extends JFrame {
    
    private JPanel mainPanel = null;
    private JPanel contentPanel = null;
    private JLabel nameLabel = null;
    private JLabel usnLabel = null;
    private JLabel passwdLabel = null;
    public JTextField nameField = null;
    public JTextField usnField = null;
    public JTextField passwdField = null;
    private JButton applyButton = null;
    private JButton exitButton = null;
    public JLabel messageLabel = null;
    
    public AddUserView(String title) throws HeadlessException {
        super(title);
    }
    
    public AddUserView() throws HeadlessException {
    }
    
    public void showAddUserView() {
        mainPanel = new JPanel();
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPanel = new JPanel(new GridLayout(4, 2, 10, 30));
        nameLabel = new JLabel("Name : ");
        contentPanel.add(nameLabel);
        nameField = new JTextField("");
        nameField.setColumns(20);
        contentPanel.add(nameField);
        usnLabel = new JLabel("USN : ");
        contentPanel.add(usnLabel);
        usnField = new JTextField();
        usnField.setColumns(20);
        contentPanel.add(usnField);
        passwdLabel = new JLabel("Password : ");
        contentPanel.add(passwdLabel);
        passwdField = new JTextField();
        passwdField.setColumns(20);
        contentPanel.add(passwdField);
        applyButton = new JButton("Apply");
        applyButton.addActionListener(new AddUserViewLister());
        contentPanel.add(applyButton);
        exitButton = new JButton("Exit");
        exitButton.addActionListener(new AddUserViewLister());
        contentPanel.add(exitButton);
        mainPanel.add(contentPanel);
        messageLabel = new JLabel();
        mainPanel.add(messageLabel);
        this.add(mainPanel);

        //JFrame method calls...
        this.getRootPane().setDefaultButton(applyButton);
        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new AddUserViewLister());
    }
}
