package ueb07;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * User: Julian
 * Date: 19.12.13
 * Time: 17:12
 */
public interface DictionaryRMI extends Remote {

    String[] lookup(String word) throws RemoteException;
}
