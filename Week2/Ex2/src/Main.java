import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
        ExecutorService pool = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MICROSECONDS, new ArrayBlockingQueue<>(10));

        for(int i = 0; i < 50; i++){
            try {
                pool.submit(new Traveller(i));
                Thread.sleep(50);
            }catch(RejectedExecutionException ree){
                System.out.println("Traveler no. " + i + ": sala esaurita");
            }catch(InterruptedException ie){
                System.out.println("Interrupted");
            }
        }
        pool.shutdown();
    }
}