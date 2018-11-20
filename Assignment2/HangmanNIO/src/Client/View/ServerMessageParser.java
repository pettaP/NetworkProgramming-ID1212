package Client.View;

import java.io.IOException;
import java.io.InputStreamReader;

public class ServerMessageParser {

    private int byteHeader;

    public String parseMessageFromServer(byte[] bytesFromServer) {
        parseMsgHeader(bytesFromServer);
        return deCode(bytesFromServer);
    }

    private void parseMsgHeader(byte[] bytesFromServer) {
        int firstPosHeader;
        int secondPosHeader;
        firstPosHeader = Character.getNumericValue(bytesFromServer[0]);
        secondPosHeader = Character.getNumericValue(bytesFromServer[1]);
        byteHeader = (firstPosHeader * 10) + secondPosHeader;
    }

    private String deCode(byte[] bytesFromServer) {
        StringBuilder sb = new StringBuilder("");
        for(int i = 2; i < byteHeader+1; i++){
            sb.append(Character.toString((char)bytesFromServer[i]));
        }
        return sb.toString();
    }
}
