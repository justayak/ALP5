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
    private Object lock;

    private void sleep(){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public void send(String x) {
        while (this.count == SIZE){
            this.sleep();
        }
        this.count++;
        this.elements[this.in] = x;
        this.in = (this.in+1)==SIZE ? 0 : this.in + 1;
    }

    @Override
    public String recv() {
        while(this.count==0){
            this.sleep();
        }
        this.count--;
        String result = this.elements[out];
        this.out = this.out + 1 == SIZE ? 0:this.out+1;
        return result;
    }

    @Override
    public int length() {
        return this.count;
    }
}
