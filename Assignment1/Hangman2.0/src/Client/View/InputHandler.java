package Client.View;

import Client.Controller.Controller;
import Client.net.OutputHandler;
import java.util.Scanner;

public class InputHandler implements Runnable {

    private boolean inputFromUser;
    private Controller contr = new Controller();
    private Scanner scanner = new Scanner(System.in);

    public InputHandler() {
        inputFromUser = true;
    }

    @Override
    public void run(){
        contr.connect("192.168.1.12", 8080, new ConsoleOutput());
        while (inputFromUser) {
            try {
                ClientMessage userInput = new ClientMessage(getInput());
                switch (userInput.getMessageType()) {
                    case ("QUIT"):
                        inputFromUser = false;
                        contr.disconnect();
                        break;
                    case ("LETTER_GUESS"):
                        contr.sendLetterGuess(userInput.getMessageBody());
                        break;
                    case ("WORD_GUESS"):
                        contr.sendWordGuess(userInput.getMessageBody());
                        break;
                    case ("START"):
                        contr.startGame();
                        break;
                    case ("NO_COMMAND"):
                        errorMsg();
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getInput(){
        return scanner.nextLine();
    }

    private void errorMsg() {
        System.out.println("Your command must start with \"Guess\" followed by a space or be \"Quit\" or \"Start\"");
    }

    private class ConsoleOutput implements OutputHandler {

        @Override
        public void handleMsg(String msg) {
            MsgParser parsedMsg = new MsgParser(msg);
            if(parsedMsg.msgType.equalsIgnoreCase("PENDING")){
                System.out.println("Your score: " + parsedMsg.gameScore);
                System.out.println("Tries Left: " + parsedMsg.gameTries);
                System.out.println(parsedMsg.gameCrypt);
                parsedMsg = null;
            } else if (parsedMsg.msgType.equalsIgnoreCase("WON")){
                System.out.println("Your score: " + parsedMsg.gameScore);
                System.out.println("You won! Type \"start\" to play another game");
                parsedMsg = null;
            } else if (parsedMsg.msgType.equalsIgnoreCase("LOST")){
                System.out.println("Your score: " + parsedMsg.gameScore);
                System.out.println("You lost! Type \"start\" to play another game");
                parsedMsg = null;
            } else {
                System.out.println(msg);
                parsedMsg = null;
            }
        }
    }

    private class MsgParser {
        private String msgType;
        private String gameScore;
        private String gameTries;
        private String gameCrypt;

        public MsgParser(String msg){
            parse(msg);
        }

        private void parse(String msgToParse) {
            String[] msgArray = msgToParse.split("#");
            msgType = msgArray[0];
            if (msgArray[0].equalsIgnoreCase("PENDING") || msgArray[0].equalsIgnoreCase("WON") || msgArray[0].equalsIgnoreCase("LOST")) {
                gameScore = msgArray[1];
                gameTries = msgArray[2];
                gameCrypt = msgArray[3];

            }
        }
    }

}
