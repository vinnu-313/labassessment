/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package las.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import las.config.ClientConfiguration;
import las.config.ServerConfiguration;
import las.rmi.LASClient;
import las.util.LasUtil;
import las.view.*;

/**
 *
 * @author megha
 */
public class ServerViewListener extends WindowAdapter implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        if (button.getText().equals("View")) {
            LASClient lASClient = null;
            if (ServerView.getClient() == null) {
                if (ServerView.getClientList().getSelectedIndex() != -1) {
                    InetAddress in = (InetAddress) ServerView.getClientListModel().elementAt(ServerView.getClientList().getSelectedIndex());
                    try {
                        Registry registry = LocateRegistry.getRegistry(in.getHostAddress(), ClientConfiguration.getPort());
                        lASClient = (LASClient) registry.lookup("LAS_CLIENT");
                    } catch (NotBoundException ex) {
                        Logger.getLogger(ServerViewListener.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (RemoteException ex) {
                        Logger.getLogger(ServerViewListener.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    ServerView.setClient(new ViewClientView("Client - " + in, lASClient));
                    ServerView.getClient().showView();
                } else {
                    JOptionPane.showMessageDialog(button.getRootPane(), "Choose atleast one to start with...", "Choose a client.", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                if (ServerView.getClientList().getSelectedIndex() != -1) {
                    InetAddress in = (InetAddress) ServerView.getClientListModel().elementAt(ServerView.getClientList().getSelectedIndex());
                    try {
                        Registry registry = LocateRegistry.getRegistry(in.getHostAddress(), ClientConfiguration.getPort());
                        lASClient = (LASClient) registry.lookup("LAS_CLIENT");
                    } catch (NotBoundException ex) {
                        Logger.getLogger(ServerViewListener.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (RemoteException ex) {
                        Logger.getLogger(ServerViewListener.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                ServerView.getClient().updateView(lASClient);
            }

        } else if (button.getText().equals("Chat")) {
            int index[] = ServerView.getClientList().getSelectedIndices();
            if (index.length > 0) {
                ChatView chatView = new ChatView("Chat", ServerView.getUser().getName());   //Creating Chat window...
                //Generating lookUp....
                Random r = new Random();
                String lookUp = "Client" + r.nextInt(4);
                //Binding to RMI registry on server at port 2011
                try {
                    LasUtil.bindNow(ServerConfiguration.getInetAddress().getHostAddress(), ServerConfiguration.getPort(), lookUp, chatView);
                } catch (UnknownHostException ex) {
                    Logger.getLogger(ServerViewListener.class.getName()).log(Level.SEVERE, null, ex);
                }
                //Requesting Clients to join chat by issuing lookUp...
                for (int i : index) {
                    InetAddress inetAddress = (InetAddress) ServerView.getClientListModel().elementAt(i);
                    try {
                        Registry registry = LocateRegistry.getRegistry(inetAddress.getHostAddress(), ClientConfiguration.getPort());
                        LASClient lASClient = (LASClient) registry.lookup("LAS_CLIENT");
                        lASClient.requestChatClient(lookUp);
                    } catch (NotBoundException ex) {
                        Logger.getLogger(ServerViewListener.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (RemoteException ex) {
                        Logger.getLogger(ServerViewListener.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                //Adding to the dest list....
                for (int i : index) {
                    chatView.addToList((InetAddress) ServerView.getClientListModel().elementAt(i));
                }
                chatView.showChatView();
            } else {
                JOptionPane.showMessageDialog(button.getParent().getParent(), "Choose atleast one client to start with...", "Kindly Choose some client(s)", JOptionPane.ERROR_MESSAGE);
            }

        } else if (button.getText().equals("Add Questions")) {
            ServerView.getAddQuestionView().showAddQuestionView();
        } else if (button.getText().equals("Add Viva Questions")) {
            new VivaQuestionView().setVisible(true);
        } else if (button.getText().equals("Finalize Marks")) {
            new MarksFinalizeView(null, true).setVisible(true);
        } else if (button.getText().equals("Add Users")) {
            ServerView.getAddUser().showAddUserView();
        } else if (button.getText().equals("Configuration")) {
            ServerView.getServerConfigView().showServerConfigView();
        } else if (button.getText().equals("About")) {
            AboutView aboutView = new AboutView(null, "About");
            aboutView.showAbout();
        }else if(button.getText().equals("Current Batch")){
            new CurrentBatchView(null, true).setVisible(true);
        }else if(button.getText().equals("Reports")){
            new ReportView(null, false).setVisible(true);
        }else if (button.getText().equals("Exit !")) {
            int i = JOptionPane.showConfirmDialog(button.getParent().getParent(), "Are you sure want to exit ?", "Confirm Exit", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (i == JOptionPane.OK_OPTION) {
                System.exit(0);
            }
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        int i = JOptionPane.showConfirmDialog(e.getWindow(), "Are you sure want to exit ?", "Confirm Exit", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (i == JOptionPane.OK_OPTION) {
            System.exit(0);
        }
    }
}
