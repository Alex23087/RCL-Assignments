import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if(args.length < 1){
            throw new IllegalArgumentException("No path passed to the program");
        }
        String path = args[0];
        List<String> queue = new LinkedList<>(); //Queue implemented as LinkedList, will be used with synchronized blocks

        Thread crawler = new Thread(new Producer(path, queue));
        crawler.start();

        int nThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService pool = Executors.newFixedThreadPool(nThreads);
        for(int i = 0; i < nThreads; i++){
            pool.execute(new Consumer(queue));
        }
        pool.shutdown();
        try {
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        }catch (InterruptedException ignored){}
    }
}