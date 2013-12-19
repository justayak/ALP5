package ueb07;

import utils.Utils;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Julian on 19.12.13.
 */
public class TcpDictionaryClient implements Dictionary {

    public static void main(String[]args){
        Dictionary d = new TcpDictionaryClient();
        d.lookup("tree");
    }

    @Override
    public String[] lookup(String word) {
        try {
            InetAddress local = InetAddress.getLocalHost();
            Socket server = Utils.sendTCP(local, TcpDictionaryServer.PORT, word);
            String result = Utils.getTCPSync(server);
            System.out.println(result);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return new String[0];
    }
}
