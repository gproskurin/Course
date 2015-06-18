/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course;

import java.io.Serializable;

/**
 *
 * @author samsung
 */
public class Data implements Serializable {
    String login;
    String password;
    
    public Data(String l, String p)
    {
        login = l;
        password = p;
    }
}
