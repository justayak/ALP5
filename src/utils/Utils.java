package utils;

import java.io.*;
import java.util.List;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

/**
 * Created with IntelliJ IDEA.
 * User: Julian
 * Date: 16.10.13
 * Time: 13:37
 */
public class Utils {

    private static long currentTime;

    public static void stopwatchStart(){
        currentTime = java.lang.System.nanoTime();
    }

    public static <T> T[] listToArray(List<T> list){
        T[] result = (T[])new Object[list.size()];
        for(int i = 0; i < list.size(); i++){
            result[i] = list.get(i);
        }
        return result;
    }

    public static String[] listToArrayStr(List<String> list){
        String[] result = new String[list.size()];
        for(int i = 0; i < list.size(); i++){
            result[i] = list.get(i);
        }
        return result;
    }

    public static void stopwatchEnd(){
        long current = java.lang.System.nanoTime();
        long dif = current - currentTime;
        long millis = dif / 1000000;
        System.out.println("Millis: {" + millis + "} Nanos: {" + dif + "}" );
    }

    /**
     * Method to send a command to a Process
     *
     * @param p
     * @param command
     */
    public static void send(Process p, String command) {
        OutputStream os = p.getOutputStream();
        try {
            os.write(command.getBytes());
        } catch (IOException e) {
            System.out.println("something went wrong... [Utils.send(..) -> " + e.getMessage());
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                System.out.println("something went wrong while closing... [Utils.send(..) -> " + e.getMessage());
            }
        }
    }

    public static void close(Process p) {
        try {
            p.getOutputStream().close();
            p.getInputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * easy exceptionless sleep
     *
     * @param millis
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("ALP5: Utils::sleep crashed..");
        }

    }

    public static int countCharactersInFile(String fileName) {

        BufferedReader br = null;
        try {
            StringBuilder sb = new StringBuilder();
            br = new BufferedReader(new FileReader(fileName));
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }

            return sb.length();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("shit happens... @Utils.countCharactersInFile");
            return -1;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("shit happens while reading... @Utils.countCharactersInFile");
        } finally {
            if (br != null) try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
                return -2;
            }
        }
        return -3;
    }

    public static String join(String[] l, String connector) {
        StringBuilder sb = new StringBuilder();
        for (String s : l) {
            if (sb.length() > 0) {
                sb.append(connector);
            }
            sb.append(s);
        }
        return sb.toString();
    }

    /**
     * Method to receive the output of a Process
     *
     * @param p
     * @return
     */
    public static String read(Process p) {
        StringBuilder sb = new StringBuilder();
        InputStream is = p.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String s = null;
        try {
            while ((s = reader.readLine()) != null) {
                if (s.equals("") || s.equals(" ")) break;
                sb.append(s);
            }
        } catch (IOException e) {
            System.out.println("something went wrong... [Utils.read(..) -> " + e.getMessage());
        }
        return sb.toString();
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        System.out.println("yyy");
        System.out.println('a' > 'b');

    }

    /**
     * If you want to use your ssh-key-login, you need to generate a pem-File from
     * the ssh-private-key and put it into the main folder ( ALP5/ ); You also need
     * to define the user with @ (like: jutanke@peking.imp.fu-berlin.de:...)
     *
     * @param commandId
     * @return
     */
    public static Process fork(String commandId) {

        String username = null; // HARDCODE ME!
        String password = null; // HARDCODE ME!

        String host = null;
        String command = commandId;
        if (commandId.contains(":")) {
            String[] temp = commandId.split(":");
            if (temp[0].length() > 2) {
                // if the host is shorter its probably just a windows drive ('d:// ...')
                host = temp[0];
                if (host.contains("@")) {
                    String[] t = host.split("@");
                    username = t[0];
                    host = t[1];
                }
                if (temp.length == 3) {
                    command = temp[1] + ":" + temp[2]; // to "repair" windows drives...
                } else {
                    command = temp[1];
                }
            }
        }

        if (host != null) {
            Process remoteP = null;
            try {
                final Connection conn = new Connection(host);
                conn.connect();

                boolean isAuth = false;
                if (password != null) {
                    isAuth = conn.authenticateWithPassword(username, password);
                }

                if (!isAuth) {
                    File f = new File("private.pem");
                    isAuth = conn.authenticateWithPublicKey(username, f, "");
                    if (!isAuth) return null;
                }

                final Session sess = conn.openSession();
                sess.execCommand(command);

                remoteP = new Process() {
                    @Override
                    public OutputStream getOutputStream() {
                        return sess.getStdin();
                    }

                    @Override
                    public InputStream getInputStream() {
                        return sess.getStdout();
                    }

                    @Override
                    public InputStream getErrorStream() {
                        return sess.getStderr();
                    }

                    @Override
                    public int waitFor() throws InterruptedException {
                        sess.wait();
                        return 0;
                    }

                    @Override
                    public int exitValue() {
                        return 0;
                    }

                    @Override
                    public void destroy() {
                        sess.close();
                        conn.close();
                    }
                };

            } catch (IOException e) {
                System.out.println("shit happens with the ssh connection: @Utils.fork .. " + e.getMessage());
                return null;
            }
            return remoteP;
        }

        ProcessBuilder b = new ProcessBuilder(command.split(" "));
        try {
            return b.start();
        } catch (IOException e) {
            System.out.println("shit happens: @Utils.fork .. " + e.getMessage());
        }
        return null;
    }
}
