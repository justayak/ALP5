package ueb04.a1;

import java.lang.*;

/**
 * Created with IntelliJ IDEA.
 * User: Julian
 * Date: 13.11.13
 * Time: 13:00
 * To change this template use File | Settings | File Templates.
 */
public class GenericProcess<M> implements Process<M> {
    @Override
    public void start(Process<M>[] peers) {

    }

    @Override
    public void send(M message) {

    }

    // ===================================
    public static  void main(String[]args){
        System.out.println("hallo");
    }
}
