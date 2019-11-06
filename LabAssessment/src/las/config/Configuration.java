/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package las.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import las.view.ClientView;

/**
 *
 * @author megha
 */
public class Configuration {

    public static void initCofig() {
        try {
            File file = new File(Configuration.class.getProtectionDomain().getCodeSource().getLocation().getFile());
            file = file.getParentFile();
            file = new File(file, "las.ini");
            char[] c = new char[512];
            FileReader fileReader = new FileReader(file);
            fileReader.read(c);
            String config = new String(c);
            String t[] = config.split(":");
            ServerConfiguration.setInetAddress(InetAddress.getByName(t[1]));
//            ServerConfiguration.setPort(Integer.parseInt(t[2]));
        } catch (IOException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean storeCofig() {
        String ip[] = ClientView.getServerConfigView().ipTextField.getText().split("\\.");
        if (ip.length > 4) {
            System.out.println("Length : " + ip.length);
            return false;
        }
        for (int i = 0; i < ip.length; ++i) {
            try {
                if (Integer.parseInt(ip[i]) > 255 || Integer.parseInt(ip[i]) < 0) {
                    return false;
                }
            } catch (Exception e) {
                System.out.println("Error : " + e);
                return false;
            }
        }
        try {
            File file = new File(Configuration.class.getProtectionDomain().getCodeSource().getLocation().getFile());
            file = file.getParentFile();
            file = new File(file, "las.ini");
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("SERVER:" + ClientView.getServerConfigView().ipTextField.getText() + ":2011");
            fileWriter.flush();
        } catch (IOException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        JOptionPane.showMessageDialog(null, "Kindly restart the application to save changes.\nApplication will exit now !");
        System.exit(0);
        return true;
    }

    public static void updateConfig() {
    }
}
