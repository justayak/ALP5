package ueb08.a1.raytracing;

import ue02.ray.ansicht.*;
import painting.Painter;
import ueb08.a1.MasterWorker;

/**
 * Sei n der Wert, der zeigt, wie oft der Prozess aufgesplittet werden soll:
 * (Der PC, auf dem das Programm ausgeführt wurde, hat 4 (theoretische) Kerne:
 *
 *
 * normal: 14859 | multi: 4205  (n = 16 ==> 8 Threads)
 * normal: 11236 | multi: 4482  (n = 8 ==> 4 Threads)
 * normal: 16299 | multi: 7233  (n = 4 ==> 2 Threads)
 * normal: 20289 | multi: 5457  (n = 100 ==> 50 Threads)
 *
 * Was kann man daraus erkennen?:
 * Die maximale Geschwindigkeit zur Problemlösung ist an die Anzahl der Kerne
 * gebunden (wow, was ne Erkenntnis..). Sind 2 Kerne in Benutzung (n = 4), dann
 * wird der Vorgang ungefähr doppelt so schnell, sind alle 4 Kerne in Benutzung
 * (n = 8, n = 16, n = 100, n > 7), dann verbessert sich der Wert fast um das
 * vierfache (~1/4 der ursprünglichen Zeit).
 * Was man noch bemerken sollte ist, dass, je mehr Threads benutzt werden, umso
 * mehr Overhead wird erzeugt und umso mehr Zeit wird wieder benötigt (Siehe n=100 -->
 * hierbei werden 50 Threads gespawnt! - Die Gesamtdauer hier ist aber länger!)
 *
 * Created by Julian on 18.01.14.
 */
public class Main {

    public static void main(String[] args){

        Modell modell = Szenen.erzeugeModell(1);
        int xsize = 800;
        int ysize = 800;
        modell.setzeBildGroesse(xsize,ysize);
        modell.setzeRekTiefe(2);
        int firstline = 0;
        int nooflines = 800;

        long start = System.nanoTime();
        int [][] rgbdata = modell.zeilenBerechnen(firstline,nooflines);
        long elapsedTime = System.nanoTime() - start;


        //Painter painter = new Painter("Ray",xsize,ysize);
        /*
        for(int i = 0; i < nooflines; i++){
            painter.update(0,firstline+i, xsize, 1, rgbdata[i], 0,0);
        } */

        Picture p = new Picture(800,800);

        Scene scene = new Scene(800,800,2,0,xsize);

        MasterWorker m = new MasterWorker();

        long startMulti = System.nanoTime();
        m.solve(scene,p,100);
        long elapsedTimeMulti = System.nanoTime() - startMulti;

        /*for(int i = 0; i < nooflines; i++){
            painter.update(0,firstline+i, xsize, 1, p.rgbdata[i], 0,0);
        } */

        System.out.println("normal: " + (elapsedTime /1000000)+ " | multi: " + (elapsedTimeMulti/1000000));

    }

}
