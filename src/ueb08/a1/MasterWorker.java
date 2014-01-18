package ueb08.a1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Julian on 18.01.14.
 */
public class MasterWorker {
    final Pool<Problem> jobPool = new PoolImpl<Problem>();
    final Pool<Solution> resPool = new PoolImpl<Solution>();

    public void sequentialSolve(Problem p, Solution s, int n) {
        // just for fun, ignore! (wtf!)
        List<Problem> chunks = p.divide(n);
        for (Problem chunk : chunks) s.extend(chunk.solve());
    }

    public void solve(Problem problem, Solution solution, int n) {
        List<Problem> chunks = problem.divide(n);
        final Solution mainSolution = solution;
        final BlockingQueue<Problem> jobs = new LinkedBlockingQueue<Problem>();
        final BlockingQueue<Solution> solutions = new LinkedBlockingQueue<Solution>();
        // der selbstgebastelte Pool ist nutzlos...
        for (Problem p : chunks) jobs.add(p);

        int tCount = Math.max(n / 2, 1);
        List<Thread> ts = new ArrayList<Thread>();
        for (int i = 0; i < tCount; i++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    Problem p;
                    while((p = jobs.poll()) != null){
                        try {
                            Solution so = p.solve();
                            solutions.put(so);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            t.start();
            ts.add(t);
        }

        MergeRun merge = new MergeRun(solutions, mainSolution);
        Thread mergeT = new Thread(merge);
        mergeT.start();
        for (Thread t : ts) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        merge.isStop = true;
        try {
            mergeT.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (!solutions.isEmpty()){
            mainSolution.extend(solutions.poll());
        }
    }


    private class MergeRun implements Runnable{

        public boolean isStop = false;
        private BlockingQueue<Solution> solutions;
        private Solution main;

        public MergeRun(BlockingQueue<Solution> solutions, Solution main){
            this.solutions = solutions;
            this.main = main;
        }

        @Override
        public void run() {
            while (!isStop){
                try {
                    Solution s = this.solutions.poll(1000, TimeUnit.MILLISECONDS);
                    if(s != null){
                        this.main.extend(s);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
