package Client.Net;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class ServerConnection {

    private static final int TIMEOUT_HALF_HOUR = 1800000;
    private static final int TIMEOUT_HALF_MINUTE = 30000;
    private Socket socket;
    private PrintWriter toServer;
    private BufferedReader fromServer;
    private ReadFromFile readFromFile;
    private FileOutput fileOutput;
    private ArrayList<String> message;

    public void sendFile(String fileName) throws IOException, FileNotFoundException {

        sendInit();
        readFromFile = new ReadFromFile(fileName);
        readFromFile.sendFile(toServer, fileName);
        toServer.close();

    }

    public void receiveFile() throws IOException{

        receiveInit();
        message = new ArrayList<>();
        String fileName = fromServer.readLine();
        String content = fromServer.readLine();
        while(content != null) {
            message.add(content);
            content = fromServer.readLine();
        }

        fileOutput.printToFile(fileName, message);
        socket.close();
    }

    private void receiveInit() throws IOException{

        socket = new Socket();
        socket.connect(new InetSocketAddress("localhost", 3500), TIMEOUT_HALF_MINUTE);
        socket.setSoTimeout(TIMEOUT_HALF_HOUR);
        fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.fileOutput = new FileOutput();
    }

    private void sendInit() throws IOException{

        socket = new Socket();
        socket.connect(new InetSocketAddress("localhost", 3500), TIMEOUT_HALF_MINUTE);
        socket.setSoTimeout(TIMEOUT_HALF_HOUR);
        boolean autoFlush = true;
        toServer = new PrintWriter(socket.getOutputStream(), autoFlush);

    }

    private class ReadFromFile {

        BufferedReader fileReader;
        private final String PATHNAME = "C:\\Users\\pete_\\Documents\\KTH\\Nätverksprogrammering\\Assignments\\Assignment3\\MRI\\src\\Client\\ClientFiles\\";

        public ReadFromFile(String fileName) throws FileNotFoundException {
            fileReader = new BufferedReader(new FileReader(PATHNAME+fileName));
        }


        public void sendFile(PrintWriter toServer, String fileName) throws IOException{

            String line = fileName;
            while (line != null) {
                toServer.println(line);
                line = fileReader.readLine();
            }
        }

    }

    private class FileOutput {

        private final String FILEPATH = "C:\\Users\\pete_\\Documents\\KTH\\Nätverksprogrammering\\Assignments\\Assignment3\\MRI\\src\\Client\\ClientFiles\\";
        private FileWriter fileWriter;
        private PrintWriter printWriter;

        public void printToFile(String fileName, ArrayList<String> message) throws IOException{
            fileWriter = new FileWriter(FILEPATH+fileName);
            this.printWriter = new PrintWriter(fileWriter);
            Iterator<String> itr = message.iterator();
            while (itr.hasNext()) {
                printWriter.println(itr.next());
            }
            printWriter.flush();
            printWriter.close();
            fileWriter.close();
        }
    }
}
