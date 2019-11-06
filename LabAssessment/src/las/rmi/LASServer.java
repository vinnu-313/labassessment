/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package las.rmi;

import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import las.config.User;

/**
 *
 * @author megha
 */
public interface LASServer extends Remote {

    void addToList(InetAddress s) throws RemoteException;

    void removeFromList(InetAddress s) throws RemoteException;

    String requestChatServer(InetAddress s) throws RemoteException;
    
    String getQuestions(String usn) throws RemoteException;

    public User isValid(User user) throws RemoteException;
    
    public Collection<String[]> getVivaQuestions(String usn) throws RemoteException;
    
    public boolean saveAnswer(String usn, int questionid, String ans) throws RemoteException;
}
