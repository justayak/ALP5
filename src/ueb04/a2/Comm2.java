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

    private static RoutingTable _routing = null;
    private static Listener _listener;
    private static Thread _thread;
    private static int _port;
    private static String _name;
    private static List<Message> _pendingTablesFromPeers = new ArrayList<Message>();
    private static int _pendingPeersCounter = 0;
    private static int[] _peers;
    private static int _updateId = -1;

    public static void main(String[] args) throws InterruptedException {
        _name = "erster";
        _peers = new int[0];
        if (args.length > 0) {
            _name = args[0];
            if (args[1].contains(",")) {
                String[] t = args[1].split(",");
                _peers = new int[t.length];
                for (int i = 0; i < t.length; i++) {
                    _peers[i] = Integer.parseInt(t[i]);
                }
            } else {
                _peers = new int[1];
                _peers[0] = Integer.parseInt(args[1]);
            }
        }
        init(_name, _peers);

        _thread.join();
    }

    public static void init(String name, int[] peers) {

        // sind wir der erste Knoten?
        if (available(FirstPort)) {  // zugegeben: das ist ein schwaches Argument..

            // Wir sind der erste Knoten!
            _port = FirstPort;
            _routing = new RoutingTable(name, _port);
            System.out.println("Wir sind der erste Knoten.. gestartet!");
            listen(_port);

        } else {

            System.out.println("Versuche, das Netzwerk zu betreten..");

            // Wir sind nicht der erste Knoten!
            // finde die anderen und update das Netzwerk
            if (peers.length == 0) {
                throw new RuntimeException("Lol keine Peers zum verbinden.. zu bloed..");
            }

            _port = findFreePort();

            System.out.println("my port: " + _port);

            // Lausche auf eingehende Nachrichten
            listen(_port);

            Message m = new Message(_port, name); // Erzeugt eine "Connect"-Nachricht
            _pendingPeersCounter = peers.length;
            for (int peer : peers) {
                send(peer, m);
                // weiter gehts in   "connectAccepted"
            }
        }

    }

    public static void stop() {
        if (_listener != null) _listener.kill = true;
        if (_thread != null) _thread.interrupt();
    }

    /*================================================
    P R O T O C O L S
     ================================================*/

    private static void forward(Message m) {

    }

    private static void update(Message m) {
        if (_routing == null) throw new RuntimeException("Build-Vorgang noch nicht abgeschlossen!");

        if (!_routing.hasPort(m.Port)) {
            // führe nur dann ein Update durch, wenn wir den Port noch nicht in unsere
            // Liste eingetragen haben!
            //System.out.println("update! " + m.toString());

            if (isIn(_port, m.Peers)) {
                _routing.add(m.Name, m.Port, 1);
                int[] newPeers = new int[_peers.length + 1];
                int i = 0;
                for (; i < _peers.length; i++) {
                    newPeers[i] = _peers[i];
                }
                newPeers[i] = m.Port;
            } else {
                int closest = m.Peers.get(0);
                int closestDistance = _routing.distanceByPort(closest);
                for (int peer : m.Peers) {
                    if (closestDistance > _routing.distanceByPort(peer)) {
                        closest = peer;
                        closestDistance = _routing.distanceByPort(peer);
                    }
                }
                _routing.add(m.Name, m.Port, closestDistance + 1);
            }

            for (int peer : _peers) {
                send(peer, m);
            }

        } else {
            System.out.println("discard update..");
        }
        // leite die Nachricht weiter!

        System.out.println(_routing.toString());
    }

    private static void connect(Message m) {
        if (_routing == null) throw new RuntimeException("Build-Vorgang noch nicht abgeschlossen!");
        System.out.println("<connect> from " + m.Port);
        Message response = new Message(_port, _name, _routing);
        send(m.Port, response);
    }

    private static void connectAccepted(Message m) {
        System.out.println("<connectaccept> from " + m.Port);
        System.out.println("m: " + m.toString());
        _pendingTablesFromPeers.add(m);
        _pendingPeersCounter--;
        if (_pendingPeersCounter == 0) {
            // wenn wir hier landen, dann haben wir alle peers erhalten
            _routing = new RoutingTable(_pendingTablesFromPeers, _name, _port);
            Message update = new Message(_port, _name, _peers);
            for (int peer : _peers) {
                send(peer, update);
            }
        }
    }

    /*================================================
    N E T W O R K  M E T H O D S
     ================================================*/

    private static void listen(int port) {
        _listener = new Listener(port);
        _thread = new Thread(_listener);
        _thread.start();
    }

    private static class Listener implements Runnable {
        public volatile boolean kill = false;
        private final int port;

        public Listener(int port) {
            this.port = port;
        }

        @Override
        public void run() {
            try {
                DatagramSocket socket = new DatagramSocket(this.port);
                while (!this.kill) {
                    DatagramPacket p = new DatagramPacket(new byte[1000], 1000);
                    socket.receive(p);
                    Message m = new Message(p.getData());
                    switch (m.Type) {
                        case Connect:
                            connect(m);
                            break;
                        case ConnectAccept:
                            connectAccepted(m);
                            break;
                        case Message:
                            forward(m);
                            break;
                        case Update:
                            update(m);
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("damn:Listener->run");
            }
        }
    }

    private static int findFreePort() {
        for (int i = FirstPort; i < FirstPort + 20; i++) {
            if (available(i)) return i;
        }
        throw new RuntimeException("fuck! es sind alle Ports aufgebraucht..");
    }

    /**
     * Sendet eine Nachricht über das Netzwerk an das Ziel
     *
     * @param port
     * @param m
     */
    private static void send(int port, Message m) {
        try {
            InetAddress local = InetAddress.getLocalHost();
            DatagramSocket toSocket = new DatagramSocket();
            byte[] stuff = m.toString().getBytes("UTF-8");
            System.out.println("sending: " + m.toString());
            DatagramPacket packet = new DatagramPacket(stuff, 0, stuff.length, local, port);
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

    private static boolean isIn(int s, List<Integer> e) {
        for (int i : e) {
            if (i == s) return true;
        }
        return false;
    }

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
         *
         * @param name
         */
        public RoutingTable(String name, int port) {
            this.nameToPosition.put(name, 0);
            this.distances = new int[1];
            this.ports = new int[1];
            this.names = new String[1];
            this.names[0] = name;
            this.ports[0] = port;
        }

        public RoutingTable(List<Message> messages, String name, int port) {
            List<RoutingTable> peers = new ArrayList<RoutingTable>();
            for (Message m : messages) {
                peers.add(new RoutingTable(m));
            }
            RoutingTable o = peers.get(0);
            int i = 0;
            this.distances = new int[o.length() + 1];
            this.ports = new int[o.length() + 1];
            this.names = new String[o.length() + 1];
            if (peers.size() == 1) {
                for (i = 0; i < o.length(); i++) {
                    this.distances[i] = o.distance(i) + 1;
                    this.ports[i] = o.port(i);
                    this.names[i] = o.name(i);
                    this.nameToPosition.put(o.name(i), i);
                }
            } else {
                for (i = 0; i < o.length(); i++) {
                    RoutingTable lowest = o;
                    for (RoutingTable t : peers) {
                        if (t.distance(i) < lowest.distance(i)) {
                            lowest = t;
                        }
                    }
                    this.distances[i] = lowest.distance(i) + 1;
                    this.ports[i] = lowest.port(i);
                    this.names[i] = lowest.name(i);
                    this.nameToPosition.put(lowest.name(i), i);
                }
            }
            this.distances[i] = 0;
            this.ports[i] = port;
            this.names[i] = name;
        }

        private RoutingTable(Message m) {
            String[] t = m.Message.split(";");
            this.distances = new int[t.length];
            this.ports = new int[t.length];
            this.names = new String[t.length];
            for (int i = 0; i < t.length; i++) {
                String[] r = t[i].split(",");
                this.distances[i] = Integer.parseInt(r[2]);
                this.ports[i] = Integer.parseInt(r[1]);
                this.names[i] = r[0];
                this.nameToPosition.put(r[0], i);
            }
        }

        public void add(String name, int port, int distance) {
            int p = this.length();
            this.nameToPosition.put(name, p);
            this.grow();
            this.names[p] = name;
            this.ports[p] = port;
            this.distances[p] = distance;

        }

        public int length() {
            return this.distances.length;
        }

        public boolean hasPort(int port) {
            return this.length() > (port - FirstPort);
        }

        public int distance(int i) {
            return this.distances[i];
        }

        public int port(int i) {
            return this.ports[i];
        }

        public String name(int i) {
            return this.names[i];
        }

        public int distance(String name) {
            int i = this.nameToPosition.get(name);
            return this.distances[i];
        }

        public int port(String name) {
            int i = this.nameToPosition.get(name);
            return this.ports[i];
        }

        public String name(String name) {
            int i = this.nameToPosition.get(name);
            return this.names[i];
        }

        public int distanceByPort(int port) {
            return this.distances[port - FirstPort];
        }

        // jaaa.. ein wenig sinnlos
        public int PortByPort(int port) {
            return this.ports[port - FirstPort];
        }

        public String nameByPort(int port) {
            return this.names[port - FirstPort];
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < this.distances.length; i++) {
                if (sb.length() > 0) {
                    sb.append(";");
                }
                sb.append(this.names[i]);
                sb.append(",");
                sb.append(this.ports[i]);
                sb.append(",");
                sb.append(this.distances[i]);
            }
            return sb.toString();
        }

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        private void grow() {
            int[] d = new int[this.distances.length + 1];
            int[] p = new int[this.ports.length + 1];
            String[] n = new String[this.names.length + 1];
            for (int i = 0; i < this.distances.length; i++) {
                d[i] = this.distances[i];
                p[i] = this.ports[i];
                n[i] = this.names[i];
            }
            this.distances = d;
            this.ports = p;
            this.names = n;
        }
    }

    private enum MessageType {
        Message,        //  0
        Update,         //  1
        Connect,        //  2
        ConnectAccept   //  3
    }

    /**
     * Nachrichten zum versenden über das Netzwerk:
     * Aufbau:
     * TYPE | PORT | NAME | DATA
     * <p/>
     * 0 = Message: DATA == Message
     * Bsp: "0|5001|timo|hallo welt"
     * <p/>
     * 1 = Update: DATA == Peers
     * Bsp: "1|5002|julian|5001,5000"
     * <p/>
     * 2 = Connect
     * Bsp:2|5003|alex
     * <p/>
     * ACHTUNG: es gibt keine Fehlerbehandlung
     * <p/>
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

        public Message(byte[] data) {
            //t = new String(data,"UTF-8").split("|");
            String s = "";
            for (byte b : data) {
                if (b == 0) break;
                s += (char) b;
            }
            String[] t = s.split("\\|");

            Name = t[2];
            Port = Integer.parseInt(t[1]);
            switch (Integer.parseInt(t[0])) {

                case 0:
                    this.Type = MessageType.Message;
                    this.Message = t[3];
                    break;
                case 1:
                    this.Type = MessageType.Update;
                    for (String p : t[3].split(",")) {
                        Peers.add(Integer.parseInt(p));
                    }
                    this.Message = t[3];
                    break;
                case 2:
                    this.Type = MessageType.Connect;
                    break;
                case 3:
                    this.Type = MessageType.ConnectAccept;
                    this.Message = t[3];
                    break;
            }
        }

        public Message(int port, String name, RoutingTable t) {
            this.Port = port;
            this.Name = name;
            this.Type = MessageType.ConnectAccept;
            this.Message = t.toString();
        }

        public Message(int port, String name, String message) {
            this.Port = port;
            this.Name = name;
            this.Message = message;
            this.Type = MessageType.Message;
        }

        public Message(int port, String name) {
            this.Port = port;
            this.Name = name;
            this.Type = MessageType.Connect;
        }

        public Message(int port, String name, int[] peers) {
            this.Port = port;
            this.Name = name;
            this.Message = "";
            for (int peer : peers) {
                if (this.Message.length() != 0) {
                    this.Message += ",";
                }
                this.Message += peer;
            }
            this.Type = MessageType.Update;
        }

        @Override
        public String toString() {
            switch (this.Type) {
                case ConnectAccept:
                    return "3|" + this.Port + "|" + this.Name + "|" + this.Message;
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