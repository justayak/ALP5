package ueb07;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;

/**
 * User: Julian
 * Date: 19.12.13
 * Time: 17:13
 */
public class RMIDictionaryServer extends Translator implements DictionaryRMI {

    public static void main(String[]args) throws RemoteException {
        LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        RMIDictionaryServer s = new RMIDictionaryServer();
        DictionaryRMI stub = (DictionaryRMI) UnicastRemoteObject.exportObject(s,0);
        RemoteServer.setLog(System.out);
        LocateRegistry.getRegistry().rebind("dict", stub);
    }

    public RMIDictionaryServer() throws RemoteException{

    }

    @Override
    public String[] lookup(String word) throws RemoteException {
        return this.translate(word);
    }
}
