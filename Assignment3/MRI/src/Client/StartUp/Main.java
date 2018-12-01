package Client.StartUp;

import Client.View.InputHandler;
import Common.FileCatalog;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Main {

    public static void main(String[] args) {
        try {
            FileCatalog fileCatalog = (FileCatalog) Naming.lookup("RMIdb");
            new InputHandler().start(fileCatalog);
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
