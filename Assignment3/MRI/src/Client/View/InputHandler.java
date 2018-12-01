package Client.View;

import Common.FileCatalog;
import Common.FileDTO;
import Common.MessagePasser;
import Common.UserDTO;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class InputHandler implements Runnable {

    private PrintToPromt printToPromt;
    private FileCatalog fileCatalog;
    private MessagePasser outputToUser;
    private boolean receivingInput;
    private final Scanner scanner = new Scanner(System.in);
    private int idAtServer;
    private UserDTO userDTO;
    private FileDTO fileDTO;
    private boolean loggedIn;

    public void start(FileCatalog fileCatalog) {
        this.printToPromt = new PrintToPromt();
        this.fileDTO = new FileDTO();
        this.fileCatalog = fileCatalog;
        this.receivingInput = true;
        this.loggedIn = false;
        this.idAtServer = 0;
        new Thread(this).start();
        try {
            this.outputToUser = new OutputToUser();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        printToPromt.startMsg();
        while(receivingInput) {
            try {
                while (!loggedIn) {
                    Input inputFromUser = new Input(readNextLine());
                    switch (inputFromUser.messageHeader) {
                        case ("LOGIN"):
                            this.userDTO = fileCatalog.loginUser(inputFromUser.userName, inputFromUser.userPwd, this.outputToUser);
                            if (userDTO != null) {
                                loggedIn = true;
                                printToPromt.loggedIn(inputFromUser.userName);
                            } else {
                                printToPromt.errorLogin();
                            }
                            break;
                        case ("REGISTER"):
                            fileCatalog.registerUser(inputFromUser.userName, inputFromUser.userPwd, this.outputToUser);
                            break;
                        case ("LOGOUT"):
                            printToPromt.invalidLogout();
                            break;
                        case ("HELP"):
                            printToPromt.printHelp();
                            break;
                        default:
                            printToPromt.badCommand();
                            break;
                    }
                } while(loggedIn) {
                    Input inputFromUser = new Input(readNextLine());
                    switch (inputFromUser.messageHeader) {
                        case ("LOGOUT"):
                            fileCatalog.removeUser(userDTO);
                            idAtServer = 0;
                            break;
                        case ("HELP"):
                            printToPromt.printHelp();
                            break;
                        case ("LIST"):
                            fileCatalog.listAllFiles(userDTO);
                            break;
                        case ("ACCESS"):
                            fileDTO = new FileDTO();
                            fileDTO.setFileName(inputFromUser.messageBody);
                            fileCatalog.getFile(fileDTO, userDTO);
                            break;
                        case ("DOWNLOAD"):
                            fileDTO = new FileDTO();
                            fileDTO.setFileName(inputFromUser.messageBody);
                            fileCatalog.downloadFile(fileDTO, userDTO);
                            break;
                        case ("UPLOAD"):
                            fileDTO = setFileDTO();
                            fileCatalog.uploadFile(fileDTO, userDTO);
                            break;
                        case ("DELETE"):
                            FileDTO file = new FileDTO();
                            file.setFileName(inputFromUser.messageBody);
                            fileCatalog.deleteFile(file, userDTO);
                            break;
                        default:
                            printToPromt.badCommand();
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String readNextLine() {
        return scanner.nextLine();
    }

    private FileDTO setFileDTO() {
        FileDTO fileDTO = new FileDTO();
        printToPromt.filename();
        fileDTO.setFileName(readNextLine());
        printToPromt.fileSize();
        fileDTO.setFileSize(readNextLine());
        printToPromt.fileWrite();
        String input = readNextLine();
        if(input.equalsIgnoreCase("Y")){
            fileDTO.setWrite(0);
        } else {
            fileDTO.setWrite(1);
        }
        return fileDTO;
    }

    private class OutputToUser extends UnicastRemoteObject implements MessagePasser {

        MessageFromServer messageFromServer;
        PrintToFile printToFile;

        public OutputToUser() throws RemoteException {
            this.messageFromServer = new MessageFromServer();
            this.printToFile = new PrintToFile();
        }

        @Override
        public void recvMsg(String msg) {
            messageFromServer.parseMsg(msg);
        }

        @Override
        public void printToFile(String msg) {
            printToFile.printToFile(msg);
        }
    }


}
