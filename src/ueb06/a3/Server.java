package ueb06.a3;

import utils.Utils;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * User: Julian
 * Date: 10.12.13
 * Time: 21:21
 */
public class Server {

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
        int port = Integer.parseInt(args[0]);
        System.out.println("start listening..");
        int responsePort = Integer.parseInt(Utils.getTCP(port).get());
        String text = Utils.getTCP(port).get();

        boolean plus = true;
        boolean minus = false;
        String maybeEnglish = Filter.init(plus, "dicts/english.dic", -1, false,text);
        System.out.println("calculated words that are probably english words (rp:" + responsePort + ")" );
        String probablyEnglish = Filter.init(minus, "dicts/german.dic", responsePort, false,maybeEnglish);
        Utils.close();

    }

}
