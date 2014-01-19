package ueb08.a1;

import utils.Json;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julian on 18.01.14.
 */
public class Getmax implements Problem<Getmax,Max>, Json {

    int[] array; int first; int next;

    public Getmax(String json){
        this.fromJSON(json);
    }

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

    @Override
    /**
     * kein json aber who cares
     */
    public String toJSON() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(int i : this.array){
            if(sb.length()>1) sb.append(",");
            sb.append(i);
        }
        sb.append("#");
        sb.append(this.first);
        sb.append("#");
        sb.append(this.next);
        return sb.toString();
    }

    @Override
    /**
     * kein json aber who cares
     */
    public void fromJSON(String json) {
        String[] values = json.split("#");
        List<Integer> arr = new ArrayList<Integer>();
        String array = values[0].substring(1,values[0].length());
        for(String integer : array.split(",")){
            arr.add(Integer.parseInt(integer));
        }
        this.array = new int[arr.size()];
        for(int i = 0; i < this.array.length;i++){
            this.array[i] = arr.get(i);
        }
        this.first = Integer.parseInt(values[1]);
        this.next = Integer.parseInt(values[2]);
        System.out.println(array);
    }

}
