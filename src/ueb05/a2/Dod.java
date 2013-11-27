package ueb05.a2;

import utils.Utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

import static utils.Utils.*;

/**
 * User: Julian
 * Date: 27.11.13
 * Time: 12:35
 */
public class Dod {

    private static final int PORT = 5000;
    private static final int TIMEOUT = 60000; // Millis

    public static void main(String[] args) throws IOException {

        Process a = fork("py");
        send(a, "print(a+2)");
        System.out.println(read(a) + " | ");

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            serverSocket.setSoTimeout(TIMEOUT);
            while (true){
                Socket client = serverSocket.accept();
                handleConnection(client);
            }
        } catch (IOException e) {
            System.out.println("hm, gab wohl nen Timeout.. (" + TIMEOUT + ") Millis!");
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
    }

    /**
     * Behandlung bei Verbindung mit Client
     *
     * @param client
     * @throws IOException
     */
    private static void handleConnection(final Socket client) throws IOException {

        new Thread() {
            public void run() {
                try {
                    boolean isFirst = true;
                    PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                    out.println("was geht?? Timo und Julians Server hier! Mach mal was:");
                    Process p = null;
                    while (true) {
                        Scanner in = new Scanner(client.getInputStream());
                        String command = in.nextLine();
                        System.out.println("command: " + command);
                        if (isFirst){
                            String[] l = command.split(" ");
                            String main = l[0];
                            command = Utils.join(Arrays.copyOfRange(l, 1, l.length), " ");
                            p = fork(main);
                            isFirst = false;
                        }
                        if (command.equals("::quit")) break;
                        send(p, command);
                        String result = read(p);
                        System.out.println(result);
                        out.println(result);
                        //out.println("go on? (y or n)");
                        //isRunning = (in.nextLine().charAt(0) == 'y');
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.run();
    }
}