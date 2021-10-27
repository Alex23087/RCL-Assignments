import java.io.*;
import java.net.ServerSocket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        int nThreads = Runtime.getRuntime().availableProcessors();
        Executor pool = Executors.newFixedThreadPool(nThreads);
        try(ServerSocket serverSocket = new ServerSocket(6789)){
            System.out.println("Listening on " + serverSocket.getLocalSocketAddress());
            while(true) {
                pool.execute(new ClientHandler(serverSocket.accept()));
            }
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }
}