package com.example.pettap.hangmanandroid.Service;

public class ClientMessage {

    private String messageType;
    public String parsedMsg;

    public ClientMessage(String clientMessage){
        processMessage(clientMessage);
    }

    private void processMessage(String clientMessage) {

        if(clientMessage.length() == 1) {
            messageType = "LETTER_GUESS";
            StringBuilder sb = new StringBuilder("");
            sb.append(messageType + " " + clientMessage);
            parsedMsg = sb.toString();
        } else if(clientMessage.equalsIgnoreCase("START")){
            parsedMsg = "START";
        } else if (clientMessage.equalsIgnoreCase("QUIT")){
            parsedMsg = "QUIT";
        } else {
            messageType = "WORD_GUESS";
            StringBuilder sb = new StringBuilder("");
            sb.append(messageType + " " + clientMessage);
            parsedMsg = sb.toString();
        }
    }
}
