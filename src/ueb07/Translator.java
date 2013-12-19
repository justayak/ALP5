package ueb07;

import java.util.HashMap;

/**
 * Created by Julian on 19.12.13.
 */
public abstract class Translator {

    /**
     * Den krams kann man natürlich auch aus ner Datei laden aber das sparen  wir uns mal...
     */
    private static HashMap<String, String[]> dic = new HashMap<String, String[]>()
    {
        {
            put("failure",new String[] {"Fehlschlag", "Misserfolg", "Versager" });
            put("message",new String[] {"Botschaft", "Nachricht"});
            put("tree",new String[] {"Baum"});
        };
    };

    protected String[] translate(String word){
        if (dic.containsKey(word)) return dic.get(word);
        return new String[] {word};
    }

    protected String translateText(String english){
        StringBuilder sb = new StringBuilder();
        for (String word : english.split(" ")){
            if (sb.length() > 0) sb.append(" ");
            if (dic.containsKey(word))sb.append(dic.get(word)[0]);
            else sb.append(word);
        }
        return sb.toString();
    }

}
