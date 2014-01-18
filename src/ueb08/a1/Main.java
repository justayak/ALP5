package ueb08.a1;

import java.util.List;

/**
 * Created by Julian on 18.01.14.
 */
public class Main {

    public static void main(String[]args){

        Getmax m = new Getmax(new int[]{1,5,2,1,44,12,12,1,9,10,43,-12,0},0,8);

        List<Getmax> l = m.divide(3);

        for(Getmax ma : l){
            for(int i = ma.first; i <= ma.next; i++){
                System.out.print(ma.array[i]);
                System.out.print("|");
            }
            System.out.println("---");
        }

        System.out.println("hier..");
    }

}
