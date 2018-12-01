package Server.Model;

import Common.MessagePasser;
import Common.UserDTO;

import java.io.Serializable;

public class Client implements UserDTO, Serializable{

    private String userName;
    private String userPwd;
    private MessagePasser outputToUser;
    private int userId;

    public Client(String userName, String userPwd, MessagePasser outputToUser) {
        this.userName = userName;
        this.userPwd = userPwd;
        this.outputToUser = outputToUser;
        this.userId = 0;
    }

    @Override
    public String getUserName() {
        return this.userName;
    }

    @Override
    public String getUserPwd() {
        return this.userPwd;
    }

    @Override
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public int getUserId() {
        return this.userId;
    }

    @Override
    public MessagePasser getOutputToUser() {
        return outputToUser;
    }
}
