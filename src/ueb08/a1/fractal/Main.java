package ueb08.a1.fractal;

import painting.Painter;
import ueb08.a1.MasterWorker;

/**
 * User: Julian
 * Date: 05.02.14
 * Time: 18:42
 */
public class Main {

    public static void main(String[] args){

        MasterWorker m = new MasterWorker();

        int xsize = 600;
        int ysize = 600;

        FractalPicture p = new FractalPicture(xsize,ysize);
        FractalScene s = new FractalScene(1.5,1.5,xsize,ysize,0,ysize);

        //m.solve(s,p,4);



        p.extend((FractalPicture) s.solve());

        Painter painter = new Painter("Frac",xsize,ysize);

        for(int i = 0; i < xsize; i++){
            painter.update(0,i,(xsize),1,p.rgbdata[i],0,0);
        }

    }

}
