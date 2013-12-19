package ueb07;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by Julian on 19.12.13.
 */
public class UdpDictionaryServer extends Translator {

    public static final int PORT = 5001;

    public static void main(String[]args) throws IOException {
        Translator t = new UdpDictionaryServer();
    }

    public UdpDictionaryServer() throws IOException {
        DatagramSocket server = new DatagramSocket(PORT);
        byte[] rec = new byte[2048];
        byte[] send = null;

        while (true){
            DatagramPacket p = new DatagramPacket(rec, rec.length);
            server.receive(p);
            String word = new String(p.getData());
            InetAddress ip = p.getAddress();
            int port = p.getPort();
            String result = this.translateEncoded(word.trim());
            send = result.getBytes();
            DatagramPacket sp = new DatagramPacket(send, send.length,ip,port);
            server.send(sp);
            System.out.println("i10e: " + word + " -> " + result);

        }

    }
}