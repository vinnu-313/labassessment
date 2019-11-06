/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package las.view;

import java.awt.AWTException;
import java.io.IOException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import las.config.ClientConfiguration;
import las.config.Configuration;
import las.config.User;
import las.listener.ClientViewListener;
import las.rmi.LASClient;
import las.util.LasUtil;

/**
 *
 * @author megha
 */
public class ClientView implements LASClient {

    private static LoginView loginView = null;
    private static User user = null;
    private static ChatView chatView = null;
    private static ServerConfigView serverConfigView = null;
    private static ViewQuestionView viewQuestionView = null;
    private PopupMenu popupMenu = null;
    public static MenuItem loginMenuItem = null;
    private MenuItem askMenuItem = null;
    private MenuItem questionMenuItem = null;
    private MenuItem vivaQuestionMenuItem = null;
    private MenuItem configMenuItem = null;
    private MenuItem aboutMenuItem = null;
    private MenuItem exitMenuItem = null;
    private SystemTray systemTray = null;
    private TrayIcon trayIcon = null;
    private Robot robot = null;
    private Toolkit toolKit = null;
    //Constructors......

    public ClientView() {
    }

    public void addToTray() throws AWTException {
        popupMenu = new PopupMenu();
        loginMenuItem = new MenuItem("Login");
        loginMenuItem.addActionListener(new ClientViewListener());
        popupMenu.add(loginMenuItem);
        askMenuItem = new MenuItem("Ask");
        askMenuItem.addActionListener(new ClientViewListener());
        popupMenu.add(askMenuItem);
        questionMenuItem = new MenuItem("View Question");
        questionMenuItem.addActionListener(new ClientViewListener());
        popupMenu.add(questionMenuItem);
        vivaQuestionMenuItem = new MenuItem("View Viva Question");
        vivaQuestionMenuItem.addActionListener(new ClientViewListener());
        popupMenu.add(vivaQuestionMenuItem);
        popupMenu.addSeparator();
        configMenuItem = new MenuItem("Configure");
        configMenuItem.addActionListener(new ClientViewListener());
        popupMenu.add(configMenuItem);
        aboutMenuItem = new MenuItem("About");
        aboutMenuItem.addActionListener(new ClientViewListener());
        popupMenu.add(aboutMenuItem);
        exitMenuItem = new MenuItem("Exit");
        exitMenuItem.addActionListener(new ClientViewListener());
        popupMenu.add(exitMenuItem);
        File file = new File(Configuration.class.getProtectionDomain().getCodeSource().getLocation().getFile());
        file = file.getParentFile();
        file = new File(file, "bulb.gif");
        trayIcon = new TrayIcon(new ImageIcon(file.getAbsolutePath(), "Lab Assessment System v1.0b").getImage());
        trayIcon.setPopupMenu(popupMenu);
        if (SystemTray.isSupported()) {
            systemTray = SystemTray.getSystemTray();
            systemTray.add(trayIcon);
            trayIcon.displayMessage("LAS Client 1.0", "running...", TrayIcon.MessageType.INFO);
        } else {
            JOptionPane.showMessageDialog(null, "SystemTray is not supported!", "System Tray is not supported !", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void requestChatClient(String lookUp) throws RemoteException {
        if (LasUtil.verifyThis(lookUp)) {
            System.out.println("Server look up is verified");
            if (getChatView() == null) {
                System.out.println("Client Chat view is null");
                ChatView tempChatView = new ChatView("Conversation", ClientView.getUser().getName());
                tempChatView.addToList(lookUp);
                try {
                    LasUtil.bindNow(ClientConfiguration.getInetAddress().getHostAddress(), ClientConfiguration.getPort(), tempChatView);
                } catch (UnknownHostException ex) {
                    Logger.getLogger(ClientView.class.getName()).log(Level.SEVERE, null, ex);
                }
                setChatView(tempChatView);
            } else {
                System.out.println("Client Chat view is not null");
                getChatView().addToList(lookUp);
            }
        } else {
            System.out.println("Server initiating chat, but not bound");
        }
    }

    @Override
    public byte[] captureScreen() throws RemoteException {
        try {
            toolKit = Toolkit.getDefaultToolkit();
            Rectangle rectangle = new Rectangle(toolKit.getScreenSize());
            robot = new Robot();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                ImageIO.write(robot.createScreenCapture(rectangle), "JPEG", bos);
            } catch (IOException ex) {
                Logger.getLogger(ClientView.class.getName()).log(Level.SEVERE, null, ex);
            }
            return bos.toByteArray();
//            return ImageUtility.toByteArray(robot.createScreenCapture(rectangle));
        } catch (AWTException ex) {
            Logger.getLogger(ClientView.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static ChatView getChatView() {
        return chatView;
    }

    public static void setChatView(ChatView chatView) {
        ClientView.chatView = chatView;
    }

    public static LoginView getLoginView() {
        return loginView;
    }

    public static void setLoginView(LoginView loginView) {
        ClientView.loginView = loginView;
    }

    public static User getUser() {
        if (user == null) {
            user = new User();
            try {
                user.setName(ClientConfiguration.getInetAddress().getHostName());
            } catch (UnknownHostException ex) {
                Logger.getLogger(ClientView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return user;
    }

    public static void setUser(User user) {
        ClientView.user = user;
    }

    public static ServerConfigView getServerConfigView() {
        if (serverConfigView == null) {
            serverConfigView = new ServerConfigView("Client - Lab Assessment v1.0");
        }
        return serverConfigView;
    }

    public static void setServerConfigView(ServerConfigView serverConfigView) {
        ClientView.serverConfigView = serverConfigView;
    }

    public static ViewQuestionView getViewQuestionView() {
        if (viewQuestionView == null) {
            viewQuestionView = new ViewQuestionView("View Questions - Lab Assessment v1.0");
        }
        return viewQuestionView;
    }

    public static void setViewQuestionView(ViewQuestionView viewQuestionView) {
        ClientView.viewQuestionView = viewQuestionView;
    }
}
