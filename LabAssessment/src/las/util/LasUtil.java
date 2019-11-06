/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package las.util;

import java.awt.Image;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import las.config.ServerConfiguration;
import las.rmi.LASChat;
import las.view.ChatView;
import las.view.ClientView;

/**
 *
 * @author megha
 */
public class LasUtil {

    public static boolean verifyThis(InetAddress s, int port) {
        try {
            Registry registry = LocateRegistry.getRegistry(s.getHostAddress(), port);
            Object object = registry.lookup("Client");
            return true;
        } catch (NotBoundException ex) {
            Logger.getLogger(LasUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AccessException ex) {
            Logger.getLogger(LasUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(LasUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static boolean verifyThis(String lookUp) {
        try {
            Registry registry = LocateRegistry.getRegistry(ServerConfiguration.getInetAddress().getHostAddress(), ServerConfiguration.getPort());
            Object object = registry.lookup(lookUp);
            return true;
        } catch (NotBoundException ex) {
            Logger.getLogger(LasUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(LasUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(LasUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static void bindNow(String hostAddress, int i, String lookUp, ChatView chatView) {
        try {
            Registry registry = LocateRegistry.getRegistry(hostAddress, i);
            LASChat chat = (LASChat) UnicastRemoteObject.exportObject(chatView, 0);
            registry.rebind(lookUp, chat);
            chatView.setLookUp(lookUp);
        } catch (RemoteException ex) {
            Logger.getLogger(LasUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void bindNow(String hostAddress, int port, ChatView chatView) {
        try {
            Registry registry = LocateRegistry.getRegistry(hostAddress, port);
            LASChat chat = (LASChat) UnicastRemoteObject.exportObject(chatView, 0);
            registry.rebind("Client", chat);
            chatView.setLookUp("Client");
        } catch (RemoteException ex) {
            Logger.getLogger(LasUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void unBindNow(String hostAddress, int port, String lookUp) {
        try {
            Registry registry = LocateRegistry.getRegistry(hostAddress, port);
            registry.unbind(lookUp);
            System.out.println("Successfully unbound");
        } catch (NotBoundException ex) {
            Logger.getLogger(LasUtil.class.getName()).log(Level.SEVERE, "Unable to find lookup at host", ex);
        } catch (RemoteException ex) {
            Logger.getLogger(LasUtil.class.getName()).log(Level.SEVERE, "Unable to find lookup at host", ex);
        }
    }

    public static Image createImage(String path, String description) {
        URL imageURL = ClientView.class.getResource(path);

        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }
}
