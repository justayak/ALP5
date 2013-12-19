package ueb07;

import java.io.IOException;
import java.net.*;

/**
 * Created by Julian on 19.12.13.     jo
 */
public class UdpDictionaryClient extends Translator implements Dictionary {

    public static void main(String[]args){
        Dictionary d = new UdpDictionaryClient();
        String[] result = d.lookup("failure");
        if (result == null){
            System.out.println("nix..");
        }else{
            for(String word : result){
                System.out.println(word);
            }
        }
    }

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
            return decode(result);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
