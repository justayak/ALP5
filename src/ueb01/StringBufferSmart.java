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
    private Object lock = new Object();

    private void sleep(){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("happens... :(");
        }
    }

    @Override
    public void send(String x) {
        while (this.count == SIZE){
            //this.sleep();
        }
        synchronized (this.lock){
            if(this.count == SIZE){
                this.send(x);
            }else{
                this.count++;
                this.elements[this.in] = x;
                this.in = (this.in+1)==SIZE ? 0 : this.in + 1;
            }
        }
    }

    @Override
    public String recv() {
        while(this.count==0){
            //this.sleep();
        }
        synchronized (this.lock){
            if (this.count == 0){
                return recv();
            }else{
                this.count--;
                String result = this.elements[out];
                this.out = this.out + 1 == SIZE ? 0:this.out+1;
                return result;
            }
        }
    }

    @Override
    public int length() {
        return this.count;
    }
}
