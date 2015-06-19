/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author samsung
 */
public interface History {
   ArrayList<String> getHistory (String surname) throws SQLException;
   ArrayList<String> getHistory (int code) throws SQLException;
   boolean login (String user, String password) throws SQLException;
   boolean logout ();
}
