package ueb08.a1;

/**
 * Created by Julian on 18.01.14.
 */
public interface Pool<T> {
    void put(T t);
    T get();
}
