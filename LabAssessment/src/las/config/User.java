/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package las.config;

import java.io.Serializable;

/**
 *
 * @author megha
 */
public class User implements Serializable {

    private String uname;
    private String passwd;
    private String name;

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User(String uname, String passwd) {
        this.uname = uname;
        this.passwd = passwd;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }
}
