package course;

import java.rmi.RemoteException;
import java.util.ArrayList;

// High-level interface for application
public interface History {
   ArrayList<String> getHistory (String surname) throws RemoteException;
   ArrayList<String> getHistory (int code) throws RemoteException;
   boolean login (String user, String password) throws RemoteException;
   boolean logout () throws RemoteException;
}
