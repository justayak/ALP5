package ueb08.a1.fractal;

import painting.Complex;
import painting.Painter;
import ueb08.a1.Problem;
import ueb08.a1.Solution;
import ueb08.a1.raytracing.Picture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: Julian
 * Date: 19.01.14
 * Time: 21:20
 */
public class FractalScene implements Problem<FractalScene, FractalPicture> {

    private static final int MAX_ITERATIONS = 20;
    private static final double EP = Math.pow(10, -2);

    private final Complex[] zeros;

    final int n = 5;

    final int firstLine;
    final int lines;
    final int xsize;
    final int ysize;
    final double maxImg;
    final double maxReal;

    static final int COLORS[][] = new int[][] {
            { 255, 0, 0 },
            new int[] { 0, 255, 0 }, new int[] { 0, 0, 255 },
            new int[] { 255, 0, 255 }, new int[] { 0, 255, 255 },
            new int[] { 255, 255, 0 }, new int[] { 255, 128, 0 },
            new int[] { 0, 255, 128 }, new int[] { 255, 0, 128 }
    };

    public FractalScene(double maxImg, double maxReal, int xsize, int ysize, int firstLine, int lines) {
        this.firstLine = firstLine;
        this.maxImg = maxImg;
        this.maxReal = maxReal;
        this.xsize = xsize;
        this.ysize = ysize;
        this.lines = lines;

        this.zeros = new Complex[this.n];
        Complex z = new Complex(1,0);
        double r = Math.pow(z.mod(),1.0/this.n);
        double theta;
        for (int i = 0; i < this.n; i++){
            theta = z.arg() + 2 * i * Math.PI;
            this.zeros[i] = new Complex(r * Math.cos(theta / this.n),r * Math.sin(theta / this.n));
        }
    }

    @Override
    public List<FractalScene> divide(int n) {
        List<FractalScene> result = new ArrayList<FractalScene>();
        int linesPerChunk = (int) Math.ceil(this.lines / (double) n);
        for (int i = 0; i < n; i++) {
            int lines = (i * linesPerChunk + linesPerChunk) >= this.lines ?
                    (this.lines - (i * linesPerChunk)) - 1 : linesPerChunk;
            result.add(new FractalScene(
                    this.maxImg,
                    this.maxReal,
                    this.xsize,
                    this.ysize,
                    i * linesPerChunk,
                    lines
            ));
        }
        return result;
    }

    @Override
    public Solution<FractalPicture> solve() {
        int[][] rgbdata = new int[this.lines][this.xsize];
        for(int y = this.firstLine; y < this.firstLine + this.lines;y++){
            for(int x = 0; x < this.xsize; x++){
                double u = this.maxReal * (x - this.xsize / 2) / (this.xsize / 2);
                double v = this.maxImg * (this.ysize / 2 - y) / (this.ysize / 2) ;
                Complex zero = new Complex(u,v);
                Complex znew = zero;
                int i = 0;
                for(; i < MAX_ITERATIONS; i++){
                    zero = znew;
                    znew = zero.minus(this.cyclotomicEquation(zero).div(
                            this.derivateCyclotomicEquation(zero)
                    ));
                    if(znew.minus(zero).div(zero).mod()*100 <= EP) break;
                }
                int[] data = new int[] {smallestDistance(znew),i};
                int[] color = Arrays.copyOf(COLORS[data[0]%COLORS.length],3);
                double br = (double) (data[1]-1) / i;
                if(br > 0.95) for(int j = 0;j<3;j++) color[j] = 255;
                else for (int j = 0; j < 3; j++) color[j] = (int) Math.round(color[j]*br / 0.8);
                rgbdata[y-firstLine][x] = Painter.calculateColor(color[0],color[1],color[2]);
            }
        }
        return new FractalPicture(xsize,ysize,rgbdata,firstLine);
    }

    private int smallestDistance(Complex z){
        int zero = 0;
        double m = z.minus(this.zeros[0]).mod();
        double d;
        for (int i = 1; i < this.zeros.length;i++){
            d = z.minus(this.zeros[i]).mod();
            if(d<m){
                zero = i;
                m = d;
            }
        }
        return zero;
    }

    private static Complex pow(Complex z, int n){
        if(n == 0) return new Complex(1,0);
        else if (n<0) return new Complex(1,0).div(pow(z,-n));
        Complex temp = z;
        for (int i = 1; i < n; i++) temp = temp.times(z);
        return temp;
    }

    private Complex cyclotomicEquation(Complex z){
        return pow(z,n-1).minus(new Complex(1,0));
    }

    private Complex derivateCyclotomicEquation(Complex z){
        return pow(z,n-1).times(new Complex(n,0));
    }

    @Override
    public Solution<FractalPicture> createSolution(String json) {
        return null;
    }

    @Override
    public String toJSON() {
        return null;
    }

    @Override
    public void fromJSON(String json) {

    }
}
