package ueb08.a1;

import java.util.Objects;

/**
 * Created by Julian on 19.01.14.
 */
public class Worker {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String data = args[0];
        String className = data.split("#")[3];
        Problem o = (Problem) Class.forName(className).newInstance();
        o.fromJSON(data);
        System.out.println(o.solve().toJSON());
    }

}
