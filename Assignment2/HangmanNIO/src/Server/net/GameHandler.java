package Server.net;

import Server.Controller.Controller;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ForkJoinPool;

class GameHandler {
    private SocketChannel channel;
    private GameServer gameServer;
    private Controller controller;
    private String msgWithoutHeader;
    private String msgWithHeader;
    private ByteBuffer msgFromClient = ByteBuffer.allocateDirect(1000);
    private ClientMessageParser clientMessageParser = new ClientMessageParser();
    protected final Queue<ByteBuffer> messagesToSend = new ArrayDeque<>();


    GameHandler(GameServer gameServer, SocketChannel channel) {
        this.gameServer = gameServer;
        this.channel = channel;
        this.controller = new Controller();
    }

    public void handleMsg() {
            try {
                if (controller.getGameStatus()) {
                    Message msg = new Message(msgWithoutHeader);
                    switch (msg.msgType) {
                        case ("LETTER_GUESS"):
                            msgWithHeader = insertHeader(controller.matchLetter(msg.msgBody));
                            addToQueue(msgWithHeader);
                            gameServer.sendMessageToClient(channel);
                            break;
                        case ("WORD_GUESS"):
                            msgWithHeader = insertHeader(controller.matchWord(msg.msgBody));
                            addToQueue(msgWithHeader);
                            gameServer.sendMessageToClient(channel);
                            break;
                        case ("QUIT"):
                            closeConnection();
                            System.err.println("Connection closed by client");
                            break;
                    }
                }
                else  {
                    Message msg = new Message(msgWithoutHeader);
                    switch (msg.msgType) {
                        case ("START"):
                            msgWithHeader = insertHeader(controller.newGame());
                            addToQueue(msgWithHeader);
                            gameServer.sendMessageToClient(channel);
                            break;
                        case ("QUIT"):
                            closeConnection();
                            System.err.println("Connection closed by client");
                            break;
                        default:
                            break;
                    }
                }
            } catch (IOException ioe) {
                System.err.println("Connection lost on client side. Server terminated");
            }

    }

    void addToQueue(String msgWithHeader) {
        ByteBuffer messageInBytes = ByteBuffer.wrap(msgWithHeader.getBytes());
        synchronized (messagesToSend) {
            messagesToSend.add(messageInBytes);
        }
    }

    void sendMsg(ByteBuffer msg) throws IOException {
        channel.write(msg);
        if (msg.hasRemaining()) {
            throw new IOException("Cloud not send");
        }
    }

    void getMessage() throws IOException{
        msgFromClient.clear();
        int numBytes = channel.read(msgFromClient);
        if (numBytes == -1) {
            throw new IOException("Connection closed");
        }
        byte[] bytes = getBytesFromBuffer();
        msgWithoutHeader = clientMessageParser.parseMessageFromClient(bytes);
        //ForkJoinPool.commonPool().execute(this);
        handleMsg();
    }

    private byte[] getBytesFromBuffer() {
        msgFromClient.flip();
        byte[] messageBytes = new byte[msgFromClient.remaining()];
        msgFromClient.get(messageBytes);
        return messageBytes;
    }

    /**
     * closes the socket and disconnects the client from the server
     */
    void closeConnection() throws IOException{
        channel.close();
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
