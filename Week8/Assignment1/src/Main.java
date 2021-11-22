import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;

public class Main {
    public static String[] names = {
            "John",
            "Ada",
            "Isabelle",
            "Alan",
            "Edsger",
            "Alice",
            "Alonzo",
            "Kurt"
    };

    public static String[] surnames = {
            "Doe",
            "Lovelace",
            "Nook",
            "Turing",
            "Dijkstra",
            "Bobson",
            "Church",
            "Gödel"
    };

    public static Random random = new Random(System.currentTimeMillis());

    public static final int[] counters = new int[TransactionType.values().length];

    public static void main(String[] args) {
        String pathname = "./example.txt";
        writeFile(pathname);

        ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        new Thread(new AccountReader(pathname, threadPool)).start();
        try {
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted");
        }

        for(int i = 0; i < counters.length; i++){
            System.out.println(TransactionType.values()[i] + ": " + counters[i]);
        }
    }

    public static Account generateRandomAccount(){
        Account out = new Account(names[random.nextInt(names.length)] + " " + surnames[random.nextInt(surnames.length)]);

        for(int i = 0; i < random.nextInt(100); i++){
            out.transactions.add(generateRandomTransaction());
        }

        return out;
    }

    public static Transaction generateRandomTransaction(){
        return new Transaction(
                TransactionType.values()[new Random().nextInt(TransactionType.values().length)],
                random.nextBoolean() ? System.currentTimeMillis() - random.nextInt() : System.currentTimeMillis() + random.nextInt()
        );
    }

    public static void writeFile(String pathname){
        Path path = Paths.get(pathname);
        try {
            try {
                Files.createFile(path);
            } catch (FileAlreadyExistsException ignored) {}
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(new FileOutputStream(pathname)));
            writer.setIndent("\t");
            writer.beginArray();
            for(int i = random.nextInt(10000); i >= 0; i--) {
                gson.toJson(generateRandomAccount(), Account.class, writer);
            }
            writer.endArray();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
