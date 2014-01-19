package ueb08.a1;

import utils.Json;

import java.util.List;

/**
 * Created by Julian on 18.01.14.
 */
public interface Problem<P,S> extends Json {
    List<P> divide(int n);
    Solution<S> solve();
    Solution<S> createSolution(String json);
}
