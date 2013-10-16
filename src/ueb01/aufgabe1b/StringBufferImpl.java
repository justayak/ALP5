package ueb01.aufgabe1b;

import java.util.concurrent.locks.Condition;

/**
 * http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/locks/Condition.html
 * Created with IntelliJ IDEA.
 * User: Julian
 * Date: 16.10.13
 * Time: 13:32
 * To change this template use File | Settings | File Templates.
 */
public class StringBufferImpl implements Buffer<String> {
    @Override
    public void send(String x) {

        //Condition

        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String recv() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int length() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
