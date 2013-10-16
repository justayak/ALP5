package ueb01.aufgabe1b;

/**
 * Created with IntelliJ IDEA.
 * User: Julian
 * Date: 16.10.13
 * Time: 13:30
 * To change this template use File | Settings | File Templates.
 */
public interface Buffer<E> {
    void send(E x);
    E recv();
    int length();
}