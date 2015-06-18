/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author samsung
 */
public class ClientTCP implements History {
    
 final static int DEFAULT_SERVER_PORT = 8888;
 public static Socket clSocket;
 private static boolean isLogged = false;
    
      public static void main (String[] args) throws Exception 
    {
       //StartClientTCP();
      
    } 

    
    @Override
    public String[] getHistory(String code) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String[] getHistory(int code) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean login(String user, String password) {
     
        
        StringBuilder sb = new StringBuilder();
        sb.append(user);
        sb.append(":");
        sb.append(password);
                    
     try {
         StartClientTCP(sb);
     } catch (Exception ex) {
         Logger.getLogger(ClientTCP.class.getName()).log(Level.SEVERE, null, ex);
     }
        return isLogged;
        
    }

    @Override
    public boolean logout() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
     public static void StartClientTCP(StringBuilder s) throws Exception
    {
        
        System.out.println("TCP Client side");
        
        // Создать экземпляр класса Socket;
        clSocket = new Socket("localhost", DEFAULT_SERVER_PORT);
             
        //Получить ссылки на входной и выходной потоки класса Socket;
        OutputStream out = clSocket.getOutputStream();
        InputStream in = clSocket.getInputStream();
        
        byte[] message = new byte[1024];
        out.write(s.toString().getBytes());
      //  int n = in.read ( message );
       // System.out.println(new String(message).trim());
        
    
        

       
    }
    
     public static void ShutdownClient() throws IOException
     {
        clSocket.shutdownInput ();
        clSocket.shutdownOutput ();
        clSocket.close();
     }
     
    

}
