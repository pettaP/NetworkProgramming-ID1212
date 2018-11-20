package Server.net;

import java.net.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

public class GameServer {
    private static final int LINGER= 5000;
    private int portNo = 8080;
    private Selector selector;
    private ServerSocketChannel listeningSocketChannel;


    public static void main(String[] args){
        GameServer gameServer = new GameServer();
        gameServer.serve();
    }

    void sendMessageToClient(SocketChannel channel) {
        channel.keyFor(selector).interestOps(SelectionKey.OP_WRITE);
        selector.wakeup();
    }

    private void serve() {
        try {
            setUpSelector();
            setUpListeningSocket();
            while (true) {

                selector.select();
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();
                    if (!key.isValid()) {
                        continue;
                    } else if (key.isAcceptable()) {
                        startGameHandler(key);
                    } else if (key.isReadable()) {
                        messageFromClient(key);
                    } else if (key.isWritable()) {
                        sendMessageToClient(key);
                    }
                }
            }
        } catch (IOException ioe) {
            System.err.println("Server failure");
        }
    }

    private void sendMessageToClient(SelectionKey key) throws IOException {
        Client client = (Client) key.attachment();
        try {
            client.sendMsg();
            key.interestOps(SelectionKey.OP_READ);
        } catch (IOException e) {
            deleteClient(key);
        }
    }

    private void deleteClient(SelectionKey key) throws IOException{
        Client client = (Client) key.attachment();
        client.gameHandler.closeConnection();
    }
    private void messageFromClient(SelectionKey key) {
        Client client = (Client) key.attachment();
        try {
            client.gameHandler.getMessage();
        } catch (IOException noConn) {
            //TO BE IMPLEMNETED
        }
    }

    private void startGameHandler(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverSocketChannel.accept();
        clientChannel.configureBlocking(false);
        GameHandler gameHandler = new GameHandler(this, clientChannel);
        Client client = new Client(gameHandler);
        clientChannel.register(selector, SelectionKey.OP_READ, client);
        clientChannel.setOption(StandardSocketOptions.SO_LINGER, LINGER);
    }

    private void setUpSelector() throws IOException{
        selector = Selector.open();
    }

    private void setUpListeningSocket() throws IOException{
        listeningSocketChannel = ServerSocketChannel.open();
        listeningSocketChannel.configureBlocking(false);
        listeningSocketChannel.bind(new InetSocketAddress(portNo));
        listeningSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private class Client {

        private GameHandler gameHandler;

        private Client(GameHandler gameHandler) {
            this.gameHandler = gameHandler;
        }

        private void sendMsg() throws IOException {
            ByteBuffer msg = null;
            synchronized (gameHandler.messagesToSend) {
                while ((msg = gameHandler.messagesToSend.peek()) != null) {
                    gameHandler.sendMsg(msg);
                    gameHandler.messagesToSend.remove();
                }
            }
        }
    }

}
