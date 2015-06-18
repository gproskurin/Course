/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author samsung
 */
public interface RemoteIF extends Remote {
    
     void CheckConnection () throws RemoteException;
     //String UserInfo() throws RemoteException;
}
