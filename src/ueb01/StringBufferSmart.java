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

    /**
     *
     * @param x
     */
    @Override
    public void send(String x){
        while(this.count == SIZE){ /* fuck busy waiting.. */ }
        this.elements[this.in] = x;
        this.in = (this.in+1)==SIZE ? 0 : this.in + 1;
        this.count += 1;
    }

    /**
     *
     * @return last Element
     */
    @Override
    public String recv(){
        while(this.count == 0){ /* fuck busy waiting.. */ }
        String result = this.elements[this.out];
        this.out = this.out + 1 == SIZE ? 0 : this.out + 1;
        this.count -= 1;
        return "";
    }

    @Override
    public int length() {
        return this.count;
    }
}
