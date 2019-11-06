/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package las.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author megha
 */
public interface LASClient extends Remote {

    public void requestChatClient(String lookUp) throws RemoteException;

    public byte[] captureScreen() throws RemoteException;
}
