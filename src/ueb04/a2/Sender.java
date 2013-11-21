package ueb04.a2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: Julian
 * Date: 13.11.13
 * Time: 13:00
 * To change this template use File | Settings | File Templates.
 */
public class Sender {


    public static void main(String[]args) throws IOException {

        String name = args[0];
        int[] ports = new int[0];
        if (args.length > 1){
            ports = new int[1];
            ports[0] = Integer.parseInt(args[1]);
        }

        Comm2.init(name,ports);

        while (true){
            System.out.println("Geben Sie eine Nachricht ein:");

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String message = br.readLine();

            System.out.println("geben Sie das Zeil ein:");
            String dest = br.readLine();

            Comm2.SEND(message,dest);
        }

    }


}
