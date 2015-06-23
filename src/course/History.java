package course;

import java.rmi.RemoteException;
import java.util.ArrayList;

// High-level interface for application
// ArrayList is easier to serialize, so use it instead of String[]
public interface History {
   ArrayList<String> getHistory (String surname) throws RemoteException; // encoded as "getHistoryByName" in TCP
   ArrayList<String> getHistory (int code) throws RemoteException; // "getHistoryByCode"
   boolean login (String user, String password) throws RemoteException; // "login"
   boolean logout () throws RemoteException; // "logout"
}
