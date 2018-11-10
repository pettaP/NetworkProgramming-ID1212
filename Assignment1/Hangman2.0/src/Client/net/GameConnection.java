package Client.net;

import Client.View.ServerMessageParser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class GameConnection {

    private Socket socket;
    private boolean connected;
    private PrintWriter toServer;
    private InputStreamReader fromServer;

    public void connect(String host, int port, OutputHandler broadcastHandler) throws
            IOException {
        broadcastHandler.handleMsg("Connecting to Game server");
        socket = new Socket();
        socket.connect(new InetSocketAddress(host, port), 30000);
        socket.setSoTimeout(300000);
        connected = true;
        boolean autoFlush = true;
        toServer = new PrintWriter(socket.getOutputStream(), autoFlush);
        fromServer = new InputStreamReader(socket.getInputStream());
        new Thread(new ServerListener(broadcastHandler)).start();
    }

    public void sendGuessEntry(String type, String msg) {
        sendMsg(type, msg);
    }

    public void startGame(String type) {
        sendMsg(type);
    }

    private void sendMsg(String... parts) {
        String joined = String.join(" ", parts);

        String msgWithHeader = insertHeader(joined);

        toServer.println(msgWithHeader);
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

    public void disconnect() throws IOException {
        sendMsg("QUIT");
        socket.close();
        socket = null;
        connected = false;
    }

    private class ServerListener implements Runnable {

        private final OutputHandler outputHandler;


        private ServerListener(OutputHandler outputHandler) {
            this.outputHandler = outputHandler;
        }

        @Override
        public void run() {
                while (connected) {
                    ServerMessageParser serverMessage = new ServerMessageParser(fromServer);
                    outputHandler.handleMsg(serverMessage.parsedMessage);
                }
        }
    }
}
