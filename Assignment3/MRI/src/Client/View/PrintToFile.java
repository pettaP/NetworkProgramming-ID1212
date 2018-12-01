package Client.View;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;

public class PrintToFile {

    private FileWriter fileWriter;
    private PrintWriter printWriter;
    private String[] messageSplit;

    public void printToFile(String msg) {
        try {
            printInit();
            printMsg(msg);
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printInit() throws IOException{
            Date date = new Date();
            this.fileWriter = new FileWriter("C:\\Users\\pete_\\Documents\\KTH\\Nätverksprogrammering\\Assignments\\Assignment3\\MRI\\downloadFile.txt");
            this.printWriter = new PrintWriter(fileWriter);
    }

    private void printMsg(String msg) {
        splitMsg(msg);
        printWriter.println("FileName            File size          Owner             Write     ");
        for(int i = 1; i < messageSplit.length; ++i) {
            StringBuilder str = new StringBuilder("");
            int spaces = 20 - messageSplit[i].length();
            for(int j = 0; j < spaces; j++) {
                str.append(" ");
            }
            printWriter.print(messageSplit[i] + str.toString());
            if((i)%4 == 0){
                printWriter.print("\n");
            }
        }
    }

    private void splitMsg(String msg) {
        messageSplit = msg.split("#");
    }

    private void close() throws IOException{
        printWriter.flush();
        printWriter.close();
        fileWriter.close();
    }
}
