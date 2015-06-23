package course;

import java.rmi.Remote;

// ClassRMI is an interface for communication with RMI server (used by TCP server)
public interface ClassRMI extends
        RemoteIF, // system-specific part (checking DB connection)
        History, // application-specific part (getHistory(), ...)
        
        Remote // required for RMI
{
}
