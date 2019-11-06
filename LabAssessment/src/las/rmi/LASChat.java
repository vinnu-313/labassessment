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
public interface LASChat extends Remote {

    public void addText(String s, String user) throws RemoteException;

    public void removeFromList(String lookUp, String user) throws RemoteException;

    public String getLookUp() throws RemoteException;
}
