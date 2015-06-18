/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course;


import static course.ClassRMI.BINDING_NAME;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author samsung
 */
public class ServerTCP {
    
    public static int DEFAULT_SERVER_PORT = 8888;
    public static int DEFAULT_SERVER_CLIENTS_NUMBER = 5555;
    public static ServerSocket serverSocket;
    
    
     public static void main (String[] args) throws RemoteException, InterruptedException, NotBoundException, IOException 
    {
      StartClientRMI();
      //StartServerTCP();
    } 
    
    //ClientRMI
        public static void StartClientRMI() throws RemoteException, NotBoundException, InterruptedException
     {
     
        Registry registry = LocateRegistry.getRegistry("localhost", 2222);
	RemoteIF service = (RemoteIF)registry.lookup(BINDING_NAME);
        service.CheckConnection();
        //RemoteIF serv =  new ClassRMI();
        //System.out.println(serv.CheckConnection());
         
     }
        private static void StartServerTCP(Socket clientSocket)
        {
            try
            {
              System.out.println("Server side");
        //Создать экземпляр класса ServerSocket;
        serverSocket = new ServerSocket(DEFAULT_SERVER_PORT, DEFAULT_SERVER_CLIENTS_NUMBER);
        //Получить ссылку на экземпляр класса Socket с помощью метода accept();
        Socket clsSocket = serverSocket.accept();
        InputStream in =  clsSocket.getInputStream();
  	OutputStream out = clsSocket.getOutputStream();
        byte[] message = new byte[1024];
        int n = in.read(message);
        String clRequest = new String(message).trim();
        
            
            
          
                try
                {

                }
                finally
                {
                    clientSocket.close();
                }
            }
            catch (IOException ex) {
                    Logger.getLogger(ServerTCP.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
    
   
    
}
