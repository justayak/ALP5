package ueb01;

/**
 * Created with IntelliJ IDEA.
 * User: Julian
 * Date: 16.10.13
 * Time: 13:30
 */
public interface Buffer<E> {
    void send(E x);
    E recv();
    int length();
}