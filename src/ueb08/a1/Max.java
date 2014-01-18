package ueb08.a1;

/**
 * Created by Julian on 18.01.14.
 */
public class Max implements Solution<Max> {

    private int max;

    public Max(int m) {
        this.max = m;
    }

    @Override
    public void extend(Max s) {
        if (s.max > this.max) this.max = s.max;
    }
}
