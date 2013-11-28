package ueb05.a2;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

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

            //String text = new String(Files.readAllBytes(Paths.get(args[1])), StandardCharsets.UTF_8);


            BufferedReader br = new BufferedReader(new FileReader(f));
            String t = null;
            String text = "";
            while ((t = br.readLine()) != null) {
                text += t;
            }

            String param = program + " " + text;

            final Socket s = new Socket(local, PORT);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));

            new Thread() {
                public void run() {
                    while (true) {
                        try {
                            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                            while (true) {
                                System.out.println(br.readLine());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.run();

            writer.print("py print(5+5)");
            writer.flush();


        } else {
            System.out.println("nop.. falsche Anzahl an Params (2 erwartet)");
        }
    }


}
