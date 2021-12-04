import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class ServerMain {
    static int port = 4242;

    public static void main(String[] args) {
        Selector selector;
        ServerSocketChannel serverSocketChannel;
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Server started on port " + port);

        try {
            while(true){
                selector.select();
                Set<SelectionKey> selectKeys = selector.selectedKeys();
                for(SelectionKey key : selectKeys){
                    if(key.isValid()) {
                        if (key.isAcceptable()) {
                            ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                            SocketChannel clientChannel = serverChannel.accept();
                            clientChannel.configureBlocking(false);
                            clientChannel.register(selector, SelectionKey.OP_READ);
                            System.out.println("Accepted connection from client " + clientChannel.getRemoteAddress());
                        } else if (key.isReadable()) {
                            SocketChannel clientChannel = (SocketChannel) key.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(512);
                            StringBuilder strbldr = new StringBuilder();
                            int bytesRead = -1;
                            while ((bytesRead = clientChannel.read(buffer)) > 0) {
                                buffer.flip();
                                while (buffer.hasRemaining()) {
                                    char c = (char) Byte.toUnsignedInt(buffer.get());
                                    strbldr.append(c);
                                }
                                buffer.clear();
                            }
                            System.out.println("Client sent " + strbldr.toString());

                            ReadableByteChannel inputChannel = Channels.newChannel(new ByteArrayInputStream(strbldr.toString().getBytes(StandardCharsets.US_ASCII)));
                            buffer = ByteBuffer.allocate(strbldr.toString().length());
                            inputChannel.read(buffer);
                            buffer.flip();
                            clientChannel.write(buffer);
                            buffer.clear();
                            clientChannel.close();
                        }
                    }
                    selectKeys.remove(key);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
}

