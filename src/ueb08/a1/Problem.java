package ueb08.a1;

import java.util.List;

/**
 * Created by Julian on 18.01.14.
 */
public interface Problem<P,S> {
    List<P> divide(int n);
    Solution<S> solve();
}