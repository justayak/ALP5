package utils;

import org.omg.SendingContext.RunTime;
import sun.misc.IOUtils;      // stuff

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: Julian
 * Date: 16.10.13
 * Time: 13:37
 * To change this template use File | Settings | File Templates.
 */
public class Utils {

    public static void main(String[] args) throws IOException {

        ProcessBuilder b = new ProcessBuilder("cmd");
        //Process p = b.start();


        if (true) return;

        BufferedReader inp = null;
        BufferedWriter out = null;
        try {

            Process p = fork("cmd");

            inp = new BufferedReader(new InputStreamReader(p.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));

            InputStream in = new BufferedInputStream( p.getInputStream());
;
            out.write("ping localhost\n");
            out.flush();

            System.out.println("send to console");

            /*int cnt;
            byte[] buffer = new byte[1024];
            while ( (cnt = in.read(buffer)) != -1) {
                System.out.println(cnt);
            } */

            System.out.println(inp.readLine());


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inp != null) inp.close();
            if (out != null) out.close();
        }


    }

    /**
     * way cleaner declaration
     *
     * @param command
     * @param host
     * @param user
     * @param password
     * @return
     */
    public static Process fork(String command, String host, String user, String password) {


        return null;
    }

    public static Process fork(String command) {
        ProcessBuilder b = new ProcessBuilder(command);
        try {
            return b.start();
        } catch (IOException e) {
            System.out.println("too bad... fork crashed {" + e.getMessage() + "}");
        }
        return null;
    }

    public static Process forkOld(String commandid) {
        if (true) {
            throw new FuckThisShitException("nope, not this time");
        }
        String command = null;
        String host = null;
        if (commandid.contains(":")) {
            String[] temp = commandid.split(":");
            command = temp[1];
            host = temp[0];
        } else {
            command = commandid;
        }
        return new Process() {
            @Override
            public OutputStream getOutputStream() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public InputStream getInputStream() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public InputStream getErrorStream() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public int waitFor() throws InterruptedException {
                return 0;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public int exitValue() {
                return 0;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void destroy() {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        };
    }

    /**
     * Do not take me too seriously
     */
    public static class FuckThisShitException extends RuntimeException {
        public FuckThisShitException(String message) {
            super(message);
        }
    }


}
