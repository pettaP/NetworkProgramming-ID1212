package Server.net;

import Server.controller.Controller;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class GameHandler implements Runnable{
    private Socket clientSocket;
    private Controller controller;
    private boolean connected;
    private BufferedReader fromClient;
    private PrintWriter toClient;
    private String msgWithHeader;

    private String debug;

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
            fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            toClient = new PrintWriter(clientSocket.getOutputStream(), autoFlush);
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
        while (connected) {
            try {
                while (controller.getGameStatus()) {
                    String foo = fromClient.readLine();
                    Message msg = new Message(foo);
                    //System.out.println(foo);
                    switch (msg.msgType) {
                        case ("LETTER_GUESS"):
                            toClient.println(controller.matchLetter(msg.msgBody));
                            break;
                        case ("WORD_GUESS"):
                            toClient.println(controller.matchWord(msg.msgBody));
                            break;
                        case ("START"):
                            debug = controller.newGame();
                            //System.out.println(debug);
                            toClient.println(debug);
                            break;
                        case ("QUIT"):
                            disconnectClient();
                            connected = false;
                            break;
                    }
                }
                while (!controller.getGameStatus()) {
                    //System.out.println("Listening for message from client");
                    String foo = fromClient.readLine();
                    System.out.println(foo);
                    Message msg = new Message(foo);
                    switch (msg.msgType) {
                        case ("START"):
                            debug = controller.newGame();
                            //System.out.println(debug);
                            toClient.println(debug);
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
}
