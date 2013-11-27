package ueb05.a2;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

import static utils.Utils.*;

/**
 * Created with IntelliJ IDEA.
 * User: Julian
 * Date: 27.11.13
 * Time: 12:35
 */
public class Dod {

    private static final int PORT = 5000;
    private static final int TIMEOUT = 60000; // 1 Minute

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            serverSocket.setSoTimeout(TIMEOUT);
            Socket client = serverSocket.accept();
            handleConnection(client);
        } catch (IOException e) {
            System.out.println("hm, gab wohl nen Timeout.. (" + TIMEOUT + ") Millis!");
            e.printStackTrace();
        } finally{
            if (serverSocket != null){
                serverSocket.close();
            }
        }
    }

    /**
     * Behandlung bei Verbindung mit Client
     * @param client
     * @throws IOException
     */
    private static void handleConnection(Socket client) throws IOException {

        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        out.println("was geht?? Timo und Julians Server hier! Mach mal was:");

        Scanner in = new Scanner(client.getInputStream());

        String command = in.nextLine();
        System.out.println("command: " + command);

        String[] l = command.split(" ");
        String main = l[0];
        command = join(Arrays.copyOfRange(l,1,l.length), " ");

        Process p = fork(main);
        send(p, command);
        String result = read(p);
        System.out.println(result);

        out.println(result);
    }

}
