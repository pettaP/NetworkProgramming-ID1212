package Server.StartUp;

import Server.Controller.Controller;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.startRMI();
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void startRMI() throws RemoteException, MalformedURLException {
        try {
            LocateRegistry.getRegistry().list();
        } catch (RemoteException e) {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        }
        Controller controller = new Controller();
        Naming.rebind("RMIdb", controller);
    }
}
