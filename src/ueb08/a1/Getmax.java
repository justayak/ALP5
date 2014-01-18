package ueb08.a1;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julian on 18.01.14.
 */
public class Getmax implements Problem<Getmax,Max> {

    final int[] array; final int first; final int next;

    public Getmax(int[] array, int first, int next){
        this.array = array; this.first = first; this.next = next;
    }

    @Override
    public List<Getmax> divide(int n) {
        int l = (int)Math.ceil(this.array.length / (double)n);
        List<Getmax> result = new ArrayList<Getmax>();
        for(int i = 0; i < n; i++){
            result.add(new Getmax(this.array, i * l, Math.min(i * l + l - 1,array.length-1)));
        }
        return result;
    }

    @Override
    public Solution<Max> solve() {
        int max = Integer.MIN_VALUE;
        for(int i = this.first; i <= this.next; i++){
            if(this.array[i] > max) max = this.array[i];
        }
        return new Max(max);
    }
}
