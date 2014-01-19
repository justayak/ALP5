package ueb08.a1.raytracing;

import ueb08.a1.Solution;

/**
 * Created by Julian on 18.01.14.
 */
public class Picture implements Solution<Picture> {

    final int[][] rgbdata;
    final int firstLine;
    final int lines;

    public Picture(int xsize, int ysize){
        this.rgbdata = new int[xsize][ysize];
        this.firstLine = 0;
        this.lines = xsize;
    }

    public Picture(int[][]rgbdata,int firstline, int lines){
        this.rgbdata = rgbdata;
        this.firstLine = firstline;
        this.lines = lines;
    }

    @Override
    public void extend(Picture s) {
        for(int i = 0; i < s.lines; i++){
            this.rgbdata[i+s.firstLine] = s.rgbdata[i];
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
