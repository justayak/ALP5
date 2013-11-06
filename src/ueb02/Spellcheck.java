package ueb02;

import utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Julian
 * Date: 28.10.13
 * Time: 22:29
 */
public class Spellcheck {

    public static void main(String[]args) throws InterruptedException {

        /*

        Das Programm startet eine Reihe von Threads, die jeweils 20000 Zeilen der "text.txt" auslesen und
        in ihre jeweilige eigene Textdatei schreiben (jaa, die Standardausgabe auf meinem Windows hat nicht
        auf meine Ausgabe gehört, darum der Umweg über die Datei..)
        Am Ende wird ein "Killer"-Thread gestartet, der 10 Sekunden wartet und dann ggf. alle anderen Threads
        killt (da kein Aufruf länger als 10 Sekunden dauern sollte)

         */

        final int CHARACTERS_PER_PROGRAM = 20000;

        int charactersInFiles = Utils.countCharactersInFile("text.txt");

        List<Thread> threads = new ArrayList<Thread>();

        for(int i = 0; i < charactersInFiles/CHARACTERS_PER_PROGRAM;i++){
            // die Ausgabe wird umgeleitet in eine Datei
            Thread t = new Thread(new Checker(i, CHARACTERS_PER_PROGRAM));
            t.start();
            threads.add(t);
        }

        Thread killer = new Thread(new Killer(threads));
        killer.start();

        for(Thread t : threads){
            t.join();
        }

        killer.join();
        System.out.println("stop..");

    }


    static class Killer implements Runnable{

        final List<Thread> threads;

        public Killer(List<Thread> threads){
            this.threads = threads;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(10000); // wait for 10 seconds..

                // kill all threads that are still alive
                for(Thread t:this.threads){
                    if (t.isAlive()){
                        t.interrupt();
                    }
                }

            } catch (InterruptedException e) {
                System.out.println("autsch..");
            }

        }
    }

    static class Checker implements Runnable{

        final int i;
        final int charPerProgram;
        public Checker(int i, int charPerProgram){
            this.i = i;
            this.charPerProgram = charPerProgram;
        }

        @Override
        public void run() {
            Utils.fork("Check.exe text.txt " + (this.i * this.charPerProgram) +
                    " " + ((this.i+1) * this.charPerProgram) + " check" + this.i +".txt");
        }
    }

}
