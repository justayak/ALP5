package ueb07;

import java.io.IOException;
import java.net.*;

/**
 * Created by Julian on 19.12.13.     jo
 */
public class UdpDictionaryClient implements Dictionary {
    @Override
    public String[] lookup(String word) {

        try {
            DatagramSocket client = new DatagramSocket();
            InetAddress ip = InetAddress.getLocalHost();
            byte[] send = word.getBytes();
            byte[] rec = new byte[2048];
            DatagramPacket sp = new DatagramPacket(send, send.length,ip,UdpDictionaryServer.PORT);
            client.send(sp);
            DatagramPacket recp = new DatagramPacket(rec, rec.length);
            client.receive(recp);
            String result = new String(recp.getData());
            System.out.println(result);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String[0];
    }
}
