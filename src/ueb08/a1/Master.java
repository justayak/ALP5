package ueb08.a1;

import utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Julian on 19.01.14.
 */
public class Master {

    public static void main(String[] args) throws IOException, InterruptedException {

        Master m = new Master();
        Getmax getmax = new Getmax(new int[] {1,2,3,4,5,6,7,8,9,10,11,12,13},0,12);

        Max max = new Max(Integer.MIN_VALUE);
        m.solve(getmax,max,4);

        System.out.println(max.max);

    }

    public void solve(Problem problem, Solution solution, int n) throws IOException, InterruptedException {
        final List<Problem> chunks = problem.divide(n);
        final Solution mainSolution = solution;
        final String workingDirectory = "h://data/studium/alp5/alp5/src";  // ist auf jedem rechner anders..
        //final String workingDirectory = "d://uni/alp5/src/";  // ist auf jedem rechner anders..
        final File dir = new File(workingDirectory);
        final BlockingQueue<Solution> solutions = new LinkedBlockingQueue<Solution>();
        List<Thread> threads = new ArrayList<Thread>();
        for(int i = 0; i < n; i++){
            final Problem prob = chunks.get(i);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Process pr = new ProcessBuilder("cmd", "/C", "java", "ueb08.a1.Worker", prob.toJSON()).redirectErrorStream(true).directory(dir).start();
                        String yy = prob.toJSON();
                        pr.waitFor();
                        String x = Utils.read(pr);
                        solutions.put(prob.createSolution(x));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
            threads.add(t);
        }

        for(Thread t : threads) t.join();

        while (!solutions.isEmpty()){
            mainSolution.extend(solutions.poll());
        }
    }
}
