public class Main {

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> Server.main(new String[]{"12345"})).start();
        Thread.sleep(200);
        new Thread(() -> Client.main(new String[]{"localhost", "12345"})).start();
    }
}
