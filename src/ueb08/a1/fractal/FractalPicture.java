package ueb08.a1.fractal;

import ueb08.a1.Solution;

import java.util.Random;

/**
 * User: Julian
 * Date: 19.01.14
 * Time: 21:20
 */
public class FractalPicture implements Solution<FractalPicture> {

    public final int [][] rgbdata;
    final boolean[] lineFilled;
    final int firstLine;
    final int xsize;
    final int ysize;

    public FractalPicture(int xsize,int ysize){
        this.xsize = xsize;
        this.ysize = ysize;
        this.rgbdata = new int[xsize][ysize];
        this.firstLine = 0;
        this.lineFilled = new boolean[ysize];
    }

    public FractalPicture(int xsize,int ysize, int[][] rgbdata, int firstLine){
        this.firstLine = firstLine;
        this.xsize = xsize;
        this.ysize = ysize;
        this.lineFilled = new boolean[ysize];
        this.rgbdata = new int[xsize][ysize];
        for(int i = 0; i < rgbdata.length;i++){
            System.arraycopy(rgbdata[i],0,this.rgbdata[firstLine+i],0,xsize);
            this.lineFilled[firstLine+i] = true;
        }
    }

    @Override
    public void extend(FractalPicture s) {
        for(int i = 0; i < this.ysize; i++){
            if(s.lineFilled[i] || !this.lineFilled[i]){
                System.arraycopy(s.rgbdata[i],0,this.rgbdata[i],0,this.xsize);
                this.lineFilled[i] =true;
            }
        }
    }

    @Override
    public String toJSON() {
        return null;
    }

    @Override
    public void fromJSON(String json) {

    }
}
