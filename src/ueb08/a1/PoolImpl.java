package ueb08.a1;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Julian on 18.01.14.
 */
public class PoolImpl<T> implements Pool<T> {

    private final BlockingQueue<T> q = new LinkedBlockingQueue<T>();

    @Override
    public void put(T t) {
        this.q.add(t);
    }

    @Override
    public T get() {
        try {
            return this.q.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("interr...");
            return null;
        }
    }
}
