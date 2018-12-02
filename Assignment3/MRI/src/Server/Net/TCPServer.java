package Server.Net;

import Server.Model.FileOutput;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TCPServer implements Runnable{

    private BufferedReader fromClient;
    private PrintWriter toClient;
    private static final int LINGER= 5000;
    private static final int TIMEOUT = 1800000;
    private int portNo = 3500;
    private ArrayList<String> message;
    private FileOutput fileOutput;
    public boolean sending;
    public String fileNameFromDB;

    public TCPServer() {
        this.fileOutput = new FileOutput();
        this.sending = false;
    }

    @Override
    public void run() {
        try {
            ServerSocket listenSocket = new ServerSocket(portNo);
            while (true) {
                Socket clientSocket = listenSocket.accept();
                clientSocket.setSoLinger(true, LINGER);
                clientSocket.setSoTimeout(TIMEOUT);
                if (!sending) {
                    fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    message = new ArrayList<>();
                    String fileName = fromClient.readLine();
                    String content = fromClient.readLine();
                    while(content != null) {
                        message.add(content);
                        content = fromClient.readLine();
                    }

                    fileOutput.printToFile(fileName, message);
                    clientSocket.close();
                } else {
                    toClient = new PrintWriter(clientSocket.getOutputStream(), true);
                    ReadFromFile readFromFile = new ReadFromFile(fileNameFromDB);
                    readFromFile.sendFile(toClient, fileNameFromDB);
                    toClient.close();
                }

            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private class ReadFromFile {

        BufferedReader fileReader;
        private final String PATHNAME = "C:\\Users\\pete_\\Documents\\KTH\\Nätverksprogrammering\\Assignments\\Assignment3\\MRI\\src\\Server\\DBFiles\\";
        ;

        public ReadFromFile(String fileName) throws FileNotFoundException {
            fileReader = new BufferedReader(new FileReader(PATHNAME+fileName));
        }

        public void sendFile(PrintWriter toClient, String fileName) throws IOException{

            String line = fileName+"\n";
            while (line != null) {
                toClient.println(line);
                line = fileReader.readLine();
            }
        }

    }
}
