package ueb04.a1;

/**
 * User: Julian
 * Date: 13.11.13
 * Time: 22:57
 */
public class MyProcess extends GenericProcess<String> {
    /**
     * @param name
     * @param boxsize
     */
    MyProcess(String name, int boxsize) {
        super(name, boxsize);
    }

     public void run(){
         for (int i = 0; i < 5; i++){
             System.out.println(this.recv());
         }
     }

    /**
     * START
     * @param args
     */
    public static void main(String[]args){

        MyProcess p = new MyProcess("test0", 10);
        MyProcess[] l = new MyProcess[]{new MyProcess("test1", 10),new MyProcess("test2", 10)};
        p.start(l);

        p.send("hallo welt");

        System.out.println("hi");

    }
}
