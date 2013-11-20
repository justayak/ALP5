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

    private static HashMap<String, Integer> NameToPort = new HashMap<String, Integer>();

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
                    return "";
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
