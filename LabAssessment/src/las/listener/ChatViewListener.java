/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package las.listener;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import las.config.ClientConfiguration;
import las.config.ServerConfiguration;
import las.rmi.LASChat;
import las.util.LasUtil;
import las.view.ChatView;
import las.view.ClientView;

/**
 *
 * @author megha
 */
public class ChatViewListener extends WindowAdapter {

    @Override
    public void windowClosing(WindowEvent e) {
        ChatView chatView = (ChatView) e.getWindow();
        ArrayList<LASChat> list = chatView.getDest();
        for (LASChat c : list) {
            try {
                c.removeFromList(chatView.getLookUp(), chatView.getChatOwner());
            } catch (RemoteException ex) {
                Logger.getLogger(ChatViewListener.class.getName()).log(Level.SEVERE, "Unable to removeFrom list", ex);
            }
        }
        try {
            if (chatView.getTitle().equals("Chat")) {
                LasUtil.unBindNow(ClientConfiguration.getInetAddress().getHostAddress(), ServerConfiguration.getPort(), chatView.getLookUp());
            } else {
                LasUtil.unBindNow(ClientConfiguration.getInetAddress().getHostAddress(), ClientConfiguration.getPort(), chatView.getLookUp());
            }
        } catch (Exception ex) {
            Logger.getLogger(ChatViewListener.class.getName()).log(Level.SEVERE, "From Window Closing", ex);
        }
        chatView.setLookUp(null);
        ClientView.setChatView(null);
        e.getWindow().dispose();
    }
}
