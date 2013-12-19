package ueb07;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import static java.rmi.server.UnicastRemoteObject.exportObject;

/**
 * User: Julian
 * Date: 19.12.13
 * Time: 17:50
 */
public class Aufgabe3 {

    public static void main(String[]args) throws RemoteException {
        Rings rings = (Rings) exportObject(new RemoteRings(), 0);
        rings.create("first", 7).flood(4711);
        System.out.println(rings.get("first").get());
    }

    interface Ring {    // ring of objects, initially all containing 0
        void set(int i)   throws RemoteException;    // set next value in ring (internal iterator)
        int  get()        throws RemoteException;    // get next value in ring (internal iterator)
        void flood(int i) throws RemoteException;    // flood ring with i
    }
    interface Rings extends Remote {    // remote Ring management
        Ring create(String name, int n) throws RemoteException;
        // creates ring 'name' of length n>0 if not already created
        Ring get(String name) throws RemoteException;
        // gets ring 'name', or null if no such ring
    }
    class RMI {


    }
    static class RemoteRings implements Rings{

        @Override
        public Ring create(String name, int n) throws RemoteException {
            return null;
        }

        @Override
        public Ring get(String name) throws RemoteException {
            return null;
        }
    }

}
