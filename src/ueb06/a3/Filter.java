package ueb06.a3;

import utils.Utils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * User: Julian
 * Date: 04.12.13
 * Time: 20:16
 */
public class Filter {

    public static void main(String[] args) throws IOException {
        if (args.length < 2) throw new RuntimeException("Wrong nbr of params!");
        boolean plus = args[0].charAt(0) == '+';
        boolean isServer = false;
        String language = args[1];
        int hostPort = -1;
        if (args.length >= 4) {
            if (args[2].equals("-c")) {
                hostPort = Integer.parseInt(args[3]);
            }
        }
        if ((args.length > 2 && args[2].equals("-s")) || (args.length >= 5 && args[4].equals("-s"))) {
            isServer = true;
        }
        // Programm kann beginnen...

        long start = System.currentTimeMillis();

        String[] text = getText(isServer);

        Scanner langScanner = new Scanner(new FileReader(language));
        String[] dict =  wordsFromScanner(langScanner);

        String[] result = filter(text, plus, dict);

        if (hostPort == -1){
            StringBuilder b = new StringBuilder();
            for(String word : result){
                b.append(word);
                b.append("\n");
            }
            System.out.println(b.toString());
        }else {
            sendText(hostPort, result);
        }

        long end = System.currentTimeMillis();
        double secs = (end - start) / 1000.0;
        //System.out.println("dif: " + (end - start) + " millis | secs: " + secs);

    }


    private static String[] filter(String[] words, boolean plus, String[] dict) {
        List<String> result = new ArrayList<String>();
        HashSet<String> dictionary = new HashSet<String>(Arrays.asList(dict));
        for(String word : words){
            if(plus){
                if(dictionary.contains(word)) result.add(word);
            }else{
                if(!dictionary.contains(word)) result.add(word);
            }
        }
        return Utils.<String>listToArrayStr(result);
    }

    private static void sendText(int port, String[] words) {

    }

    public static String[] getText(boolean isServer) throws IOException {
        String[] result;
        if (isServer) {
            final int port = 5000;
            ServerSocket server = new ServerSocket(port);
            Socket client = server.accept();
            Scanner scanner = new Scanner(client.getInputStream());
            result = wordsFromScanner(scanner);
        } else {
            // Ließ von der Console
            System.out.println("please specify the path to input file:");
            String path = new BufferedReader(new InputStreamReader(System.in)).readLine();

            File f = new File(path);
            System.out.println(f.exists());

            Scanner scanner = new Scanner(new FileReader(path));
            result = wordsFromScanner(scanner);
        }
        return result;
    }

    private static String[] wordsFromScanner(Scanner scanner){
        List<String> result = new ArrayList<String>();
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            String[] words = line.split(" ");
            for(String word : words){
                if (word.length() > 0)
                    result.add(word.replace(",", "").replace(".", "").replace("'", "").replace("\"", "")
                            .replace("...", "").replace("!","").replace(";","").replace(":", "").toLowerCase());
            }
        }
        String[] r = Utils.<String>listToArrayStr(result);
        return r;
    }

}
