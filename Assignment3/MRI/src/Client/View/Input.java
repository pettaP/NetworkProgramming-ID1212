package Client.View;

public class Input {

    String messageHeader;
    String messageBody;
    String userName;
    String userPwd;
    private String[] messageParts;

    public Input(String inputFromUser) {
        parse(inputFromUser);
        if(messageHeader.equalsIgnoreCase("LOGIN") || messageHeader.equalsIgnoreCase("REGISTER")) {
            assignLoginParameters();
        }
        if(messageHeader.equalsIgnoreCase("ACCESS") || messageHeader.equalsIgnoreCase("DELETE") || messageHeader.equalsIgnoreCase("DOWNLOAD")) {
            assignFileParameter();
        }
    }

    private void parse(String inputFromUser) {
        messageParts = inputFromUser.split(" ");
        extractHeader(messageParts[0]);
    }

    private void extractHeader(String unparsedHeader) {
        this.messageHeader = unparsedHeader.toUpperCase();
    }

    private void assignLoginParameters() {
        if(messageParts.length == 3) {
            this.userName = messageParts[1];
            this.userPwd = messageParts[2];
        } else {
            this.messageHeader = "FOO";
        }
    }

    private void assignFileParameter() {
        StringBuilder str = new StringBuilder("");
        for(int i = 1; i < messageParts.length; i++) {
            str.append(messageParts[i]);
            str.append(" ");
        }
        this.messageBody = str.toString();
    }
}
