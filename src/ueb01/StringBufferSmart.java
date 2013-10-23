package ueb01;

/**
 * Created with IntelliJ IDEA.
 * User: Julian
 * Date: 16.10.13
 * Time: 13:36
 * To change this template use File | Settings | File Templates.
 */
public class StringBufferSmart implements Buffer<String> {

    private static final int SIZE = 10;

    private String[] elements = new String[SIZE];
    private int in = 0;
    private int out = 0;
    private volatile int count = 0;

    private Object notFull = new Object();
    private Object notEmpty = new Object();

    /**
     *
     * @param x
     */
    @Override
    public void send(String x){
        //while(this.count == SIZE){ /* fuck busy waiting.. */ }
        if (this.count == SIZE){
            synchronized (notFull){
                try {
                    notFull.wait();
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
            }
        }
        this.elements[this.in] = x;
        this.in = (this.in+1)==SIZE ? 0 : this.in + 1;
        this.count += 1;
        synchronized (notEmpty){
            notEmpty.notify();
        }
    }

    /**
     *
     * @return last Element
     */
    @Override
    public String recv() {
        //while(this.count == 0){ /* fuck busy waiting.. */ }
        if (this.count == 0){
            synchronized (notEmpty)          {
                try {
                    notEmpty.wait();
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
            }
        }
        String result = this.elements[this.out];
        this.out = this.out + 1 == SIZE ? 0 : this.out + 1;
        this.count -= 1;
        synchronized (notFull){
            notFull.notify();
        }
        return result;
    }

    @Override
    public int length() {
        return this.count;
    }
}
