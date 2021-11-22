import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;

public class AccountReader implements Runnable{
    private final String pathname;
    private final ExecutorService threadPool;

    public AccountReader(String pathname, ExecutorService threadPool){
        this.pathname = pathname;
        this.threadPool = threadPool;
    }

    @Override
    public void run() {
        try (JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream(pathname)))){
            reader.beginArray();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            while(reader.hasNext()){
                Account account = gson.fromJson(reader, Account.class);
                threadPool.execute(new AccountCounter(account));
            }
            reader.endArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        threadPool.shutdown();
    }
}