import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClientMain{
    public static void main(String[] args) {
        System.out.println("Input a string to send to the server");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        try {
            SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(4242));
            ByteBuffer buffer = ByteBuffer.allocate(input.length());

            ReadableByteChannel inputChannel = Channels.newChannel(new ByteArrayInputStream(input.getBytes(StandardCharsets.US_ASCII)));

            inputChannel.read(buffer);
            buffer.flip();
            socketChannel.write(buffer);
            buffer.clear();

            StringBuilder strbldr = new StringBuilder();
            socketChannel.read(buffer);
            buffer.flip();
            while (buffer.hasRemaining()) {
                strbldr.append((char) buffer.get());
            }
            System.out.println("Server sent " + strbldr.toString());
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}