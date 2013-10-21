package ueb01;

import java.io.IOException;

/**
 * http://w3.inf.fu-berlin.de/lehre/WS13/alp5/uebungen/blatt1/index.html
 * Created with IntelliJ IDEA.
 * User: Julian
 * Date: 16.10.13
 * Time: 13:35
 * To change this template use File | Settings | File Templates.
 */
public class Test {

    public static void main(String[]args) throws IOException, InterruptedException {

        StringBufferImpl buffer = new StringBufferImpl();

        buffer.send("Hallo");

        System.out.println(buffer.recv());

        buffer.send("1");
        buffer.send("2");
        buffer.send("3");

        System.out.println(buffer.recv());
        System.out.println(buffer.recv());
        System.out.println(buffer.recv());
    }

}
