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

    /**
     * Encodes the result:
     * <> is token for "null"
     * trans1|trans2|trans3 is a list seperated by the "|" symbol
     * @param trans
     * @return
     */
    protected String encode(String[] trans){
        if (trans == null){
            return "<>";
        }
        StringBuilder sb = new StringBuilder();
        for(String word : trans){
            if (sb.length() > 0) sb.append("|");
            sb.append(word);
        }
        return sb.toString();
    }

    protected String translateEncoded(String word){
        return this.encode(this.translate(word));
    }

    protected String[] decode(String encoded){
        if (encoded.equals("<>")) return null;
        return encoded.split("\\|");
    }

    protected String[] translate(String word){
        if (dic.containsKey(word)) return dic.get(word);
        return null;
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
