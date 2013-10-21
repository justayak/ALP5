package ueb01;

/**
 * Created with IntelliJ IDEA.
 * User: Julian
 * Date: 21.10.13
 * Time: 17:03
 * To change this template use File | Settings | File Templates.
 */
public class Aufgabe2 {
    public static void main(String[]args) throws InterruptedException {
        System.out.println("-- Aufgabe 2 --");

        Buffer<String> buffer = new StringBufferSmart();
        Consumer c = new Consumer(buffer);
        Thread t1 = new Thread(new Producer(buffer));
        Thread t2 = new Thread(c);

        t1.start();
        t2.start();

        t1.join();

        Thread.sleep(1000);

        c.stopMe=true;
        t2.interrupt();

        System.out.println("-- end --");

    }


    private static class Producer implements Runnable{

        final Buffer<String> buffer;

        public Producer(Buffer<String> buffer){
            this.buffer = buffer;
        }

        @Override
        public void run() {
            for(int i=0;i<1001;i++){
                this.buffer.send("hallo " + i);
            }
        }
    }

    private static class Consumer implements Runnable{

        final Buffer<String> buffer;
        public boolean stopMe;

        public Consumer(Buffer<String> buffer){
            this.buffer = buffer;
        }

        @Override
        public void run() {
            while(!this.stopMe){
                System.out.println("> " + this.buffer.recv());
            }
        }
    }
}
