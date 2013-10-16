package ueb01;

/**
 * Created with IntelliJ IDEA.
 * User: Julian
 * Date: 16.10.13
 * Time: 13:12
 * To change this template use File | Settings | File Templates.
 */
public class Monitor {

    // Example for monitor
    // all object methods must be declared as synchronized

    static class M {
        public synchronized void f(){
            System.out.println("MonitorMethod");
        }

        public void f2(){
            synchronized (this){
                System.out.println("Monitor2");
            }
        }

        public void f3(){
            synchronized (this.getClass()){
                System.out.println("Monitor2");
            }
        }
    }

    public static void main(String[]args){
        M m = new M();
    }
}
