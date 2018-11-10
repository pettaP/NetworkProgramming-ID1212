package Client.View;

import java.io.IOException;
import java.io.InputStreamReader;

public class ServerMessageParser {

    private int byteHeader;
    private int[] byteArray;
    public String parsedMessage;
    StringBuilder sb = new StringBuilder("");
    private int firstPosHeader = 0;
    private int secondPosHeader = 0;

    public ServerMessageParser(InputStreamReader inputReader) {
        parseMsgHeader(inputReader);
        byteArray = new int[byteHeader];
        parsedMessage = deCode(inputReader);
    }

    private void parseMsgHeader(InputStreamReader inputReader) {
        try {
            firstPosHeader = Character.getNumericValue(inputReader.read());
            while(firstPosHeader == -1){
                firstPosHeader = Character.getNumericValue(inputReader.read());
            }
            secondPosHeader = Character.getNumericValue(inputReader.read());
            byteHeader = (firstPosHeader * 10) + secondPosHeader;
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    private String deCode(InputStreamReader inputReader) {
        int bytesRead = 0;
        try {
            while (bytesRead < byteHeader) {
                byteArray[bytesRead++] = inputReader.read();
            }
        } catch (IOException ioe){
            ioe.printStackTrace();
        }

        for(int i: byteArray){
            sb.append(Character.toString((char)i));
        }
        return sb.toString();
    }
}
