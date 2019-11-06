/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package las.listener;

import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import las.config.ClientConfiguration;
import las.config.ServerConfiguration;
import las.rmi.LASServer;
import las.util.LasUtil;
import las.view.*;

/**
 *
 * @author megha
 */
public class ClientViewListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        MenuItem menuItem = (MenuItem) e.getSource();
        if (menuItem.getLabel().equals("Login")) {
            if (ClientView.getLoginView() == null) {
                LoginView loginView = new LoginView("Login - Lab Assessment Client 1.0");
                loginView.showLoginView();
                ClientView.setLoginView(loginView);
            } else {
                JOptionPane.showMessageDialog(null, "Login already open");
            }
        } else if (menuItem.getLabel().equals("Logout")) {
            if (ClientView.getChatView() != null) {
                try {
                    ClientView.getChatView().setChatOwner(ClientConfiguration.getInetAddress().getHostName());
                } catch (UnknownHostException ex) {
                    Logger.getLogger(ClientViewListener.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            ClientView.setUser(null);
            ClientView.loginMenuItem.setLabel("Login");
        } else if (menuItem.getLabel().equals("Ask")) {
            if (ClientView.getUser().getUname() != null) {
                if (ClientView.getChatView() == null) {
                    ChatView chatView = new ChatView("Conversation", ClientView.getUser().getName());
                    String res = null;
                    try {
                        LasUtil.bindNow(ClientConfiguration.getInetAddress().getHostAddress(), ClientConfiguration.getPort(), chatView);
                        Registry registry = LocateRegistry.getRegistry(ServerConfiguration.getInetAddress().getHostAddress(), ServerConfiguration.getPort());
                        LASServer lASServer = (LASServer) registry.lookup("LAS_SERVER");
                        res = lASServer.requestChatServer(ClientConfiguration.getInetAddress());
                    } catch (NotBoundException ex) {
                        Logger.getLogger(ClientViewListener.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (RemoteException ex) {
                        Logger.getLogger(ClientViewListener.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (UnknownHostException ex) {
                        Logger.getLogger(ClientViewListener.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (res == null) {
                        JOptionPane.showMessageDialog(null, "Server Rejected the request.", "Request Rejected", JOptionPane.ERROR_MESSAGE);
                    } else {
                        LasUtil.verifyThis(res);
                        System.out.println("Server Bind Verifed");
                        chatView.addToList(res);
                        ClientView.setChatView(chatView);
                        chatView.showChatView();
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Ask running...!", "Chat Status", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Kindly login before starting a coversation !", "Login required !", JOptionPane.WARNING_MESSAGE);
            }
        } else if (menuItem.getLabel().equals("View Question")) {
            if (ClientView.getUser().getUname() == null) {
                JOptionPane.showMessageDialog(null, "Kindly login to view the question !", "Login required !", JOptionPane.WARNING_MESSAGE);
            } else {
                ClientView.getViewQuestionView().showViewQuestionView();
            }
        } else if (menuItem.getLabel().equals("View Viva Question")) {
            if (ClientView.getUser().getUname() == null) {
                JOptionPane.showMessageDialog(null, "Kindly login to view the viva question !", "Login required !", JOptionPane.WARNING_MESSAGE);
            } else {
                new ViewVivaQuestionView().setVisible(true);
            }
        } else if (menuItem.getLabel().equals("Configure")) {
            ClientView.getServerConfigView().showServerConfigView();
        } else if (menuItem.getLabel().equals("About")) {
            AboutView aboutView = new AboutView();
            aboutView.showAbout();
        } else if (menuItem.getLabel().equals("Exit")) {
//            JOptionPane.showMessageDialog(null, "Exit");
            try {
                Registry registry = LocateRegistry.getRegistry(ClientConfiguration.getInetAddress().getHostAddress(), 2011);
                LASServer s = (LASServer) registry.lookup("LAS_SERVER");
                s.removeFromList(InetAddress.getLocalHost());
            } catch (Exception ex) {
                System.out.println("Server not found. Exiting client");
            }
            System.exit(0);
        }
    }
}
