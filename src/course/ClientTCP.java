package course;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

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
    public ArrayList<String> getHistory(String name) {
        try {
            ArrayList<String> req = new ArrayList<>();
            req.add("getHistoryByName");
            req.add(name);
            ArrayList<String> resp = sendRequestGetResponse(req);
            return resp;
        } catch (Exception ex) {
            System.out.println("Exception in getHistoryString(): " + ex);
            return null;
        }
    }

    @Override
    public ArrayList<String> getHistory(int code) {
        try {
            ArrayList<String> req = new ArrayList<>();
            req.add("getHistoryByCode");
            req.add(Integer.toString(code));
            ArrayList<String> resp = sendRequestGetResponse(req);
            return resp;
        } catch (Exception ex) {
            System.out.println("Exception in getHistory(int): " + ex);
            return null;
        }
    }

    @Override
    public boolean login(String user, String password) {
        try {
            ArrayList<String> req = new ArrayList<>();
            req.add("login");
            req.add(user);
            req.add(password);
            ArrayList<String> resp = sendRequestGetResponse(req);
            boolean result = (resp.get(0) == "ok"); // "ok" in first element indicates success
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
            ArrayList<String> req = new ArrayList<>();
            req.add("logout");
            ArrayList<String> resp = sendRequestGetResponse(req);
            boolean result = (resp.get(0) == "ok"); // "ok" in first element indicates success
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

        clSocket = new Socket("localhost", DEFAULT_SERVER_PORT);

        outStream = new ObjectOutputStream(clSocket.getOutputStream());
        inStream = new ObjectInputStream(clSocket.getInputStream());

        System.out.println("*** Login 1.1: " + login("vp", "passw1"));
        System.out.println("*** Login 1.2: " + logout());

        System.out.println("*** Login 2.1: " + login("user1", "passwd_11"));
        
        System.out.println("*** Login 3.1: " + login("pv", "incorrect_passw2"));
        System.out.println("*** Login 3.2: " + login("pv", "passw2"));
        
        System.out.println("*** History 1: " + getHistory(123));
        System.out.println("*** History 2: " + getHistory(124));
        System.out.println("*** History 3: " + getHistory(125));
        System.out.println("*** History 4: " + getHistory("Marks")); // all Markses
    }

}
