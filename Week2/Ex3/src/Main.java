import java.util.ArrayList;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
        if(args.length < 1){
            System.err.println("Invalid number of arguments passed");
            return;
        }
        double n;
        try {
            n = Double.parseDouble(args[0]);
        }catch(NumberFormatException nfe){
            nfe.printStackTrace();
            return;
        }

        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        ArrayList<Future<Double>> results = new ArrayList<>(49);

        for(int i = 2; i <= 50; i++){
            results.add(pool.submit(new Power(n, i)));
        }
        pool.shutdown();

        double result = 0;
        for(Future<Double> f : results){
            try {
                result += f.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Risultato: " + result);
    }
}