package ueb07;

import utils.Utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Julian on 19.12.13.
 */
public class TcpDictionaryClient implements Dictionary {

    public static void main(String[]args){
        Dictionary d = new TcpDictionaryClient();
    }

    @Override
    public String[] lookup(String word) {
        try {
            InetAddress local = InetAddress.getLocalHost();
            Utils.sendTCP(local, TcpDictionaryServer.PORT, word);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return new String[0];
    }
}
