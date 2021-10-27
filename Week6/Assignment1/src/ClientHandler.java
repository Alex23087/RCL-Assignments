import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable{
    private final Socket clientSocket;
    private Scanner scanner;
    private PrintWriter printWriter;
    private static final String notFoundString = "The document has not been found";

    public ClientHandler(Socket clientSocket){
        this.clientSocket = clientSocket;
        try {
            this.scanner = new Scanner(clientSocket.getInputStream());
            this.printWriter = new PrintWriter(clientSocket.getOutputStream());
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (clientSocket.isConnected()) {
                String str = scanner.nextLine();
                if(str.startsWith("GET")){ //Only gets the GET line, ignores other requests and headers
                    System.out.println("Client " + clientSocket.getInetAddress() + ":" + clientSocket.getPort() + " issued request:\n" + str);
                    String path = str.substring(5, str.indexOf(" ", 5));
                    File file = new File(path);
                    if(file.exists()){
                        printWriter.println("HTTP/1.1 200 OK");
                        printWriter.println("Content-Length: " + file.length());
                        printWriter.println("Content-Type: " + MimeParser.getTypeFromFilename(file.getName()).toString());
                        printWriter.println();
                        printWriter.flush();
                        InputStream is = new FileInputStream(file);
                        OutputStream os = clientSocket.getOutputStream();
                        os.write(is.readAllBytes());
                    }else{
                        printWriter.println("HTTP/1.1 404 Not Found");
                        printWriter.println("Content-Length: " + notFoundString.length());
                        printWriter.println("Content-Type: text/plain");
                        printWriter.println();
                        printWriter.println(notFoundString);
                    }
                    printWriter.println();
                    printWriter.flush();
                }
            }
        }catch(Exception e){
            System.out.println("Client " + clientSocket.getInetAddress() + ":" + clientSocket.getPort() + " disconnected");
        }
    }
}