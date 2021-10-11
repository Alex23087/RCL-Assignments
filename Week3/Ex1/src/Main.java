import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();
        Writer[] writers = new Writer[20];
        Reader[] readers = new Reader[20];
        for(int i = 0; i < 20; i++){
            writers[i] = new Writer(counter);
            readers[i] = new Reader(counter);
        }

        ExecutorService threadPool = Executors.newCachedThreadPool();
        for(Writer w : writers){
            threadPool.execute(w);
        }
        for(Reader r : readers){
            threadPool.execute(r);
        }
        threadPool.shutdown();
        threadPool.awaitTermination(1000, TimeUnit.MILLISECONDS);
        System.out.println(counter.get());
    }
}