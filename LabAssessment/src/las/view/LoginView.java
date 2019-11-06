/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package las.view;

import java.sql.SQLException;
import las.listener.LoginViewListener;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import las.config.MyConnection;
import las.config.ServerConfiguration;
import las.config.User;
import las.rmi.LASServer;

/**
 *
 * @author megha
 */
public class LoginView extends JFrame implements ActionListener {

    private JPanel mainPanel = null;
    private JLabel nameLabel = null;
    private JLabel warningLabel = null;
    private JLabel passwordLabel = null;
    private JTextField username = null;
    private JTextField password = null;
    private JPanel textPanel = null;
    private JButton loginButton = null;
    private JButton cancelButton = null;

    //Constructors.....
    public LoginView(String title) throws HeadlessException {
        super(title);
    }

    public LoginView() throws HeadlessException {
    }

    public void showLoginView() {
        mainPanel = new JPanel();
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        textPanel = new JPanel(new GridLayout(3, 2, 10, 20));
        nameLabel = new JLabel("Username : ");
        textPanel.add(nameLabel);
        username = new JTextField(10);
        textPanel.add(username);
        passwordLabel = new JLabel("Password : ");
        textPanel.add(passwordLabel);
        password = new JPasswordField(10);
        textPanel.add(password);
        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        textPanel.add(loginButton);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        textPanel.add(cancelButton);
        mainPanel.add(textPanel);
        warningLabel = new JLabel();
        mainPanel.add(warningLabel);
        this.add(mainPanel);
        this.addWindowListener(new LoginViewListener());

        //JFrame Settings...
        this.pack();
        this.setVisible(true);
        this.setResizable(false);
        this.getRootPane().setDefaultButton(loginButton);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        if (this.getTitle().equals("Server Login")) {
            if (button.getText().equals("Login")) {
                try {
                    if (username.getText().equalsIgnoreCase("admin")) {
                        Connection connection = MyConnection.getConnection();
                        Statement statement = connection.createStatement();
                        ResultSet rs = statement.executeQuery("select * from las.info where usn='" + username.getText() + "'");
                        if (rs.next() && rs.getString(3).equals(password.getText())) {
                            final ServerView serverView = new ServerView("Lab Assessment Server 1.0");
                            User u = new User();
                            u.setName(rs.getString(1)); 
                            u.setUname(rs.getString(2)); 
                            ServerView.setUser(u);
                            LASServer lass = (LASServer) UnicastRemoteObject.exportObject(serverView, 0);
                            Registry registry = LocateRegistry.createRegistry(ServerConfiguration.getPort());
                            registry.rebind("LAS_SERVER", lass);
                            System.out.println("Registered to RMI");
                            javax.swing.SwingUtilities.invokeLater(new Runnable() {

                                @Override
                                public void run() {
                                    serverView.createAndShow();
                                }
                            });
                            this.dispose();
                        } else {
                            warningLabel.setText("Invalid username/password");
                        }
                    } else {
                        warningLabel.setText("Invalid username/password");
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(LoginView.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(LoginView.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(LoginView.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                int i = JOptionPane.showConfirmDialog(this, "Are you sure want to exit ?", "Confirm Exit", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (i == JOptionPane.OK_OPTION) {
                    System.exit(0);
                }
            }
        } else {
            if (button.getText().equals("Login")) {
                try {
                    User user = new User(username.getText(), password.getText());
                    Registry registry = LocateRegistry.getRegistry(ServerConfiguration.getInetAddress().getHostAddress(), ServerConfiguration.getPort());
                    LASServer server = (LASServer) registry.lookup("LAS_SERVER");
                    user = server.isValid(user);
                    if (user != null) {
                        ClientView.setUser(user);
                        if (ClientView.getChatView() != null) {
                            ClientView.getChatView().setChatOwner(user.getName());
                        }
                        ClientView.setLoginView(null);
                        ClientView.loginMenuItem.setLabel("Logout");
                        this.dispose();
                    } else {
                        warningLabel.setText("Invalid username/password");
                    }
                } catch (NotBoundException ex) {
                    JOptionPane.showMessageDialog(null, "Unable to reach server...", "Error - Lab Assessment Client 1.0", JOptionPane.ERROR_MESSAGE);
                    Logger.getLogger(LoginViewListener.class.getName()).log(Level.SEVERE, "NotBound Exception", ex);
                } catch (RemoteException ex) {
                    JOptionPane.showMessageDialog(null, "Unable to reach server...", "Error - Lab Assessment Client 1.0", JOptionPane.ERROR_MESSAGE);
                    Logger.getLogger(LoginViewListener.class.getName()).log(Level.SEVERE, "Remote Exception", ex);
                } catch (UnknownHostException ex) {
                    JOptionPane.showMessageDialog(null, "Unable to reach server...", "Error - Lab Assessment Client 1.0", JOptionPane.ERROR_MESSAGE);
                    Logger.getLogger(LoginViewListener.class.getName()).log(Level.SEVERE, "UnknownHostException", ex);
                }

            } else {
                ClientView.setLoginView(null);
                System.out.println("Closing Login");
                this.dispose();
            }
        }
    }
}
