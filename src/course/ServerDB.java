package course;

import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ServerDB extends UnicastRemoteObject implements ClassRMI {

    public static final String BINDING_NAME = "CompanyDBService";

    private static final String url = "jdbc:derby://localhost:1527/CompanyDB";
    private static ServerDB server = null;
    private Connection connectionDB = null;
    
    // this class is created by Instance() function, so constructor is private
    private ServerDB() throws RemoteException {
    }

    // singleton pattern
    public static ServerDB Instance() throws RemoteException {
        // first call to this function creates object
        if (server == null) {
            server = new ServerDB();
        }
        return server;
    }

    public static void main(String[] args) throws SQLException, RemoteException, InterruptedException, AlreadyBoundException, Exception {
        Instance().connectDB();
        //dropAllTables();
        //createTablesCompanyDB();
        Instance().StartRMIServ();
    }

    private void connectDB() throws Exception {
        try {
            connectionDB = DriverManager.getConnection(url);
        } catch (Exception ex) {
            System.out.println("Connection failed: " + ex.getMessage());
            throw ex;
        }
        System.out.println("Connection to CompanyDB created");
    }

    public void closeConnectionDB() throws SQLException {
        connectionDB.close();
        connectionDB = null;
        System.out.println("Connection to CompanyDB closed");
    }

    private void StartRMIServ() throws RemoteException, InterruptedException, AlreadyBoundException, Exception {
        ServerDB service = ServerDB.Instance();
        Naming.rebind(BINDING_NAME, service);
        System.out.println("StartRMIServ(): OK");
    }

    // Override function from RemoteIF
    @Override
    public void CheckConnection() throws RemoteException {
        System.out.println("rmi ok");
    }

    // Execute SQL query passed by argument, return array (ArrayList) of rows. Each row is concatenation of all its columns, separated by space
    // TODO (optimization): prepare statements once at start instead of preparing them for each query
    private ArrayList<String> executeQuery(String q) {
        try {
            Statement stmt = connectionDB.createStatement();
            ResultSet rslt = stmt.executeQuery(q);
            ResultSetMetaData rsmd = rslt.getMetaData();
            final int numberOfColumns = rsmd.getColumnCount();
            ArrayList<String> resArray = new ArrayList<>();
            while (rslt.next()) { // for each row of result
                String row = new String();
                
                //concatenate all columns, separate them by space
                for (int i = 1; i <= numberOfColumns; i++) {
                    if (i > 1) {
                        row += " ";
                    }
                    row += rslt.getString(i);
                }
                
                resArray.add(row);
            }
            return resArray;
        } catch (Exception ex) {
            System.out.println("Exception in executeQuery: " + ex);
            return null;
        }
    }

    @Override
    public ArrayList<String> getHistory(String surname) throws RemoteException {
        System.out.println("ServerDB: getHistory(String '"+surname+"')");
        final String qHistory = "SELECT e.name, e.last_name, eh.manager, eh.position, eh.hire, eh.dismiss FROM employees e JOIN employeehistory eh ON e.code = eh.code WHERE e.last_name =" + "'" + surname + "'";
        return executeQuery(qHistory);
    }

    @Override
    public ArrayList<String> getHistory(int code) throws RemoteException {
        System.out.println("ServerDB: getHistory(int "+code+")");
        final String qHistoryCode = "SELECT e.name, e.last_name, eh.manager, eh.position, eh.hire, eh.dismiss FROM employees e JOIN employeehistory eh ON e.code = eh.code WHERE e.code =" + code;
        return executeQuery(qHistoryCode);
    }

    @Override
    public boolean login(String user, String password) throws RemoteException {
        System.out.println("ServerDB: login('"+user+"','"+password+"')");
        try {
            String qLogin = "SELECT 1 FROM employees WHERE login = '" + user + "' AND psw = '" + password + "'";
            Statement stmt = connectionDB.createStatement();
            ResultSet rslt = stmt.executeQuery(qLogin);
            return (rslt.next()); // non-empty result of query means success
        } catch (Exception ex) {
            System.out.println("Exception in login(): " + ex);
            return false; // if some error occurs, return false
        }
    }

    @Override
    public boolean logout() throws RemoteException {
        System.out.println("ServerDB: logout");
        return true;
    }
    
    // unused
    /*
    private void createTablesCompanyDB() throws SQLException {
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
    */

    // unused
    /*
    private boolean checkIfTableExists(final String tn) throws SQLException {
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
    */

    // unused
    /*
    private void dropAllTables() throws SQLException {
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
    */
}
