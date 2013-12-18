
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.util.*;

public class Riddle {
    public static void main(String[] arg) throws Exception{
        Map<String, RemoteMap<String, String>> maps = getMaps();
        RemoteMap<String, String> m1, m2;
        m1 = maps.get("that");
        m2 = maps.get("this");
        m1.enter("disease", "mumps");
        m2.enter("disease", "flu");
        Map<String, String> there = m1.dump();
        Map<String, String> here  = m2.dump();
        there.put("disease", "xxx");
        here.put("disease", "xxx");
        System.out.print("there: " + m1.lookup("disease") + "   ");
        System.out.print("here:  " + m2.lookup("disease") + "\n");
        System.exit(0);
    }
    static Map<String, RemoteMap<String, String>> getMaps() throws Exception {
        Map<String, RemoteMap<String, String>> maps = new HashMap<String, RemoteMap<String, String>>();
        RemoteMap<String, String> map = new RemoteMapImpl<String, String>();
        maps.put("this", map);
        map = (RemoteMap)UnicastRemoteObject.exportObject(new RemoteMapImpl<String, String>(), 0);
        maps.put("that", map);
        return maps;
    }
}  // end class Riddle


interface RemoteMap<Key, Data> extends Remote {
    void enter(Key key, Data data)  throws KeyCollisionException, RemoteException;
    Data lookup(Key key)            throws RemoteException;
    void update(Key key, Data data) throws NoSuchKeyException, RemoteException;
    void delete(Key key)            throws NoSuchKeyException, RemoteException;
    Map<Key, Data> dump()           throws RemoteException;
}
class KeyCollisionException extends RuntimeException { }
class NoSuchKeyException    extends RuntimeException { }


class RemoteMapImpl<K, D> implements RemoteMap<K, D>  {
    private Map<K, D> map = new HashMap<K, D>();
    public void enter(K key, D data) {
        if(map.containsKey(key)) throw new KeyCollisionException();
        else map.put(key, data);
    }
    public D lookup(K key) {
        return map.get(key);
    }
    public void update(K key, D data) {
        if(!map.containsKey(key)) throw new NoSuchKeyException();
        else map.put(key, data);
    }
    public void delete(K key) {
        if(!map.containsKey(key)) throw new NoSuchKeyException();
        else map.remove(key);
    }
    public Map<K, D> dump() {
        return map;
    }
} // end class RemoteMapImpl




