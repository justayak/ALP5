package ueb04.a2;

/**
 * User: Julian
 * Date: 21.11.13
 * Time: 16:08
 */
public class Receiver {

    public static void main(String[]args){

        String name = args[0];
        int[] ports = new int[0];
        if (args.length > 1){
            ports = new int[1];
            ports[0] = Integer.parseInt(args[1]);
        }

        Comm2.init(name,ports);

        while (true){
            System.out.println(Comm2.RECV());
        }

    }

}
