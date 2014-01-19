package ueb08.a1;

import utils.Json;

/**
 * Created by Julian on 18.01.14.
 */
public interface Solution<T> extends Json {
    void extend(T s);
}
