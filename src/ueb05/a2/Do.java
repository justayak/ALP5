package ueb05.a2;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * User: Julian
 * Date: 28.11.13
 * Time: 18:44
 */
public class Do {

    public static void main(String[] args) throws IOException {
        if (args.length == 2) {
            final int PORT = 5000;
            InetAddress local = InetAddress.getLocalHost();
            String program = args[0];
            File f = new File(args[1]);
            BufferedReader br = new BufferedReader(new FileReader(f));
            String t = null;
            String text = "";
            while ((t = br.readLine()) != null) {
                text += t;
            }
            // Once ist ein Serverflag, um den gegebenen Befehl nur ein
            // einziges mal aufzurufen! Once muss der 2te Parameter sein
            final String ONCE = "!!once!!";
            String param = program + " " + ONCE + " " + text;
            final Socket s = new Socket(local, PORT);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));

            Scanner in = new Scanner(s.getInputStream());

            // Die "Empfangsnachricht"
            System.out.println(in.nextLine());

            // schicke das Command
            writer.write(param);
            writer.close();

            // Der eigentliche Funktionswert
            System.out.println(in.nextLine());

        } else {
            System.out.println("nop.. falsche Anzahl an Params (2 erwartet)");
        }
    }


}
