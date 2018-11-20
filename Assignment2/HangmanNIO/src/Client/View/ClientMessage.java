package Client.View;

public class ClientMessage {

    private String messageType;
    private String messageBody;

    public ClientMessage (String clientMessage){
        processMessage(clientMessage);
    }

    private void processMessage(String clientMessage) {
        String[] messageArray = clientMessage.split(" ");
        messageType = messageArray[0].toUpperCase();
        if(messageArray.length > 1){
            if(messageType.equalsIgnoreCase("Guess") & messageArray[1].length() > 1){
                messageType = "WORD_GUESS";
            } else if (messageType.equalsIgnoreCase("Guess") & messageArray[1].length() == 1){
                messageType = "LETTER_GUESS";
            } else {
                messageType = "NO_COMMAND";
            }
            messageBody = messageArray[1];
        } else if (messageType.equalsIgnoreCase("Start")){
            messageType = "START";
        } else if (messageType.equalsIgnoreCase("Quit")){
            messageType = "QUIT";
        }else {
            messageType = "NO_COMMAND";
        }
    }

    public String getMessageType() {
        return messageType;
    }

    public String getMessageBody() {
        return messageBody;
    }
}
