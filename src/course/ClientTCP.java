/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author samsung
 */
public class ClientTCP implements History {

    private final static int DEFAULT_SERVER_PORT = 8888;
    private Socket clSocket;
    private ObjectOutputStream outStream;
    private ObjectInputStream inStream;

    public static void main(String[] args) throws Exception {
        ClientTCP ct = new ClientTCP();
        ct.StartClientTCP();
    }

    @Override
    public ArrayList<String> getHistory(String code) {
        try {
            ArrayList<String> req = new ArrayList<String>();
            req.add("getHistoryString");
            req.add(code);
            ArrayList<String> resp = sendRequestGetResponse(req);
            return resp;
        } catch (Exception ex) {
            System.out.println("Exception in getHistoryString(): " + ex);
            return null;
        }
    }

    @Override
    public ArrayList<String> getHistory(int code) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean login(String user, String password) {
        try {
            ArrayList<String> req = new ArrayList<String>();
            req.add("login");
            req.add(user);
            req.add(password);
            ArrayList<String> resp = sendRequestGetResponse(req);
            boolean result = (resp.get(0) == "ok");
            return result;
        } catch (Exception ex) {
            // In case of error, just return false
            System.out.println("Exception in login(): " + ex);
            return false;
        }
    }

    @Override
    public boolean logout() {
        try {
            ArrayList<String> req = new ArrayList<String>();
            req.add("logout");
            ArrayList<String> resp = sendRequestGetResponse(req);
            boolean result = (resp.get(0) == "ok");
            return result;
        } catch (Exception ex) {
            // In case of error, just return false
            System.out.println("Exception in logout(): " + ex);
            return false;
        }
    }

    private ArrayList<String> sendRequestGetResponse(ArrayList<String> request) throws IOException, ClassNotFoundException {
        outStream.writeObject(request);
        ArrayList<String> resp = (ArrayList<String>) inStream.readObject();
        System.out.println("Client got response: " + resp);
        return resp;
    }

    private void StartClientTCP() throws Exception {

        System.out.println("TCP Client side");

        // Создать экземпляр класса Socket;
        clSocket = new Socket("localhost", DEFAULT_SERVER_PORT);

        outStream = new ObjectOutputStream(clSocket.getOutputStream());
        inStream = new ObjectInputStream(clSocket.getInputStream());

        boolean l1 = login("user1", "passwd1");
        boolean l2 = login("user1", "passwd_11");
        ArrayList<String> s = getHistory("user3");
    }

    /*
     public static void ShutdownClient() throws IOException {
     clSocket.shutdownInput();
     clSocket.shutdownOutput();
     clSocket.close();
     }
     */
}
