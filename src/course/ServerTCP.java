/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course;

import static course.ClassRMI.BINDING_NAME;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author samsung
 */
public class ServerTCP {

    public static final int DEFAULT_SERVER_PORT = 8888;
    private ServerSocket serverSocket;
    private RemoteIF rmiDbServer;

    public static void main(String[] args) throws RemoteException, InterruptedException, NotBoundException, IOException, ClassNotFoundException {
        ServerTCP st = new ServerTCP();
        st.StartClientRMI();
        st.StartServerTCP();
    }

    //ClientRMI
    public void StartClientRMI() throws RemoteException, NotBoundException, InterruptedException {
        System.out.println("StartClientRMI(): BEGIN");
        //Registry registry = LocateRegistry.getRegistry("localhost", 2222);
        Registry registry = LocateRegistry.getRegistry();
        rmiDbServer = (RemoteIF) registry.lookup(BINDING_NAME);
        rmiDbServer.CheckConnection();
        System.out.println("StartClientRMI(): OK");
    }

    private void StartServerTCP() throws IOException {
        System.out.println("StartServerTCP(): BEGIN");
        ServerSocket serverSocket = new ServerSocket(DEFAULT_SERVER_PORT);
        while (true) {
            try {
                Socket clSocket = serverSocket.accept();
                ServeOneSocketThread t = new ServeOneSocketThread(clSocket);
                t.start();
            } catch (Exception ex) {
                System.out.println("Exception in TCP Server (ignored): " + ex);
            }
        }
    }

    class ServeOneSocketThread extends Thread {

        private final Socket socket;
        private boolean logged_in = false;

        ServeOneSocketThread(Socket s) {
            socket = s;
        }

        @Override
        public void run() {
            try {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                while (true) {
                    ArrayList<String> request = (ArrayList<String>) in.readObject();
                    System.out.println("Request: " + request);

                    ArrayList<String> response = new ArrayList<String>();

                    final String cmd = request.get(0);
                    switch (cmd) {
                        case "login":
                            if (logged_in) {
                                response.add("fail_already_logged_in");
                            } else {
                                response.add("ok");
                                logged_in = true;
                            }
                            break;
                        case "logout":
                            response.add("ok");
                            logged_in = false;
                            break;
                        case "getHistoryString":
                            response.add("qwe er sf");
                            response.add("xfkgs sgsdg sdg");
                            break;
                        default:
                            response.add("unrecognized_command");
                    }
                    out.writeObject(response);
                }
            } catch (EOFException ex) {
                System.out.println("ServerTCP: client disconnected");
            } catch (Exception ex) {
                System.out.println("Exception in ServeOneSocketThread.run() (ignored): " + ex);
            }
        }
    }

}
