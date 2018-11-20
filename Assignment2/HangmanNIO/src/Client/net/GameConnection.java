package Client.net;

import Client.View.ServerMessageParser;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

public class GameConnection implements Runnable{

    private boolean connected;
    private final Queue<ByteBuffer> messagesToSend = new ArrayDeque<>();
    private final ByteBuffer messageFromServer = ByteBuffer.allocateDirect(100);
    private final ServerMessageParser serverMessageParser = new ServerMessageParser();
    private OutputHandler outputHandler;
    private InetSocketAddress serverAddress;
    private SocketChannel socketChannel;
    private Selector selector;
    private volatile boolean timeToSend = false;

    @Override
    public void run() {
        try {
            setUpConnection();
            setUpSelector();

            while (connected || !messagesToSend.isEmpty()) {
                if (timeToSend) {
                    socketChannel.keyFor(selector).interestOps(SelectionKey.OP_WRITE);
                    timeToSend = false;
                }

                selector.select();
                        for(SelectionKey key : selector.selectedKeys()) {
                            selector.selectedKeys().remove(key);
                            if (!key.isValid()) {
                                continue;
                            }
                            if (key.isConnectable()){
                                makeConnection(key);
                            } else if (key.isReadable()) {
                                msgFromServer(key);
                            } else if (key.isWritable()){
                                msgToServer(key);
                            }
                        }
            }
        } catch (Exception e) {
            System.err.println("Connection error");
        }
    }

    public void connect(String host, int port, OutputHandler outputHandler) {
        this.outputHandler = outputHandler;
        serverAddress = new InetSocketAddress(host, port);
        new Thread(this).start();
    }

    private void msgToServer(SelectionKey key) throws IOException {
        ByteBuffer msg;
        synchronized (messagesToSend) {
            while ((msg = messagesToSend.peek()) != null){
                socketChannel.write(msg);
                if (msg.hasRemaining()) {
                    return;
                }
                messagesToSend.remove();
            }
            key.interestOps(SelectionKey.OP_READ);
        }
    }

    public void startGame() {
        sendMsg("START");
    }

    public void disconnect() throws IOException {
        sendMsg("QUIT");
        connected = false;
        //Notify user about status
    }

    public void sendLetterGuess(String msgBody) {
        sendMsg("LETTER_GUESS", msgBody);
    }

    public void sendWordGuess(String msgBody) {
        sendMsg("WORD_GUESS", msgBody);
    }

    private void sendMsg(String... parts) {
        String joined = String.join(" ", parts);

        String msgWithHeader = insertHeader(joined);

        synchronized (messagesToSend) {
            messagesToSend.add(ByteBuffer.wrap(msgWithHeader.getBytes()));
        }
        timeToSend = true;
        selector.wakeup();
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

    private void msgFromServer(SelectionKey key) throws IOException {
        messageFromServer.clear();
        int bytesInBuffer = socketChannel.read(messageFromServer);
        if (bytesInBuffer == -1) {
            throw new IOException("Buffer corrupt");
        }
        byte[] bytes = getBytesFromBuffer();
        String parsedMessage = serverMessageParser.parseMessageFromServer(bytes);
        printMessage(parsedMessage);
    }

    private byte[] getBytesFromBuffer() {
        messageFromServer.flip();
        byte[] messageBytes = new byte[messageFromServer.remaining()];
        messageFromServer.get(messageBytes);
        return messageBytes;
    }

    private void setUpConnection()throws IOException {
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(serverAddress);
        connected = true;
    }

    private void makeConnection(SelectionKey key) throws IOException {
        socketChannel.finishConnect();
        key.interestOps(SelectionKey.OP_READ);
        //Print status to user
    }

    private void setUpSelector() throws IOException{
        selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
    }

    private void printMessage(String messageToPrint) {
        Executor pool = ForkJoinPool.commonPool();
        pool.execute(() -> outputHandler.handleMsg(messageToPrint));
    }
}
