/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package las.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicBorders.FieldBorder;
import las.config.ClientConfiguration;
import las.config.ServerConfiguration;
import las.listener.ChatViewListener;
import las.rmi.LASChat;
import las.style.ButtonFont;

/**
 *
 * @author megha
 */
public class ChatView extends JFrame implements LASChat, ActionListener, Serializable {

    private ArrayList<LASChat> dest = null;
    private String chatOwner = null;
    private String lookUp = null;
    private JPanel mainPanel = null;
    private JPanel textPanel = null;
    private JPanel inputPanel = null;
    private JScrollPane textScrollPane = null;
    private JScrollPane inputScrollPane = null;
    private JTextArea chatTextArea = null;
    private JTextField chatInput = null;
    private JButton sendButton = null;

    //Constructors....
    public ChatView(String title, String owner) throws HeadlessException {
        super(title);
        this.chatOwner = owner;
    }

    public ChatView() throws HeadlessException {
    }

    public void showChatView() {
        mainPanel = new JPanel();
        textPanel = new JPanel();
        textPanel.setBorder(new CompoundBorder(new EmptyBorder(20, 20, 20, 20), new TitledBorder("Conversation")));
        chatTextArea = new JTextArea(null, 15, 40);
        chatTextArea.setEditable(false);
        textScrollPane = new JScrollPane(chatTextArea);
        textPanel.add(textScrollPane);
        inputPanel = new JPanel();
        inputPanel.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10), new FieldBorder(Color.yellow, Color.darkGray, Color.lightGray, Color.lightGray)));
        chatInput = new JTextField(30);
        chatInput.setPreferredSize(new Dimension(300, 50));
        inputScrollPane = new JScrollPane(chatInput, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        inputPanel.add(inputScrollPane);
        sendButton = new JButton("Send");
        sendButton.setPreferredSize(new Dimension(100, 50));
        sendButton.setFont(ButtonFont.getButtonFont());
        sendButton.addActionListener(this);
        inputPanel.add(sendButton);
        mainPanel.add(textPanel);
        mainPanel.add(inputPanel);
        mainPanel.setPreferredSize(new Dimension(500, 420));
        this.add(mainPanel);

        //JFrame method calls....
        this.getRootPane().setDefaultButton(sendButton);
        this.pack();
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setVisible(true);
        this.addWindowListener(new ChatViewListener());
        this.setResizable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            for (LASChat c : dest) {
                c.addText(chatInput.getText(), chatOwner);
            }
            addText(chatInput.getText(), chatOwner);
            chatInput.setText("");
        } catch (RemoteException ex) {
            Logger.getLogger(ChatView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<LASChat> getDest() {
        return dest;
    }

    public void setDest(ArrayList<LASChat> dest) {
        this.dest = dest;
    }

    public JTextField getChatInput() {
        return chatInput;
    }

    public void setChatInput(JTextField chatInput) {
        this.chatInput = chatInput;
    }

    public JTextArea getChatTextArea() {
        return chatTextArea;
    }

    public void setChatTextArea(JTextArea chatTextArea) {
        this.chatTextArea = chatTextArea;
    }

    @Override
    public String getLookUp() {
        return lookUp;
    }

    public void setLookUp(String lookUp) {
        this.lookUp = lookUp;
    }

    @Override
    public void addText(String s, String user) throws RemoteException {
        if (chatTextArea == null && s.contains("has left the chat")) {
            this.dispose();
            return;
        } else if (!isVisible()) {
            showChatView();
        }
        String t = chatTextArea.getText();
        chatTextArea.setText(t + "\n" + user + " : " + s);
    }

    @Override
    public void removeFromList(String lookUp, String user) throws RemoteException {
        LASChat rLASChat = null;
        addText("has left the chat", user);
        for (LASChat lASChat : dest) {
            if (lASChat.getLookUp().equals(lookUp)) {
                rLASChat = lASChat;
            }
        }
        if (rLASChat != null) {
            dest.remove(rLASChat);
            System.out.println("Removed from list");
        }
    }

    public void addToList(InetAddress s) {
        if (dest == null) {
            dest = new ArrayList<LASChat>();
        }
        try {
            Registry registry = LocateRegistry.getRegistry(s.getHostAddress(), ClientConfiguration.getPort());
            LASChat chat = (LASChat) registry.lookup("Client");
            dest.add(chat);
            System.out.println("Added Client to List");
        } catch (NotBoundException ex) {
            Logger.getLogger(ChatView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(ChatView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void addToList(String lookUp) {
        if (dest == null) {
            dest = new ArrayList<LASChat>();
        }
        try {
            Registry registry = LocateRegistry.getRegistry(ServerConfiguration.getInetAddress().getHostAddress(), ServerConfiguration.getPort());
            LASChat chat = (LASChat) registry.lookup(lookUp);
            dest.add(chat);
            System.out.println("Added Server to List");
        } catch (UnknownHostException ex) {
            Logger.getLogger(ChatView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(ChatView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(ChatView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getChatOwner() {
        return chatOwner;
    }

    public void setChatOwner(String chatOwner) {
        this.chatOwner = chatOwner;
    }
}
