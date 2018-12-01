package Common;

import java.io.Serializable;
import java.util.Scanner;

public class FileDTO implements Serializable {

    private String fileName;
    private int fileSize;
    private int write;

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = Integer.parseInt(fileSize);
    }

    public void setWrite(int write) {
        this.write = write;
    }

    public String getFileName() {
        return this.fileName;
    }

    public int getFileSize() {
        return fileSize;
    }

    public int getWrite() {
        return write;
    }
}
