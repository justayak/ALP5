package ueb04.a2;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * User: Julian
 * Date: 18.11.13
 * Time: 23:22
 */
public class Comm {

    private static String name;
    private static int[] peers;
    private static BlockingQueue<String> mailbox;


    public static void init(String n, int[] p){
        name = n;
        peers = p;
        mailbox = new ArrayBlockingQueue<String>(10);
        if (initNetwork()){


        }
    }

    public static void SEND(String message, String destination){

    }

    public static String RECV(){
        return null;
    }

    /**
     * Klasse, die eine Zelle in der Routingtabelle darstellt
     */
    private static class RoutingCell {

        public final int Port;
        public int ClosestPort;
        public int Distance;

        RoutingCell (int port){
            this.Port = port;
        }

        public void forward(Message m) {
            try {
                DatagramSocket toSocket = new DatagramSocket();
                DatagramPacket packet = new DatagramPacket(m.toString().getBytes("UTF-8"),100,address, this.Port);
                toSocket.send(packet);
            } catch (SocketException e) {
                e.printStackTrace();
                System.out.println("ah fuck, @RoutingCell->forward");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                System.out.println("ah fuck2, @RoutingCell->forward");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("ah fuck3, @RoutingCell->forward");
            }


        }
    }

    private enum MessageType{
        Message  ,  // 0
        Update      // 1
    }

    /**
     * Nachrichten zum versenden über das Netzwerk:
     * Aufbau:
     *      TYPE | PORT | DATA
     *
     *      0 = Message: DATA == Message
     *   Bsp: "0|5001|hallo welt"
     *
     *      1 = Update: DATA == Peers
     *   Bsp: "1|5002|5001,5000"
     *
     *  ACHTUNG: es gibt keine Fehlerbehandlung
     *
     * "DEST-PORT|MESSAGE"  (Achtung -> UTF8!)
     * Bsp: "5001|Hallo Gunter"
     * Trennzeichen: '|'
     */
    private static class Message {
        public final int Port;
        public String Message;
        public MessageType Type;
        public List<Integer> Peers = new ArrayList<Integer>();
        public Message(byte [] data){
            String[] t = null;
            try {
                t = new String(data,"UTF-8").split("|");
            } catch (UnsupportedEncodingException e) {
                System.out.println("nop wtf");
            }
            int type = Integer.parseInt(t[0]);
            Port = Integer.parseInt(t[1]);
            if (type == 0){
                Type = MessageType.Message;
                Message = t[2];
            }else {
                Type = MessageType.Update;
                for(String p : t[2].split(",")){
                    Peers.add(Integer.parseInt(p));
                }
            }
        }
        public Message(RoutingCell c,String message){
            this.Port = c.Port;
            this.Message = message;
        }

        @Override
        public String toString(){
            return Port + "|" + Message;
        }
    }

    /**
     * Helfer...
     */
    private static class Listener implements Runnable{

        public volatile boolean kill = false;

        @Override
        public void run() {
            while (!this.kill){
                DatagramPacket p = new DatagramPacket(new byte[100],100);
                try {
                    socket.receive(p);
                    Message m = new Message(p.getData());

                    if(m.Type == MessageType.Message){
                        forward(m);
                    }else {
                        update(m);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("damn:Listener->run");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("damn:Listerner->run (Interrupt)");
                }
            }
        }
    }

    //=========================================
    // M E S S A G I N G
    //=========================================

    private static int FirstPort =  5000;
    static DatagramSocket socket;
    static InetAddress address;
    static Listener l;
    static Thread t;
    static int id = -1;

    static boolean initNetwork(){
        try {
            address = InetAddress.getLocalHost();

            for(int i = FirstPort; i < FirstPort + 10; i++){
                if (available(i)){
                    id = i;
                    break;
                }
            }

            if (id == -1){
                System.out.println("exceeded::No free Ports anymore..");
                return false;
            }

            socket = new DatagramSocket(id);
            System.out.println("using port: " + id);

            l = new Listener();
            t = new Thread(l); // beginne, auf Nachrichten zu lauschen

        } catch (SocketException e) {
            e.printStackTrace();
            System.out.println("Comm -> initNetwork _ shit !");
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.out.println("Comm -> initNetwork _ Localhost fliegt uns um die Ohren");
        }
        return true;
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

    public static void unload(){
        if(l!=null){
            l.kill = true;
        }
        if(t!=null){
            t.interrupt();
        }
    }

    //=========================================
    // N E T P R O T O C O L
    //=========================================

    static List<RoutingCell> table = new ArrayList<RoutingCell>();

    /**
     *
     * @param m
     */
    private static void forward(Message m) throws InterruptedException {
        if(id == m.Port){
            mailbox.put(m.Message);
        }else{
            table.get(m.Port - FirstPort).forward(m);
        }
    }

    /**
     *
     * @param m
     */
    private static void update(Message m){
        if (m.Peers.contains(id)){
            RoutingCell cell = new RoutingCell(m.Port);
            cell.Distance = 1;
            table.add(m.Port - FirstPort, cell);
        }else{
            int closestDist = Integer.MAX_VALUE;

            for(int p : m.Peers){

            }
        }
    }

    //=========================================
    // S T A R T
    //=========================================

    public static void main(String[]args){
        System.out.println("start");
        init("julian", new int[0]);
    }

}
