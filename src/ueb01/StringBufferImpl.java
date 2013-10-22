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

    @Override
    public void send(String x) {
        this.lock.lock();
        try{
            while (this.count == SIZE){
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
        }

    }

    @Override
    public String recv() {
        this.lock.lock();
        try{
            while(this.count==0){
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
        }
    }

    @Override
    public int length() {
        return count;
    }
}
