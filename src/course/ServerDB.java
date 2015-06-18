/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.rmi.Remote;
import java.util.Arrays;

/**
 *
 * @author samsung
 */
public class ServerDB extends UnicastRemoteObject implements RemoteIF, History {

    private static final String BINDING_NAME = "CompanyDBService";
    private static ServerDB server;

    private ServerDB() throws RemoteException {
    }

    public static ServerDB Instance() throws RemoteException {
        if (server == null) {
            server = new ServerDB();
        }
        return server;
    }

    public static final String url = "jdbc:derby://localhost:1527/CompanyDB";
    private Connection connectionDB = null;
    //private Statement stmt = null;  //prepared statement
    //private DatabaseMetaData dmd = null;
    //private ResultSet rs = null;

    public static String result;

    public static void main(String[] args) throws SQLException, RemoteException, InterruptedException, AlreadyBoundException, Exception {
        //StartRMIServ();
        Instance().connectDB();
        //dropAllTables();
        //createTablesCompanyDB();
        //closeConnectionDB();
      /*
         String [] s = Instance().getHistory("Malevich");
         System.out.println("getHistroy using String parameter");
         for (int i = 0; i < s.length; i++) {
         System.out.print(s[i] + ", ");
         }
         */

        int cde = 1;
        String[] cd = Instance().getHistory(cde);
        System.out.println("getHistroy using int parameter");
        for (int i = 0; i < cd.length; i++) {
            System.out.print(cd[i] + ", ");
        }

        boolean check = Instance().login("kmalev", "4321");
        System.out.println("/n login method /n");
        System.out.println("" + check);

    }

    public void connectDB() throws Exception {
        try {
            connectionDB = DriverManager.getConnection(url);
        } catch (Exception ex) {
            System.out.println("Connection failed" + ex.getMessage());
            throw ex;
        }
        System.out.println("Connection to CompanyDB created");
    }

    public void closeConnectionDB() throws SQLException {
        connectionDB.close();
        connectionDB = null;
        System.out.println("Connection to CompanyDB closed");
    }

    public void createTablesCompanyDB() throws SQLException {
        String employeesTable = " CREATE TABLE employees "
                + " (code INTEGER CONSTRAINT emp_employee_id PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)"
                + ", name VARCHAR(24) CONSTRAINT emp_name_nn NOT NULL "
                + ", last_name VARCHAR(32) CONSTRAINT emp_ln_nn NOT NULL "
                + ", login VARCHAR(16) CONSTRAINT emp_log_nn NOT NULL "
                + ", psw VARCHAR(16) ) ";

        String employeeHistTable = "CREATE TABLE EmployeeHistory "
                + "(id INTEGER CONSTRAINT eh_empl_id PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)"
                + ", position VARCHAR(24) CONSTRAINT eh_pos_nn NOT NULL "
                + ", manager INTEGER CONSTRAINT emp_man_ck CHECK (manager>0) "
                + ", hire DATE CONSTRAINT eh_hire_nn NOT NULL "
                + ", dismiss DATE "
                + ", code INTEGER CONSTRAINT eh_code_fk REFERENCES employees (code) "
                + ", CONSTRAINT eh_dismiss_ck CHECK (dismiss >= hire) ) ";

        Statement stmt;
        try {
            stmt = connectionDB.createStatement();
        } catch (SQLException ex) {
            System.out.println("Statement not created" + ex.getMessage());
            throw ex;
        }

        if (!checkIfTableExists("EMPLOYEES")) {
            stmt.executeUpdate(employeesTable);
            System.out.println("table Employees created");
        }

        if (!checkIfTableExists("EMPLOYEEHISTORY")) {
            stmt.executeUpdate(employeeHistTable);
            System.out.println("table EmployeeHistory created");
        }

        stmt.close();
    }

    public boolean checkIfTableExists(final String tn) throws SQLException {
        DatabaseMetaData dmd;
        try {
            dmd = connectionDB.getMetaData();
        } catch (SQLException ex) {
            System.out.println("Can't get metadata" + ex.getMessage());
            throw ex;
        }
        ResultSet rs = dmd.getTables(null, null, tn, null);
        final boolean res = rs.next();
        rs.close();
        return res;
    }

    public void dropAllTables() throws SQLException {
        String dropEmp = "DROP TABLE EMPLOYEES";
        String dropEmpHist = "DROP TABLE EMPLOYEEHISTORY";
        Statement stmt = connectionDB.createStatement();
        if (checkIfTableExists("EMPLOYEES")) {
            stmt.executeUpdate(dropEmpHist);
        }
        if (checkIfTableExists("EMPLOYEESHISTORY")) {
            stmt.executeUpdate(dropEmp);
        }
        stmt.close();
    }
    /*
     public static String getHistory (String surname) throws SQLException{
     String qHistory = "SELECT e.name, e.last_name, eh.manager, eh.position, eh.hire, eh.dismiss FROM employees e JOIN employeehistory eh ON e.code = eh.code WHERE e.last_name =" + "'" + surname + "'";
     String resultString = null;
     stmt = connectionDB.createStatement();
     ResultSet rslt = stmt.executeQuery(qHistory);
     ResultSetMetaData rsmd = rslt.getMetaData();
     int numberOfColumns = rsmd.getColumnCount();
     while (rslt.next()){
     for (int i = 1; i <= numberOfColumns; i++) {
     if (i > 1) System.out.print(" ");
     resultString += rslt.getString(i);
     }      
     }   
     return resultString;
     }
     */
    /*  
     public static String getHistory (int code) throws SQLException
     {
     String qHistoryCode = "SELECT e.name, e.last_name, eh.manager, eh.position, eh.hire, eh.dismiss FROM employees e JOIN employeehistory eh ON e.code = eh.code WHERE e.code =" + "'" + code + "'";
     stmt = connectionDB.createStatement();
     ResultSet rslt = stmt.executeQuery(qHistoryCode);
     String resultString = null;
     ResultSetMetaData rsmd = rslt.getMetaData();
     int numberOfColumns = rsmd.getColumnCount();
     while (rslt.next()){
     for (int i = 1; i <= numberOfColumns; i++) {
     if (i > 1) System.out.print(" ");
     resultString += rslt.getString(i);
     }      
     }   
     return resultString;
     }
     */

    /*
     public static boolean Login(String l, String p) throws SQLException
     {
     String qLogin = "SELECT 1 FROM employees WHERE login = '" + l + "' AND psw = '" + p + "'";
     stmt = connectionDB.createStatement();
     ResultSet rslt = stmt.executeQuery(qLogin);
     if (rslt.next()){ 
     return true;
     }
     return false;
    
     }
     */
    public static void StartRMIServ() throws RemoteException, InterruptedException, AlreadyBoundException {
        try {
            System.out.print("Registry...");
            final Registry registry = LocateRegistry.createRegistry(2222);
            System.out.println("OK");
            ServerDB service = ServerDB.Instance();
            //Remote stub = UnicastRemoteObject.exportObject(service, 0);
            System.out.println("Binding...");
            registry.rebind(BINDING_NAME, service);
            System.out.println("OK");
        } catch (Exception ex) {
            // XXX
            throw ex;
        }

        /*
         while (true) 
         {
        
         Thread.sleep(Integer.MAX_VALUE);
		     
         }
         */
    }

    @Override
    public void CheckConnection() throws RemoteException {
        System.out.println("rmi ok");

    }

    @Override
    public String[] getHistory(String surname) throws SQLException {
        String qHistory = "SELECT e.name, e.last_name, eh.manager, eh.position, eh.hire, eh.dismiss FROM employees e JOIN employeehistory eh ON e.code = eh.code WHERE e.last_name =" + "'" + surname + "'";
        String[] resArray = new String[10];
        Statement stmt = connectionDB.createStatement();
        ResultSet rslt = stmt.executeQuery(qHistory);
        ResultSetMetaData rsmd = rslt.getMetaData();
        int numberOfColumns = rsmd.getColumnCount();
        while (rslt.next()) {
            for (int i = 1; i <= numberOfColumns; i++) {
                if (i > 1) {
                    System.out.print(" ");
                }
                resArray[i] = rslt.getString(i);
            }
        }
        return resArray;
    }

    @Override
    public String[] getHistory(int code) throws SQLException {
        String qHistoryCode = "SELECT e.name, e.last_name, eh.manager, eh.position, eh.hire, eh.dismiss FROM employees e JOIN employeehistory eh ON e.code = eh.code WHERE e.code =" + code;
        Statement stmt = connectionDB.createStatement();
        ResultSet rslt = stmt.executeQuery(qHistoryCode);
        String[] resArray = new String[10];
        ResultSetMetaData rsmd = rslt.getMetaData();
        int numberOfColumns = rsmd.getColumnCount();
        while (rslt.next()) {
            for (int i = 1; i <= numberOfColumns; i++) {
                if (i > 1) {
                    System.out.print(" ");
                }
                resArray[i] = rslt.getString(i);
            }
        }
        return resArray;
    }

    @Override
    public boolean login(String user, String password) throws SQLException {
        String qLogin = "SELECT 1 FROM employees WHERE login = '" + user + "' AND psw = '" + password + "'";
        Statement stmt = connectionDB.createStatement();
        ResultSet rslt = stmt.executeQuery(qLogin);
        if (rslt.next()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean logout() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
