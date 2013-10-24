package utils;

import java.io.*;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

/**
 * Created with IntelliJ IDEA.
 * User: Julian
 * Date: 16.10.13
 * Time: 13:37
 * To change this template use File | Settings | File Templates.
 */
public class Utils {

    public static void send(Process p,String command){
        OutputStream os = p.getOutputStream();
        try {
            os.write(command.getBytes());
        } catch (IOException e) {
            System.out.println("something went wrong... [Utils.send(..) -> " + e.getMessage());
        }finally {
            try {
                os.close();
            } catch (IOException e) {
                System.out.println("something went wrong while closing... [Utils.send(..) -> " + e.getMessage());
            }
        }
    }

    public static String read(Process p){
        StringBuilder sb = new StringBuilder();
        InputStream is = p.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String s = null;
        try {
            while ((s=reader.readLine()) != null){
                if (s.equals("") || s.equals(" ")) break;
                sb.append(s);
            }
        } catch (IOException e) {
            System.out.println("something went wrong... [Utils.read(..) -> " + e.getMessage());
        }
        return sb.toString();
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        Process p = fork("jutanke@peking.imp.fu-berlin.de:python");

        send(p, "a=6**2\nprint a");
        System.out.println(read(p));

    }

    /**
     * If you want to use
     * @param commandId
     * @return
     */
    public static Process fork(String commandId) {

        String username = null; // HARDCODE ME!
        String password = null; // HARDCODE ME!

        String host = null;
        String command = commandId;
        if (commandId.contains(":")){
            String[] temp = commandId.split(":");
            if (temp[0].length() > 2){
                // if the host is shorter its probably just a windows drive ('d:// ...')
                host = temp[0];
                if (host.contains("@")){
                    String[] t = host.split("@");
                    username = t[0];
                    host = t[1];
                }
                if (temp.length == 3){
                    command = temp[1] + ":" + temp[2]; // to "repair" windows drives...
                }else {
                    command = temp[1];
                }
            }
        }

        if(host !=null){
            Process remoteP = null;
            try {
                final Connection conn = new Connection(host);
                conn.connect();

                boolean isAuth = false;
                if (password != null){
                    isAuth = conn.authenticateWithPassword(username, password);
                }

                if(!isAuth){
                    File f = new File("private.pem");
                    isAuth = conn.authenticateWithPublicKey(username, f, "");
                    if(!isAuth)return null;
                }

                final  Session sess = conn.openSession();
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
