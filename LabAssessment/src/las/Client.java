/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package las;

import java.awt.AWTException;
import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import javax.swing.JOptionPane;
import las.config.ClientConfiguration;
import las.config.Configuration;
import las.config.ServerConfiguration;
import las.rmi.LASClient;
import las.rmi.LASServer;
import las.view.ClientView;
import las.view.ServerConfigView;

/**
 *
 * @author megha
 */
public class Client {

    public static void main(String args[]) {
        try {
            Configuration.initCofig();
            ClientConfiguration.setInetAddress(InetAddress.getLocalHost());
            Registry registry = LocateRegistry.getRegistry(ServerConfiguration.getInetAddress().getHostAddress(), ServerConfiguration.getPort());
            LASServer s = (LASServer) registry.lookup("LAS_SERVER");
            Registry clientRegisty = LocateRegistry.createRegistry(ClientConfiguration.getPort());
            final ClientView clientView = new ClientView();
            LASClient c = (LASClient) UnicastRemoteObject.exportObject(clientView, 0);
            clientRegisty.rebind("LAS_CLIENT", c);
            System.out.println("Register to RMI");
            s.addToList(InetAddress.getLocalHost());
            javax.swing.SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    try {
                        clientView.addToTray();
                    } catch (AWTException ex) {
                        JOptionPane.showMessageDialog(null, "Unable to add to tray !", "System Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        } catch (Exception e) {
            int r = JOptionPane.showConfirmDialog(null, "Server Not Found !\nDo you want change server configuration ?", "Error", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.OK_OPTION) {
                ServerConfigView configuration = new ServerConfigView("Configuration - Lab Assessment v1.0");
                ClientView.setServerConfigView(configuration);
                configuration.showServerConfigView();
            }
            System.out.println("Error from client : " + e);
        }
    }
}
