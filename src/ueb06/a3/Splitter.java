package ueb06.a3;

import sun.management.resources.agent_de;
import utils.Utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 * User: Julian
 * Date: 10.12.13
 * Time: 21:21
 */
public class Splitter {

    public static void main(String[] args) throws UnknownHostException, FileNotFoundException, ExecutionException, InterruptedException {
        int port1 = Integer.parseInt(args[0]);
        int port2 = Integer.parseInt(args[1]);
        InetAddress local = InetAddress.getLocalHost();

        String file = args[2];
        String[] words = Utils.wordsFromScanner(new Scanner(new FileReader(file)));

        String []words1 = Arrays.copyOfRange(words,0, words.length/2);
        String []words2 = Arrays.copyOfRange(words, (words.length/2) +1, words.length);
        String text1 = Utils.join(words1, " ");
        String text2 = Utils.join(words2, " ");
        Utils.sendTCP(local, port1, "6000");      // response
        Utils.sendTCP(local, port1, text1);
        Utils.sendTCP(local, port2, "6001");      // response
        Utils.sendTCP(local, port2, text2);

        String response1 = Utils.getTCP(5003).get();
        String response2 = Utils.getTCP(5004).get();

        System.out.println(response1);
        System.out.println("=============");
        System.out.println(response2);

        Utils.close();


    }

}
