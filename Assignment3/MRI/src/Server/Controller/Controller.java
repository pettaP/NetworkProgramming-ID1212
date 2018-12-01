package Server.Controller;

import Common.FileCatalog;
import Common.FileDTO;
import Common.MessagePasser;
import Common.UserDTO;
import Server.Model.Client;
import Server.Model.SQLMessage;
import Server.Integration.DBHandler;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Controller extends UnicastRemoteObject implements FileCatalog {

    private final DBHandler dbHandler;
    private final SQLMessage sqlMessage;
    private final Map<Integer, Client> loggedInUsers = Collections.synchronizedMap(new HashMap<>());

    public Controller() throws RemoteException{
        this.dbHandler = new DBHandler();
        this.sqlMessage = new SQLMessage();
    }

    @Override
    public synchronized UserDTO loginUser(String userName, String userPwd, MessagePasser messagePasser) {
        int userId = 0;
        Client client = new Client(userName, userPwd, messagePasser);
        userId = dbHandler.logInUser(client);
        if(userId != 0) {
            client.setUserId(userId);
            loggedInUsers.put(client.getUserId(), client);
            return client;
        }
        return null;
    }

    @Override
    public synchronized void registerUser (String userName, String userPwd, MessagePasser messagePasser) throws RemoteException{
            Client client = new Client(userName, userPwd, messagePasser);
            client.getOutputToUser().recvMsg(dbHandler.registerUser(client));
    }

    @Override
    public synchronized void removeUser(UserDTO userDTO) throws RemoteException{
        Client client = loggedInUsers.get(userDTO.getUserId());
        String userName = loggedInUsers.get(client.getUserId()).getUserName();
        loggedInUsers.get(client.getUserId()).getOutputToUser().recvMsg("LOGOUT#" + userName);
        loggedInUsers.remove(client.getUserId());
    }

    @Override
    public synchronized void listAllFiles(UserDTO userDTO) throws RemoteException {
        Client client = loggedInUsers.get(userDTO.getUserId());
        loggedInUsers.get(client.getUserId()).getOutputToUser().recvMsg(dbHandler.listAllFiles());
    }

    @Override
    public synchronized void getFile(FileDTO fileDTO, UserDTO userDTO) throws RemoteException, SQLException {
        Client client = loggedInUsers.get(userDTO.getUserId());
        int fileOwner = dbHandler.getOwner(fileDTO);
        String response = dbHandler.getFile(fileDTO);
        if(sqlMessage.isValidSQLMessage(response)){
            if(fileOwner != client.getUserId()) {
                if(loggedInUsers.get(fileOwner) != null) {
                    loggedInUsers.get(fileOwner).getOutputToUser().recvMsg("ACCESS#"+fileDTO.getFileName()+ "#" +loggedInUsers.get(client.getUserId()).getUserName());
                }
            }
        }
        loggedInUsers.get(client.getUserId()).getOutputToUser().recvMsg(response);
    }

    @Override
    public synchronized void uploadFile(FileDTO fileDTO, UserDTO userDTO) throws RemoteException {
        Client client = loggedInUsers.get(userDTO.getUserId());
        loggedInUsers.get(client.getUserId()).getOutputToUser().recvMsg(dbHandler.uploadFile(fileDTO, client.getUserId()));
    }

    @Override
    public synchronized void deleteFile(FileDTO fileDTO, UserDTO userDTO) throws RemoteException, SQLException {
        Client client = loggedInUsers.get(userDTO.getUserId());
        int fileOwner = dbHandler.getOwner(fileDTO);
        String response = dbHandler.deleteFile(fileDTO, client.getUserId());
        if(sqlMessage.isSuccessfulDelete(response)){
            if(fileOwner != client.getUserId()) {
                if(loggedInUsers.get(fileOwner) != null) {
                    loggedInUsers.get(fileOwner).getOutputToUser().recvMsg(response);
                }
            }
        }
        loggedInUsers.get(client.getUserId()).getOutputToUser().recvMsg(response);
    }

    @Override
    public synchronized void downloadFile(FileDTO fileDTO, UserDTO userDTO) throws RemoteException, SQLException {
        Client client = loggedInUsers.get(userDTO.getUserId());
        int fileOwner = dbHandler.getOwner(fileDTO);
        String response = dbHandler.getFile(fileDTO);
        if(sqlMessage.isValidSQLMessage(response)){
            if(fileOwner != client.getUserId()) {
                if(loggedInUsers.get(fileOwner) != null) {
                    loggedInUsers.get(fileOwner).getOutputToUser().recvMsg("DOWNLOAD#"+fileDTO.getFileName()+ "#" +loggedInUsers.get(client.getUserId()).getUserName());
                }
            }
        }
        loggedInUsers.get(client.getUserId()).getOutputToUser().printToFile(response);
    }

}
