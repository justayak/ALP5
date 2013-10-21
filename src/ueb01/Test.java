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

        Thread t1 = new Thread(new Creator(buffer, "creator #1"));
        Thread t2 = new Thread(new Creator(buffer, "creator #2"));

        Consumer c1 = new Consumer(buffer, "consumer #1");
        Consumer c2 = new Consumer(buffer, "consumer #2");
        Thread t3 = new Thread(c1);
        Thread t4 = new Thread(c2);

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        t1.join();
        t2.join();
        c1.stopMe = true;
        c2.stopMe = true;
        t3.interrupt();     // has to be done because we "wait" on the monitor and if we dont
        t4.interrupt();     // interrupt the threads they will sleep there forever
        System.out.println("-- end --");
    }

    private static class Creator implements Runnable{

        private final Buffer<String> buffer;
        private final String name;

        public Creator(Buffer<String> buffer, String name){
            this.buffer = buffer;
            this.name = name;
        }

        @Override
        public void run() {

            for(int i = 0; i < 30; i++){
                this.buffer.send("text " + i + " from " + this.name);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
    }

    private static class Consumer implements Runnable{

        private final Buffer<String> buffer;
        private final String name;
        public boolean stopMe = false;

        public Consumer(Buffer<String> buffer, String name){
            this.buffer = buffer;
            this.name = name;
        }

        @Override
        public void run() {
            while(!this.stopMe){
                String m = this.buffer.recv();
                System.out.println(m + " out from " + this.name + " | size: " + this.buffer.length() );
            }
            System.out.println("close " + this.name);
        }
    }
}
