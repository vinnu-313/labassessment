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
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import las.config.MyConnection;
import las.view.ServerView;

/**
 *
 * @author megha
 */
public class AddQuestionViewListener extends WindowAdapter implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Apply")) {
            try {
                Connection connection = MyConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("insert into las.questions(question) values(?)");
                if (ServerView.getAddQuestionView().questionField.getText().length() > 500) {
                    ServerView.getAddQuestionView().messageLabel.setText("Text too long !");
                } else if (ServerView.getAddQuestionView().questionField.getText().length() > 0) {
                    preparedStatement.setString(1, ServerView.getAddQuestionView().questionField.getText());
                    preparedStatement.executeUpdate();
                    ServerView.getAddQuestionView().messageLabel.setText("Question Added !");
                    ServerView.getAddQuestionView().questionField.setText("");
                } else {
                    ServerView.getAddQuestionView().messageLabel.setText("Text too short !");
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(AddQuestionViewListener.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(AddQuestionViewListener.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else if (e.getActionCommand().equals("Reset")) {
            int i = JOptionPane.showConfirmDialog(null, "Do you really want reset ?\nClicking 'OK' will delete all questions and related information !", "Irreversible Action - Lab Assessment Server v1.0", JOptionPane.OK_CANCEL_OPTION);
            if (i == JOptionPane.OK_OPTION) {
                try {
                    Connection connection = MyConnection.getConnection();
                    Statement statement = connection.createStatement();
                    statement.executeUpdate("delete from las.questionmap");
                    statement.executeUpdate("delete from las.questions");
                    ServerView.getAddQuestionView().messageLabel.setText("Reset Successful !");
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(AddQuestionViewListener.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(AddQuestionViewListener.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        } else {
            ServerView.getAddQuestionView().dispose();
            ServerView.setAddQuestionView(null);
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        ServerView.getAddQuestionView().dispose();
        ServerView.setAddQuestionView(null);
    }
}
