package Server.net;

public class Message {
    public String msgType;
    public String msgBody;

    public Message(String receivedString) {
        parse(receivedString);
    }

    /**
     * extracts the parts of the message. the first element is the message type and the second is the letter och word guess
     * @param strToParse the unparsed message received from the socket from the client side
     */
    private void parse(String strToParse) {
        String[] msgTokens = strToParse.split(" ");
        msgType = msgTokens[0].toUpperCase();
        if (msgTokens.length > 1) {
            msgBody = msgTokens[1];
        }
    }
}
