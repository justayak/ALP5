package ueb08.a1.raytracing;

import ue02.ray.ansicht.Modell;
import ue02.ray.ansicht.Szenen;
import ueb08.a1.Problem;
import ueb08.a1.Solution;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julian on 18.01.14.
 */
public class Scene implements Problem<Scene, Picture> {

    final Modell model;
    final int firstLine;
    final int lines;
    private int xsize,ysize,rec;

    private Scene(Modell m, int f, int n) {
        this.model = m;
        this.firstLine = f;
        this.lines = n;
    }

    public Scene(int xsize, int ysize, int rec, int f, int n){
        this.xsize = xsize;
        this.ysize = ysize;
        this.rec = rec;
        this.model = Szenen.erzeugeModell(1);
        this.model.setzeBildGroesse(xsize,ysize);
        this.model.setzeRekTiefe(rec);
        this.firstLine = f;
        this.lines = n;
    }

    @Override
    public List<Scene> divide(int n) {
        List<Scene> result = new ArrayList<Scene>();
        int linesPerChunk = (int)Math.ceil(lines / (double)n);
        for(int i = 0; i < n; i++){
            int lines = (i*linesPerChunk + linesPerChunk) >= this.lines ?
                    (this.lines - (i*linesPerChunk)) - 1:linesPerChunk;
            result.add(new Scene(
                    this.xsize,
                    this.ysize,
                    this.rec,
                    i*linesPerChunk,
                    lines));
        }
        return result;
    }

    @Override
    public Solution<Picture> solve() {
        try{
            return new Picture(this.model.zeilenBerechnen(firstLine,lines),firstLine,lines);
        }catch (Exception e){
            return new Picture(this.model.zeilenBerechnen(firstLine,lines),firstLine,lines);
        }

    }
}
