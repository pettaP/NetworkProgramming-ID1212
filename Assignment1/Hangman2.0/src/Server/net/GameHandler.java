package Server.net;

import Server.controller.Controller;

import java.io.*;
import java.net.Socket;

public class GameHandler implements Runnable{
    private Socket clientSocket;
    private Controller controller;
    private boolean connected;
    private InputStreamReader fromClient;
    private PrintWriter toClient;
    private String msgWithHeader;

    /**
     * constructor. creates a controller for this game session
     * @param clientSocket the socket to listen to for messages from the client
     */
    GameHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.controller = new Controller();
        connected = true;
    }

    /**
     *
     */
    @Override
    public void run() {
        try {
            boolean autoFlush = true;
            fromClient = new InputStreamReader(clientSocket.getInputStream());
            toClient = new PrintWriter(clientSocket.getOutputStream(), autoFlush);
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
        while (connected) {
            try {
                while (controller.getGameStatus()) {
                    ClientMessageParser messageToDecode = new ClientMessageParser(fromClient);
                    Message msg = new Message(messageToDecode.parsedMessage);
                    switch (msg.msgType) {
                        case ("LETTER_GUESS"):
                            msgWithHeader = insertHeader(controller.matchLetter(msg.msgBody));
                            toClient.println(msgWithHeader);
                            break;
                        case ("WORD_GUESS"):
                            msgWithHeader = insertHeader(controller.matchWord(msg.msgBody));
                            toClient.println(msgWithHeader);
                            break;
                        case ("QUIT"):
                            disconnectClient();
                            connected = false;
                            break;
                    }
                }
                while (!controller.getGameStatus()) {
                    ClientMessageParser messageToDecode = new ClientMessageParser(fromClient);
                    Message msg = new Message(messageToDecode.parsedMessage);
                    switch (msg.msgType) {
                        case ("START"):
                            msgWithHeader = insertHeader(controller.newGame());
                            toClient.println(msgWithHeader);
                            break;
                        case ("QUIT"):
                            disconnectClient();
                            connected = false;
                            break;
                        default:
                            break;
                    }
                }
            } catch (IOException ioe) {
                System.err.println("Connection lost on client side. Server terminated");
                disconnectClient();
                connected = false;
            }
        }
    }

    /**
     * closes the socket and disconnects the client from the server
     */
    private void disconnectClient() {
        try {
            clientSocket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        connected = false;
    }

    private String insertHeader(String joinedMsg) {
        StringBuilder sb = new StringBuilder(joinedMsg);
        if (joinedMsg.length() > 9) {
            sb.insert(0, joinedMsg.length());
        } else {
            sb.insert(0, joinedMsg.length());
            sb.insert(0, "0");
        }

        return sb.toString();
    }
}
