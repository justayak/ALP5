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
public abstract class GenericProcess_o<M> extends Thread implements Process<M> {

    protected final String name;
    private final int SIZE;
    private BlockingQueue<M> mailbox;
    private List<Process<M>> peers = null;
    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();
    private int count = 0;

    /**
     * @param name
     * @param boxsize
     */
    GenericProcess_o(String name, int boxsize){
        this.SIZE = boxsize;
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
        if (this.peers != null){
            this.lock.lock();
            try{
                while (this.count == this.SIZE){
                    this.notFull.await();
                }
                this.mailbox.put(message);
                this.count++;
                this.notEmpty.signal();
            }catch (InterruptedException e){
                e.printStackTrace();
                System.out.println("GenericProcess::send -> Crash shit fuck damn etc.");
            }finally {
                this.lock.unlock();
            }
        }
    }

    protected M recv(){
        this.lock.lock();
        try{
            while (this.count == 0){
                this.notEmpty.await();
            }
            M result = this.mailbox.poll();
            this.count--;
            notFull.signal();
            return result;
        }catch(InterruptedException e){
            e.printStackTrace();
            System.out.println("GenericProcess::recv -> fucked up again shit ");
            return null;
        }finally{
            this.lock.unlock();
        }
    }

    // ===================================
    public static  void main(String[]args){
        System.out.println("hallo");
    }
}
