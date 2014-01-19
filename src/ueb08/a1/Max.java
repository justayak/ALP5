package ueb08.a1;

/**
 * Created by Julian on 18.01.14.
 */
public class Max implements Solution<Max> {

    public int max;

    public Max(int m) {
        this.max = m;
    }

    public Max(String json){
        this.fromJSON(json);
    }

    @Override
    public void extend(Max s) {
        if (s.max > this.max) this.max = s.max;
    }

    @Override
    public String toJSON() {
        return "" + this.max;
    }

    @Override
    public void fromJSON(String json) {
        this.max = Integer.parseInt(json);
    }
}
