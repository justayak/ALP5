package utils;

import org.omg.SendingContext.RunTime;
import sun.misc.IOUtils;      // stuff

import javax.sound.midi.SysexMessage;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.util.Arrays;
import java.util.Map;

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

        //ProcessBuilder b = new ProcessBuilder("D:/cmd/JulsTest.exe", "hello", "demo2");

        //Process p = b.start();

        //System.out.println(read(p));


        Process p = fork("D:/cmd/JulsTest.exe hello demo2");
        System.out.println(read(p));
    }

    /**
     *
     * @param commandId
     * @return
     */
    public static Process fork(String commandId) {
        String host = null;
        String command = commandId;
        if (commandId.contains(":")){
            String[] temp = commandId.split(":");
            if (temp[0].length() > 2){
                // if the host is shorter its probably just a windows drive ('d:// ...')
                host = temp[0];
                if (temp.length == 3){
                    command = temp[1] + ":" + temp[2]; // to "repair" windows drives...
                }else {
                    command = temp[1];
                }
            }
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
