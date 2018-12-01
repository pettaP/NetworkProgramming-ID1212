package Common;

public interface UserDTO {

    public String getUserName();

    public String getUserPwd();


    public void setUserId(int userId);


    public int getUserId();

    public MessagePasser getOutputToUser();
}
