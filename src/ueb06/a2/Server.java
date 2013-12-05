package ueb06.a2;

import java.util.concurrent.Future;

/**
 * User: Julian
 * Date: 04.12.13
 * Time: 20:02
 */
public interface Server {

    /**
     * Starts the Server and connects with the given port
     * @param port
     */
    void fork(int port);

    /**
     * Runs async function on server.
     */
    Future<String> op();

}
