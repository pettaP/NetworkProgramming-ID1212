package Server.net;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.net.SocketException;

public class GameServer {
    private static final int LINGER= 5000;
    private static final int TIMEOUT = 1800000;
    private int portNo = 7200;

    public static void main(String[] args){
        GameServer gameServer = new GameServer();
        gameServer.serve();
    }

    private void serve() {
        try {
            ServerSocket listenSocket = new ServerSocket(portNo);
            while (true) {
                Socket clientSocket = listenSocket.accept();
                startGameHandler(clientSocket);
            }
        } catch (IOException ioe) {
            System.err.println("Server failure");
        }
    }

    private void startGameHandler(Socket clientSocket) throws SocketException{
        clientSocket.setSoLinger(true, LINGER);
        clientSocket.setSoTimeout(TIMEOUT);
        GameHandler handler = new GameHandler(clientSocket);
        Thread handlerThread = new Thread(handler);
        handlerThread.setPriority(Thread.MAX_PRIORITY);
        handlerThread.start();
    }

}
