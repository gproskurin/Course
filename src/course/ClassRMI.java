/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course;

import java.io.Serializable;
import java.rmi.RemoteException;

/**
 *
 * @author samsung
 */
public class ClassRMI implements RemoteIF, Serializable {
  
    public static final String BINDING_NAME = "CompanyDBService";

    public ClassRMI() throws RemoteException {
        super();

    }
    
    
    @Override
    public void CheckConnection() throws RemoteException {
        String response = "Hello from server";
       
    }
/*
    @Override
    public String UserInfo() throws RemoteException {
      String data;
      
      return data;
    }
 */
}
