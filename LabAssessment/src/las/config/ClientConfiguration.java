/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package las.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author megha
 */
public class ClientConfiguration {

    private static InetAddress inetAddress = null;
    private static int port = 0;

    public static int getPort() {
        if (port == 0) {
            port = 2012;
        }
        return port;
    }

    public static void setPort(int port) {
        ClientConfiguration.port = port;
    }

    public static InetAddress getInetAddress() throws UnknownHostException {
        if (inetAddress == null) {
            inetAddress = InetAddress.getLocalHost();
        }
        return inetAddress;
    }

    public static void setInetAddress(InetAddress inetAddress) {
        ClientConfiguration.inetAddress = inetAddress;
    }
}
