import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class ServerMain {

    public static void main(String[] args) {
        if(args.length < 2){
            System.err.println("Please pass multicast address and port as parameters");
            return;
        }
        String address = args[0];
        int port = -1;
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException nfe){
            System.err.println("Invalid port passed");
            return;
        }
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(address);
        } catch (UnknownHostException e) {
            System.err.println("Check that the address is correct");
            return;
        }

        try {
            DatagramSocket datagramSocket = new DatagramSocket();
            while(true){
                String msg = LocalDateTime.now().toString();
                byte[] data = msg.getBytes(StandardCharsets.UTF_8);
                DatagramPacket packet = new DatagramPacket(data, data.length, inetAddress, port);
                datagramSocket.send(packet);
                Thread.sleep(5000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}