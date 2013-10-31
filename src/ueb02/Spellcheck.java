package ueb02;

import utils.Utils;

/**
 * User: Julian
 * Date: 28.10.13
 * Time: 22:29
 */
public class Spellcheck {

    public static void main(String[]args){

        // die Ausgabe ist umgeleitet in eine Datei
        Process p = Utils.fork("Check.exe text.txt 0 2000 check.txt");



    }

}
