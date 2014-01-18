package ueb08.a1;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Julian on 18.01.14.
 */
public class MasterWorker {
    final Pool<Problem> jobPool = new PoolImpl<Problem>();
    final Pool<Solution> resPool = new PoolImpl<Solution>();

    public void sequentialSolve(Problem p, Solution s, int n){
        // just for fun, ignore! (wtf!)
        List<Problem> chunks = p.divide(n);
        for(Problem chunk : chunks) s.extend(chunk.solve());
    }

    public void solve(Problem problem, Solution solution, int n){
        List<Problem> chunks = problem.divide(n);
        BlockingQueue<Problem> jobs = new LinkedBlockingQueue<Problem>();
        // der selbstgebastelte Pool ist nutzlos...
        for(Problem p : chunks) jobs.add(p);

        int tCount = Math.max(n / 2, 1);
        for(int i = 0; i < tCount; i++){

        }

    }

}
