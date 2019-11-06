/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package las.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author megha
 */
public class MyConnection {

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/las", "root", "admin");
    }
}
