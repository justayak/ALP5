package ueb04.a2;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * User: Julian
 * Date: 20.11.13
 * Time: 23:26
 */
public class Comm2 {

    private static RoutingTable Routing = null;

    public static void init(String name, int[] peers){

        // sind wir der erste Knoten?
        if (!available(FirstPort)){  // zugegeben: das ist ein schwaches Argument..

            // Wir sind der erste Knoten!
            Routing = new RoutingTable(name);

        } else{

            // Wir sind nicht der erste Knoten!
            // finde die anderen und update das Netzwerk

        }

    }

    /*================================================
    N E T W O R K  M E T H O D S
     ================================================*/

    /**
     * Sendet eine Nachricht über das Netzwerk an das Ziel
     * @param port
     * @param m
     */
    private static void send(int port, Message m){
        try {
            InetAddress local = InetAddress.getLocalHost();
            DatagramSocket toSocket = new DatagramSocket();
            DatagramPacket packet = new DatagramPacket(m.toString().getBytes("UTF-8"),100,port);
            toSocket.send(packet);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
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


    /*================================================
    S U P P O R T I N G  C L A S S E S
     ================================================*/

    private static int FirstPort = 5000;

    /**
     * In Stringform:
     * "NAME,PORT,DISTANCE;NAME2,PORT2,DISTANCE2"
     */
    private static class RoutingTable {
        private int[] distances;
        private int[] ports;
        private String[] names;
        private HashMap<String, Integer> nameToPosition = new HashMap<String, Integer>();

        /**
         * Erzeugt Routingtabelle mit initialer Größe
         * @param name
         */
        public RoutingTable(String name){
            this.nameToPosition.put(name,0);
            this.distances = new int[1];
            this.ports = new int[1];
            this.names = new String[1];
        }

        public RoutingTable(List<Message> messages, String name){
            List<RoutingTable> peers = new ArrayList<RoutingTable>();
            for(Message m : messages){
                peers.add(new RoutingTable(m));
            }
            RoutingTable o = peers.get(0);
            if(peers.size() == 1){
                for(int i = 0; i < o.length();i++){
                    this.distances[i] = o.distance(i) + 1;
                    this.ports[i] = o.port(i);
                    this.names[i] = o.name(i);
                    this.nameToPosition.put(o.name(i),i);
                }
            }else{
                for(int i = 0; i < o.length();i++){
                    RoutingTable lowest = o;
                    for(RoutingTable t : peers){
                        if(t.distance(i) < lowest.distance(i)){
                            lowest = t;
                        }
                    }
                    this.distances[i] = lowest.distance(i) + 1;
                    this.ports[i] = lowest.port(i);
                    this.names[i] = lowest.name(i);
                    this.nameToPosition.put(lowest.name(i),i);
                }
            }
        }

        private RoutingTable(Message m){
            String[] t = m.Message.split(";");
            this.distances = new int[t.length];
            this.ports = new int[t.length];
            this.names = new String[t.length];
            for(int i =0; i < t.length; i++){
                String[]r = t[i].split(",");
                this.distances[i] = Integer.parseInt(r[2]);
                this.ports[i] = Integer.parseInt(r[1]);
                this.names[i] = r[0];
                this.nameToPosition.put(r[0],i);
            }
        }

        public int length(){
            return this.distances.length;
        }

        public int distance(int i){
            return this.distances[i];
        }

        public int port(int i){
            return this.ports[i];
        }

        public String name(int i){
            return this.names[i];
        }

        public int distance(String name){
            int i = this.nameToPosition.get(name);
            return this.distances[i];
        }

        public int port(String name){
            int i = this.nameToPosition.get(name);
            return this.ports[i];
        }

        public String name(String name){
            int i = this.nameToPosition.get(name);
            return this.names[i];
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < this.distances.length;i++){
                if(sb.length() > 0){
                    sb.append(";");
                }
                sb.append(this.names[i]);
                sb.append(",");
                sb.append(this.ports[i]);
                sb.append(",");
                sb.append(this.names[i]);
            }
            return sb.toString();
        }

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        private void grow(){
            int[] d = new int[this.distances.length + 1];
            int[] p = new int[this.ports.length + 1];
            String[] n = new String[this.names.length + 1];
            for(int i = 0; i < this.distances.length;i++){
                d[i]=this.distances[i];
                p[i]=this.ports[i];
                n[i]=this.names[i];
            }
            this.distances = d;
            this.ports = p;
            this.names = n;
        }



    }

    private enum MessageType{
        Message,        //  0
        Update,         //  1
        Connect,        //  2
        ConnectAccept   //  3
    }

    /**
     * Nachrichten zum versenden über das Netzwerk:
     * Aufbau:
     *      TYPE | PORT | NAME | DATA
     *
     *      0 = Message: DATA == Message
     *   Bsp: "0|5001|timo|hallo welt"
     *
     *      1 = Update: DATA == Peers
     *   Bsp: "1|5002|julian|5001,5000"
     *
     *      2 = Connect
     *      Bsp:2|5003|alex
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
        public String Name;
        public MessageType Type;
        public List<Integer> Peers = new ArrayList<Integer>();
        public Message(byte [] data){
            String[] t = null;
            try {
                t = new String(data,"UTF-8").split("|");
            } catch (UnsupportedEncodingException e) {
                System.out.println("nop wtf");
            }
            Name = t[2];
            Port = Integer.parseInt(t[1]);
            switch (Integer.parseInt(t[0])){

                case 0:
                    Type = MessageType.Message;
                    Message = t[3];
                    break;
                case 1:
                    Type = MessageType.Update;
                    for(String p : t[3].split(",")){
                        Peers.add(Integer.parseInt(p));
                    }
                    break;
                case 2:
                    Type = MessageType.Connect;
                    break;

            }
        }

        public Message(int port, String name, RoutingTable t){
            this.Port = port;
            this.Name = name;
            this.Type = MessageType.ConnectAccept;
            this.Message = t.toString();
        }

        public Message(int port, String name,String message){
            this.Port = port;
            this.Name = name;
            this.Message = message;
            this.Type = MessageType.Message;
        }

        public Message(int port, String name,List<Integer> peers){
            this.Port = port;
            this.Name = name;
            this.Message = "";
            for(int peer : peers){
                if (this.Message.length() != 0){
                    this.Message += ",";
                }
                this.Message += peer;
            }
            this.Type = MessageType.Update;
        }

        @Override
        public String toString(){
            switch (this.Type){
                case ConnectAccept:
                    return "3|" + this.Port + "|" + this.Name + "|"  + this.Message;
                case Connect:
                    return "2|" + this.Port + "|" + this.Name;
                case Update:
                    return "1|" + this.Port + "|" + this.Name + "|" + this.Message;
                case Message:
                    return "0|" + this.Port + "|" + this.Name + "|" + this.Message;
            }
            return null;
        }
    }



}
