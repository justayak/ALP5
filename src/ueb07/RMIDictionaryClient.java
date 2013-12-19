package ueb07;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * User: Julian
 * Date: 19.12.13
 * Time: 17:14
 */
public class RMIDictionaryClient {

    public static void main(String[]args) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry();
        DictionaryRMI dict = (DictionaryRMI) registry.lookup("dict");
        String[] result = dict.lookup("tree");
        if (result == null){
            System.out.println("null..");
        }else{
            for(String word : result){
                System.out.println(word);
            }
        }

    }

}
