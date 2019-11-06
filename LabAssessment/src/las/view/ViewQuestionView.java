/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package las.view;

import java.awt.HeadlessException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import las.config.ServerConfiguration;
import las.listener.ViewQuestionViewListener;
import las.rmi.LASServer;

/**
 *
 * @author megha
 */
public class ViewQuestionView extends JFrame {

    private JLabel question = null;

    public ViewQuestionView(String title) throws HeadlessException {
        super(title);
    }

    public ViewQuestionView() throws HeadlessException {
    }

    public void showViewQuestionView() {
        if (ClientView.getUser().getUname() == null) {
            JOptionPane.showMessageDialog(null, "Kindly login to view Questions !", "Login required !", JOptionPane.WARNING_MESSAGE);
            this.dispose();
        } else {
            try {
                Registry registry = LocateRegistry.getRegistry(ServerConfiguration.getInetAddress().getHostAddress(), ServerConfiguration.getPort());
                LASServer lASServer = (LASServer) registry.lookup("LAS_SERVER");
                String temp = "";
                temp = lASServer.getQuestions(ClientView.getUser().getUname());
                if (temp.equals("")) {
                    temp = "No Questions are defined ! Kindly contact the administrator.";
                }
                question = new JLabel(temp);
            } catch (UnknownHostException ex) {
                Logger.getLogger(ViewQuestionView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NotBoundException ex) {
                Logger.getLogger(ViewQuestionView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RemoteException ex) {
                Logger.getLogger(ViewQuestionView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        question.setBorder(new EmptyBorder(50, 50, 50, 50));
        add(question);

        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new ViewQuestionViewListener());

    }
}
