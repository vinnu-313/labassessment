/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package las.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import las.config.MyConnection;
import las.view.ServerView;

/**
 *
 * @author megha
 */
public class AddUserViewLister extends WindowAdapter implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Apply")) {
            if (ServerView.getAddUser().nameField.getText().length() <= 0) {
                ServerView.getAddUser().messageLabel.setText("Name Can't be empty !");
            } else if (ServerView.getAddUser().usnField.getText().length() <= 0) {
                ServerView.getAddUser().messageLabel.setText("USN Can't be empty !");
            } else if (ServerView.getAddUser().passwdField.getText().length() < 6) {
                ServerView.getAddUser().messageLabel.setText("Password too short !");
            } else {
                try {
                    Connection con = MyConnection.getConnection();
                    PreparedStatement ps = con.prepareStatement("insert into las.info values(?,?,?)");
                    ps.setString(1, ServerView.getAddUser().nameField.getText());
                    ps.setString(2, ServerView.getAddUser().usnField.getText());
                    ps.setString(3, ServerView.getAddUser().passwdField.getText());
                    ps.executeUpdate();
                    ServerView.getAddUser().messageLabel.setText("User Added Successfully !");
                    ServerView.getAddUser().nameField.setText("");
                    ServerView.getAddUser().usnField.setText("");
                    ServerView.getAddUser().passwdField.setText("");
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(AddUserViewLister.class.getName()).log(Level.SEVERE, null, ex);
                    ServerView.getAddUser().messageLabel.setText("Something went wrong !");
                } catch (SQLException ex) {
                    Logger.getLogger(AddUserViewLister.class.getName()).log(Level.SEVERE, null, ex);
                    ServerView.getAddUser().messageLabel.setText("User already exist !");
                }
            }
        } else {
            ServerView.getAddUser().dispose();
            ServerView.setAddUser(null);
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        ServerView.getAddUser().dispose();
        ServerView.setAddUser(null);
    }
}
