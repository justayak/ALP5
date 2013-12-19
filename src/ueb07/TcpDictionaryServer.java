package ueb07;

/**
 * Created by Julian on 19.12.13.
 */
public class TcpDictionaryServer extends Translator{
    public static void main(String[] args){
        Translator x = new TcpDictionaryServer();
    };

    public TcpDictionaryServer(){
        System.out.println(this.translateText("hello world tree message"));
    }

}
