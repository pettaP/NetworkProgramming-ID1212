package Client.View;

public class MessageFromServer {

    private String msgHeader;
    private String[] messageSplit;

    public void parseMsg(String msg) {
        messageSplit = msg.split("#");
        this.msgHeader = messageSplit[0];
        msgCase();
    }

    private void msgCase() {
        switch (msgHeader) {
            case ("NOTUNIQUE"):
                System.out.println("The name that you chose is not unique. Try another name.");
                break;
            case ("USERREG"):
                System.out.println("Good! You registered as a new user with user name \"" + messageSplit[1] + "\"");
                break;
            case ("LOGOUT"):
                System.out.println("You logged out from the server");
                break;
            case ("SQL"):
                System.out.println("FileName            File size          Owner             Write     ");
                for(int i = 1; i < messageSplit.length; ++i) {
                    StringBuilder str = new StringBuilder("");
                    int spaces = 20 - messageSplit[i].length();
                    for(int j = 0; j < spaces; j++) {
                        str.append(" ");
                    }
                    System.out.print(messageSplit[i] + str.toString());
                    if((i)%4 == 0){
                        System.out.print("\n");
                    }
                    }
                break;
            case ("NOFILE"):
                System.out.println("There is no file with that name. Please check the filename");
                break;
            case ("FILEREG"):
                System.out.println("The file \"" + messageSplit[1] + "\" was added to the database");
                break;
            case ("DELETE"):
                System.out.println("The file \"" + messageSplit[1] + "\" was removed from the database");
                break;
            case ("PROTECTED"):
                System.out.println("The file \"" + messageSplit[1] + "\" is protected by owner");
                break;
            case ("ACCESS"):
                System.out.println("The file \"" + messageSplit[1] + "\" was accessed by " + messageSplit[2]);
                break;
            case ("DOWNLOAD"):
                System.out.println("The file \"" + messageSplit[1] + "\" was downloaded by " + messageSplit[2]);
                break;
        }
    }
}
