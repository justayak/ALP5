package ueb04.a2;

import java.io.IOException;
import java.net.*;

/**
 * User: Julian
 * Date: 18.11.13
 * Time: 23:22
 */
public class Comm {

    private static String name;
    private static int[] peers;
    private static int FirstPort =  5000;

    public static void init(String n, int[] p){
        name = n;
        peers = p;
        initNetwork();
    }

    public static void SEND(String message, String destination){

    }

    public static String RECV(){
        return null;
    }

    //=========================================
    // M E S S A G I N G
    //=========================================

    static DatagramSocket socket;
    static InetAddress address;

    static void initNetwork(){
        try {
            address = InetAddress.getLocalHost();

            socket = new DatagramSocket(FirstPort);

            for(int i = FirstPort; i < FirstPort + 10; i++){
                System.out.println(available(i) + " <> "+ i);
            }





        } catch (SocketException e) {
            e.printStackTrace();
            System.out.println("Comm -> initNetwork _ shit !");
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.out.println("Comm -> initNetwork _ Localhost fliegt uns um die Ohren");
        }
    }

    /**
     * Nice geborgt von Apache:
     * http://svn.apache.org/viewvc/camel/trunk/components/camel-test/src/main/java/org/apache/camel/test/AvailablePortFinder.java?view=markup#l130
     * Checks to see if a specific port is available.
     *
     * @param port the port to check for availability
     */
    public static boolean available(int port) {
        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    /* should not be thrown */
                }
            }
        }

        return false;
    }

    //=========================================
    // S T A R T
    //=========================================

    public static void main(String[]args){
        System.out.println("start");
        init("julian", new int[0]);
    }

}
