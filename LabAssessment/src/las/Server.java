package las;

import las.view.LoginView;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author megha
 */
public class Server {

    public static void main(String args[]) {
        try {
                LoginView loginView = new LoginView("Server Login");
//                System.out.println("Server Running");
            loginView.showLoginView();
        } catch (Exception e) {
            System.out.println("Error : " + e);
            System.exit(1);
        }
    }
}
