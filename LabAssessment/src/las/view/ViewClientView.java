/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package las.view;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import las.listener.ViewClientViewListener;
import las.rmi.LASClient;
import las.util.ImageUtility;

/**
 *
 * @author megha
 */
public class ViewClientView extends JFrame implements Runnable {

    private JLabel view = null;
    private static Thread thread = null;
    private JButton shutButton = null;
    private LASClient lASClient = null;
    private JScrollPane scrollPane = null;

    //Constructors....
    public ViewClientView(String title, LASClient lasClient) throws HeadlessException {
        super(title);
        this.lASClient = lasClient;
    }

    public ViewClientView() throws HeadlessException {
    }

    //Accessor and mutators....
    public LASClient getlASClient() {
        return lASClient;
    }

    public void setlASClient(LASClient lASClient) {
        this.lASClient = lASClient;
    }

    public void showView() {

        view = new JLabel();
        scrollPane = new JScrollPane(view);
        scrollPane.setPreferredSize(new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 50, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 100));
        this.add(scrollPane);
        thread = new Thread(this);

        //JFrame method calls....
        this.pack();
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setVisible(true);
        this.addWindowListener(new ViewClientViewListener());
        thread.start();
    }

    @Override
    public void run() {
        System.out.println("Thread Started");
        while (true) {
            BufferedImage bi = null;
            try {
                bi = ImageUtility.read(lASClient.captureScreen());
                if (bi == null) {
                    view.setText("Unable to load image");
                } else {
                    view.setIcon(new ImageIcon(bi));
                }
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(ViewClientView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RemoteException ex) {
                Logger.getLogger(ViewClientView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void updateView(LASClient lASClient) {
        System.out.println("View Updated");
        thread.suspend();
        setlASClient(lASClient);
        thread.resume();
    }
}
