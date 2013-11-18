package ueb04.a1;

import java.lang.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created with IntelliJ IDEA.
 * User: Julian
 * Date: 13.11.13
 * Time: 13:00
 */
public abstract class GenericProcess<M> extends Thread implements Process<M> {

    protected final String name;
    private BlockingQueue<M> mailbox;
    private List<Process<M>> peers = null;

    /**
     * @param name
     * @param boxsize
     */
    GenericProcess(String name, int boxsize){
        this.name = name;
        this.mailbox = new ArrayBlockingQueue<M>(boxsize);
    }

    @Override
    public void start(Process<M>[] peers) {
        this.peers = Arrays.asList(peers);
        this.start();
    }

    @Override
    public void send(M message) {
        try {
            this.mailbox.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("Generic-Process -> send *= fuck shit happend!");
        }
    }

    protected M recv(){
        try {
            return this.mailbox.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("Generic-Process -> recv *= fuck happend!");
            return null;
        }
    }

    // ===================================
    public static  void main(String[]args){
        System.out.println("hallo");
    }
}
