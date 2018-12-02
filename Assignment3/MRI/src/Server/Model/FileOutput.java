package Server.Model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

public class FileOutput {

    private final String FILEPATH = "C:\\Users\\pete_\\Documents\\KTH\\Nätverksprogrammering\\Assignments\\Assignment3\\MRI\\src\\Server\\DBFiles\\";
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
