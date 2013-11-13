package ueb04.a1;

/**
 * Created with IntelliJ IDEA.
 * User: Julian
 * Date: 13.11.13
 * Time: 12:59
 * To change this template use File | Settings | File Templates.
 */
public interface Process<M> {
    void start(Process<M>[] peers); // starts the thread, passing some peers
    void send(M message);           // sends a message, possibly blocking
}
