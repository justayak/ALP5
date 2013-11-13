package ueb04.a1;

import java.lang.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Julian
 * Date: 13.11.13
 * Time: 13:00
 * To change this template use File | Settings | File Templates.
 */
public abstract class GenericProcess<M> extends Thread implements Process<M> {

    protected final String name;
    protected final int boxsize;

    private List<Process<M>> peers = null;

    public void run(){
        System.out.println("thread!");
    }

    /**
     * @param name
     * @param boxsize
     */
    GenericProcess(String name, int boxsize){
        this.name = name;
        this.boxsize = boxsize;
    }

    @Override
    public void start(Process<M>[] peers) {
        this.peers = Arrays.asList(peers);
        this.start();
    }

    @Override
    public void send(M message) {
        if (this.peers != null){

        }
    }


    protected M recv(){

        return null;
    }

    // ===================================
    public static  void main(String[]args){
        System.out.println("hallo");
    }
}
