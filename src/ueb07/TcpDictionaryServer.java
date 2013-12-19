package ueb07;

import utils.Utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Julian on 19.12.13.
 */
public class TcpDictionaryServer extends Translator{
    public static void main(String[] args){
        Translator x = new TcpDictionaryServer();
    };

    public static final int PORT = 5000;
    private Socket current = null;

    public TcpDictionaryServer(){
        while(true){
            String answer = encode(this.translate(this.await()));
            Utils.sendTCP(current,answer);
            System.out.println("send translation: " + answer);
        }

    }

    /**
     * Encodes the result:
     * <> is token for "null"
     * trans1|trans2|trans3 is a list seperated by the "|" symbol
     * @param trans
     * @return
     */
    private String encode(String[] trans){
        if (trans == null){
            return "<>";
        }
        StringBuilder sb = new StringBuilder();
        for(String word : trans){
            if (sb.length() > 0) sb.append("|");
            sb.append(word);
        }
        return sb.toString();
    }

    private String await() {
        Utils.SyncTcpResponse response = Utils.getTCPSync(PORT);
        if(!response.isValid()){
            System.out.println("fehler bei der übertragung..");
            return null;
        }else{
            current =  response.socket;
            return response.message;
        }
    }

}
