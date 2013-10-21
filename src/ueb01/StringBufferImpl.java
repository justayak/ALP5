package ueb01;

import ueb01.Buffer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/locks/Condition.html
 * Created with IntelliJ IDEA.
 * User: Julian
 * Date: 16.10.13
 * Time: 13:32
 * To change this template use File | Settings | File Templates.
 */
public class StringBufferImpl implements Buffer<String> {

    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    private static final int SIZE = 10;

    private String[] elements = new String[SIZE];
    private int in = 0;
    private int out = 0;
    private int count = 0;

    private static final boolean LOG = false;

    private void log(String message){
        if(LOG){
            System.out.println(message);
        }
    }

    @Override
    public void send(String x) {
        this.log("send - before {" + x + "}");
        this.lock.lock();
        this.log("send - inside");
        try{
            while (this.count == SIZE){
                this.log("send - await notFull: " + this.count );
                this.notFull.await();
            }
            this.elements[in] = x;
            this.in = (this.in+1) == SIZE ? 0 : this.in + 1;
            this.count++;
            this.notEmpty.signal();
        }catch (InterruptedException e){
            System.out.println("nope :( - " + e.getMessage());
        } finally {
            this.lock.unlock();
            this.log("send - leave: " + this.count);
        }

    }

    @Override
    public String recv() {
        this.log("recv - before" );
        this.lock.lock();
        this.log("recv - inside" );
        try{
            while(this.count==0){
                this.log("recv- await notEmpty: " + this.count );
                this.notEmpty.await();
            }
            String result = this.elements[out];
            this.out = this.out + 1 == SIZE ? 0 : this.out + 1;
            count--;
            notFull.signal();
            return result;
        } catch (InterruptedException e){
            System.out.println("nope again :( - " + e.getMessage());
            return null;
        } finally {
            this.lock.unlock();
            this.log("recv - leave" );
        }
    }

    @Override
    public int length() {
        return count;
    }
}
