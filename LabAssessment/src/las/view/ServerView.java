/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package las.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import las.config.ClientConfiguration;
import las.config.MyConnection;
import las.config.ServerConfiguration;
import las.config.User;
import las.listener.ServerViewListener;
import las.rmi.LASServer;
import las.style.ButtonFont;
import las.util.LasUtil;

/**
 *
 * @author megha
 */
public class ServerView extends JFrame implements LASServer {

    private static ViewClientView client = null;
    private static ChatView chat = null;
    private static AddUserView addUser = null;
    private static AddQuestionView addQuestionView = null;
    private static JList clientList = null;
    private static ServerConfigView serverConfigView = null;
    private static DefaultListModel clientListModel = new DefaultListModel();
    private static User user = null;
    private JPanel mainPanel = null;
    private JPanel clientPanel = null;
    private JPanel taskPanel = null;
    private JScrollPane clientScrollPane = null;
    private JButton clientButton = null;
    private JButton chatButton = null;
    private JButton addQuestionButton = null;
    private JButton addUsers = null;
    private JButton currentBatch = null;
    private JButton marksButton = null;
    private JButton configButton = null;
    private JButton reportButton = null;
    private JButton aboutButton = null;
    private JButton vivaButton = null;
    private JButton exitButton = null;

    //Constructors...
    public ServerView(String title) throws HeadlessException {
        super(title);
    }

    public ServerView() throws HeadlessException {
    }

    public void createAndShow() {

        mainPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        clientPanel = new JPanel();
        clientPanel.setPreferredSize(new Dimension(400, 650));
        clientPanel.setBorder(new CompoundBorder(new TitledBorder("Clients"), new EmptyBorder(20, 20, 20, 20)));
        clientList = new JList(clientListModel);
        clientScrollPane = new JScrollPane(clientList);
        clientScrollPane.setPreferredSize(new Dimension(310, 570));
        clientPanel.add(clientScrollPane);
        mainPanel.add(clientPanel);
        taskPanel = new JPanel(new GridLayout(11, 1, 10, 10));
        taskPanel.setBorder(new CompoundBorder(new TitledBorder("Tasks"), new EmptyBorder(20, 20, 20, 20)));
        clientButton = new JButton("View");
        clientButton.setFont(ButtonFont.getButtonFont());
        clientButton.addActionListener(new ServerViewListener());
        taskPanel.add(clientButton);
        chatButton = new JButton("Chat");
        chatButton.setFont(ButtonFont.getButtonFont());
        chatButton.addActionListener(new ServerViewListener());
        taskPanel.add(chatButton);
        addQuestionButton = new JButton("Add Questions");
        addQuestionButton.setFont(ButtonFont.getButtonFont());
        addQuestionButton.addActionListener(new ServerViewListener());
        taskPanel.add(addQuestionButton);
        vivaButton = new JButton("Add Viva Questions");
        vivaButton.setFont(ButtonFont.getButtonFont());
        vivaButton.addActionListener(new ServerViewListener());
        taskPanel.add(vivaButton);
        addUsers = new JButton("Add Users");
        addUsers.setFont(ButtonFont.getButtonFont());
        addUsers.addActionListener(new ServerViewListener());
        taskPanel.add(addUsers);
        currentBatch = new JButton("Current Batch");
        currentBatch.setFont(ButtonFont.getButtonFont());
        currentBatch.addActionListener(new ServerViewListener());
        taskPanel.add(currentBatch);
        marksButton = new JButton("Finalize Marks");
        marksButton.setFont(ButtonFont.getButtonFont());
        marksButton.addActionListener(new ServerViewListener());
        taskPanel.add(marksButton);
        configButton = new JButton("Configuration");
        configButton.setFont(ButtonFont.getButtonFont());
        configButton.addActionListener(new ServerViewListener());
        taskPanel.add(configButton);
        reportButton = new JButton("Reports");
        reportButton.setFont(ButtonFont.getButtonFont());
        reportButton.addActionListener(new ServerViewListener());
        taskPanel.add(reportButton);
        aboutButton = new JButton("About");
        aboutButton.setFont(ButtonFont.getButtonFont());
        aboutButton.addActionListener(new ServerViewListener());
        taskPanel.add(aboutButton);
        exitButton = new JButton("Exit !");
        exitButton.setFont(ButtonFont.getButtonFont());
        exitButton.addActionListener(new ServerViewListener());
        taskPanel.add(exitButton);
        mainPanel.add(taskPanel);
        this.add(mainPanel);

        //JFrame method calls...
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new ServerViewListener());
        this.pack();
        this.setVisible(true);
        this.setResizable(false);
    }

    //RMI Exposed methods...
    @Override
    public void addToList(InetAddress s) throws RemoteException {
        clientListModel.addElement(s);
    }

    @Override
    public void removeFromList(InetAddress s) throws RemoteException {
        clientListModel.removeElement(s);
    }

    @Override
    public User isValid(User user) throws RemoteException {
        try {
            Connection connection = MyConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select name from las.info where usn = ? and password= ?");
            preparedStatement.setString(1, user.getUname());
            preparedStatement.setString(2, user.getPasswd());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user.setName(resultSet.getString(1));
                System.out.println("User Valid");
                return user;
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServerView.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Invalid User");
        return null;
    }

    @Override
    public String requestChatServer(InetAddress s) throws RemoteException {
        int i = JOptionPane.showConfirmDialog(null, "Request from " + s, "Chat Request", JOptionPane.OK_CANCEL_OPTION); // Prompt for permission
        if (i == JOptionPane.OK_OPTION) {   //If request accepted                                                                               
            if (LasUtil.verifyThis(s, ClientConfiguration.getPort())) {  //If client is already bound to RMI registry on port 2012
                ChatView chatView = new ChatView("Chat", user.getName());   //Create Chat Window 
                chatView.addToList(s);                      //Add the client to destination list
                //LookUp Generation....
                Random r = new Random();
                String lookUp = "Client" + r.nextInt(4);
                try {
                    //Bind the new Chat Window to RMI registry on server at port 2011
                    LasUtil.bindNow(ServerConfiguration.getInetAddress().getHostAddress(), ServerConfiguration.getPort(), lookUp, chatView);
                } catch (UnknownHostException ex) {
                    Logger.getLogger(ServerView.class.getName()).log(Level.SEVERE, null, ex);
                }
                return lookUp;  //If everything works fine, return the lookup to client
            } else {    //If Client is not bound to RMI registry on port 2012
                System.out.println("Client Requested, but not bound");
            }
        }
        return null;
    }

    //Accessor and Mutators....
    public static ChatView getChat() {
        return chat;
    }

    public static void setChat(ChatView chat) {
        ServerView.chat = chat;
    }

    public static ViewClientView getClient() {
        return client;
    }

    public static void setClient(ViewClientView client) {
        ServerView.client = client;
    }

    public static JList getClientList() {
        return clientList;
    }

    public static void setClientList(JList clientList) {
        ServerView.clientList = clientList;
    }

    public static DefaultListModel getClientListModel() {
        return clientListModel;
    }

    public static void setClientListModel(DefaultListModel clientListModel) {
        ServerView.clientListModel = clientListModel;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        ServerView.user = user;
    }

    public static ServerConfigView getServerConfigView() {
        if (serverConfigView == null) {
            serverConfigView = new ServerConfigView("Server Configuration");
        }
        return serverConfigView;
    }

    public static void setServerConfigView(ServerConfigView serverConfigView) {
        ServerView.serverConfigView = serverConfigView;
    }

    public static AddUserView getAddUser() {
        if (addUser == null) {
            addUser = new AddUserView("Add Users - Lab Assessment v1.0");
        }
        return addUser;
    }

    public static void setAddUser(AddUserView addUser) {
        ServerView.addUser = addUser;
    }

    public static AddQuestionView getAddQuestionView() {
        if (addQuestionView == null) {
            addQuestionView = new AddQuestionView("Add Questiions - Lab Assessment v1.0");
        }
        return addQuestionView;
    }

    public static void setAddQuestionView(AddQuestionView addQuestionView) {
        ServerView.addQuestionView = addQuestionView;
    }

    @Override
    public String getQuestions(String usn) throws RemoteException {
        String temp = "";
        try {
            System.out.println("USN : " + usn);
            Connection connection = MyConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select q.question from questions q, questionmap m where q.questionid=m.questionid and m.usn='" + usn + "'");
            while (resultSet.next()) {
                temp += resultSet.getRow() + ". " + resultSet.getString(1) + "";
            }
            if (temp.equals("")) {
                resultSet = statement.executeQuery("select ifnull(max(questionid),0), ifnull(min(questionid),0) from questions");
                resultSet.next();
                Random rand = new Random();
                int randomNum = rand.nextInt(resultSet.getInt(1) - resultSet.getInt(2) + 1) + resultSet.getInt(2);
                if (randomNum > 0) {
                    statement.executeUpdate("insert into questionmap values('" + usn + "'," + randomNum + ")");
                    System.out.println("New Questions defined");
                    resultSet = statement.executeQuery("select q.question from questions q, questionmap m where q.questionid=m.questionid and m.usn='" + usn + "'");
                    System.out.println("Findig the defined questions");
                    while (resultSet.next()) {
                        temp += resultSet.getRow() + ". " + resultSet.getString(1) + "";
                    }
                    System.out.println("Final Result : " + temp);
                    return temp;
                } else {
                    return temp;
                }
            } else {
                return temp;
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerView.class.getName()).log(Level.SEVERE, null, ex);
            return temp;
        } catch (SQLException ex) {
            Logger.getLogger(ServerView.class.getName()).log(Level.SEVERE, null, ex);
            return temp;
        }
    }

    @Override
    public ArrayList<String[]> getVivaQuestions(String usn) throws RemoteException {
        ArrayList<String[]> questions = new ArrayList<String[]>();
        try {
            boolean flag = true;
            Connection con = MyConnection.getConnection();
            Statement st = con.createStatement();
            Statement st1 = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT v.questionid, v.question FROM vivamap vm, viva v where usn = '" + usn + "' and v.questionid = vm.questionid");
            if (rs.next()) {
                String[] question = new String[2];
                question[0] = rs.getString(2);
                question[1] = rs.getString(1);
                questions.add(question);
                rs.next();
                question = new String[2];
                question[0] = rs.getString(2);
                question[1] = rs.getString(1);
                questions.add(question);
                rs.next();
                question = new String[2];
                question[0] = rs.getString(2);
                question[1] = rs.getString(1);
                questions.add(question);
                rs.next();
                question = new String[2];
                question[0] = rs.getString(2);
                question[1] = rs.getString(1);
                questions.add(question);
                rs.next();
                question = new String[2];
                question[0] = rs.getString(2);
                question[1] = rs.getString(1);
                questions.add(question);
                rs.next();
                question = new String[2];
                question[0] = rs.getString(2);
                question[1] = rs.getString(1);
                questions.add(question);
                rs.next();
                question = new String[2];
                question[0] = rs.getString(2);
                question[1] = rs.getString(1);
                questions.add(question);
                rs.next();
                question = new String[2];
                question[0] = rs.getString(2);
                question[1] = rs.getString(1);
                questions.add(question);
                rs.next();
                question = new String[2];
                question[0] = rs.getString(2);
                question[1] = rs.getString(1);
                questions.add(question);
                rs.next();
                question = new String[2];
                question[0] = rs.getString(2);
                question[1] = rs.getString(1);
                questions.add(question);
            } else {
                rs = st.executeQuery("select count(*) from viva");
                if (rs.next() && rs.getInt(1) >= 10) {
                    Random rd = new Random();
                    int questionarray[] = new int[10];
                    for (int i = 0; i < 10; ++i) {
//                        System.out.println("In Outer For");
                        while (true) {
                            flag = true;
//                            System.out.println("in While");
                            int n = rd.nextInt(rs.getInt(1));
//                            System.out.println("Number generated : " + n);
                            for (int j = 0; j < i; ++j) {
                                if (questionarray[j] == n) {
//                                    System.out.println("Number Exist");
                                    flag = false;
                                    break;
                                }
                            }
                            if (flag) {
//                                System.out.println("Unique Number : " + n);
                                questionarray[i] = n;
                                break;
                            }
                        }
                    }
//                    System.out.println("Random Questions ");
                    for (int j = 0; j < 10; ++j) {
//                        System.out.println("Question array[" + j + "] = " + questionarray[j]);
                        rs = st.executeQuery("select questionid from viva");
                        for (int i = 0; rs.next(); ++i) {
//                            System.out.println("I - Value : " + i);
                            if (questionarray[j] == i) {
//                                System.out.println("Match Found");
                                st1.executeUpdate("insert into vivamap(usn, questionid) values('" + usn + "'," + rs.getInt(1) + ")");
//                                System.out.println("Selected Question  = " + rs.getInt(1));
                            } else {
//                                System.out.println("No Match");
                            }
                        }
                    }
                    rs = st.executeQuery("SELECT v.questionid, v.question FROM vivamap vm, viva v where usn = '" + usn + "' and v.questionid = vm.questionid");
                    if (rs.next()) {
                        String[] question = new String[2];
                        question[0] = rs.getString(2);
                        question[1] = rs.getString(1);
                        questions.add(question);
                        rs.next();
                        question = new String[2];
                        question[0] = rs.getString(2);
                        question[1] = rs.getString(1);
                        questions.add(question);
                        rs.next();
                        question = new String[2];
                        question[0] = rs.getString(2);
                        question[1] = rs.getString(1);
                        questions.add(question);
                        rs.next();
                        question = new String[2];
                        question[0] = rs.getString(2);
                        question[1] = rs.getString(1);
                        questions.add(question);
                        rs.next();
                        question = new String[2];
                        question[0] = rs.getString(2);
                        question[1] = rs.getString(1);
                        questions.add(question);
                        rs.next();
                        question = new String[2];
                        question[0] = rs.getString(2);
                        question[1] = rs.getString(1);
                        questions.add(question);
                        rs.next();
                        question = new String[2];
                        question[0] = rs.getString(2);
                        question[1] = rs.getString(1);
                        questions.add(question);
                        rs.next();
                        question = new String[2];
                        question[0] = rs.getString(2);
                        question[1] = rs.getString(1);
                        questions.add(question);
                        rs.next();
                        question = new String[2];
                        question[0] = rs.getString(2);
                        question[1] = rs.getString(1);
                        questions.add(question);
                        rs.next();
                        question = new String[2];
                        question[0] = rs.getString(2);
                        question[1] = rs.getString(1);
                        questions.add(question);
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            }
            return questions;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ViewVivaQuestionView.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(ViewVivaQuestionView.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public boolean saveAnswer(String usn, int questionid, String ans) throws RemoteException {
        try {
            Connection con = MyConnection.getConnection();
            PreparedStatement ps = con.prepareCall("update vivamap set answer = ? where questionid = ? and usn = ?");
            ps.setString(1, ans);
            ps.setInt(2, questionid);
            ps.setString(3, usn);
            ps.executeUpdate();
            return true;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerView.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(ServerView.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}