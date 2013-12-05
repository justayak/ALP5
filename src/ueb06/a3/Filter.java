package ueb06.a3;

/**
 * User: Julian
 * Date: 04.12.13
 * Time: 20:16
 */
public class Filter {

    public static void main(String[] args){
        if(args.length < 2) throw new RuntimeException("Wrong nbr of params!");
        boolean plus = args[0].charAt(0) == '+';
        boolean isServer = false;
        String language = args[1];
        int hostPort = -1;
        if (args.length >= 4){
            if (args[2].equals("-c")){
                hostPort = Integer.parseInt(args[3]);
            }
        }
        if (args[2].equals("-s") || (args.length >= 5 && args[4].equals("-s"))){
            isServer = true;
        }
        // Programm kann beginnen...

    }

}
