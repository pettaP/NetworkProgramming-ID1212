package Common;

import Server.Model.Client;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface FileCatalog extends Remote {

    public UserDTO loginUser(String userName, String userPwd, MessagePasser messagePasser) throws RemoteException;

    public void registerUser (String userName, String userPwd, MessagePasser messagePasser) throws RemoteException;

    public void removeUser(UserDTO userDTO) throws RemoteException;

    public void listAllFiles(UserDTO userDTO) throws RemoteException;

    public void getFile(FileDTO fileDTO, UserDTO userDTO) throws RemoteException, SQLException;

    public void uploadFile(FileDTO fileDTO, UserDTO userDTO) throws RemoteException;

    public void deleteFile(FileDTO fileDTO, UserDTO userDTO) throws RemoteException, SQLException;

    public void downloadFile(FileDTO fileDTO, UserDTO userDTO) throws RemoteException, SQLException;
}
