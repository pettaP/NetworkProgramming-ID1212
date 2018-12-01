package Common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MessagePasser extends Remote {

    void recvMsg(String msg) throws RemoteException;

    void printToFile(String msg) throws RemoteException;
}
