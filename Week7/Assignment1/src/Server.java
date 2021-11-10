import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Server {
	public static final int RESPONSE_LENGTH = 32;

	public static void main(String[] args) {
		if(args.length < 1){
			System.err.println("Invalid number of parameters passed");
			return;
		}

		int port;
		try{
			port = Integer.parseInt(args[0]);
		} catch (NumberFormatException nfe){
			System.err.println("ERR -arg 0");
			return;
		}

		long seed;
		if(args.length > 1){
			try{
				seed = Long.parseLong(args[1]);
			} catch (NumberFormatException nfe){
				System.err.println("ERR -arg 1");
				return;
			}
		}else{
			seed = System.currentTimeMillis();
		}

		Random rand = new Random(seed);

		DatagramSocket socket;
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException se){
			se.printStackTrace();
			return;
		}
		System.out.println("Server listening on port " + port);

		while(true){
			DatagramPacket request = new DatagramPacket(new byte[RESPONSE_LENGTH], RESPONSE_LENGTH);
			try {
				socket.receive(request);
				System.out.println(request.getAddress() + ":" + request.getPort());
				System.out.println(new String(request.getData(), 0, request.getLength(), StandardCharsets.US_ASCII));
				if(getRandomBoolean(rand, 0.75f)) {
					DatagramPacket response = new DatagramPacket(request.getData(), request.getData().length, request.getAddress(), request.getPort());
					long delay = rand.nextInt(500);
					System.out.println("Sending ping with a delay of " + delay + "ms");
					Thread.sleep(delay);
					socket.send(response);
				}else{
					System.out.println("Response not sent");
				}
			} catch (IOException ioe){
				ioe.printStackTrace();
			} catch (InterruptedException ie){
				System.out.println("Thread interrupted");
				return;
			}
		}
	}

	private static boolean getRandomBoolean(Random r, float p){
		return r.nextFloat() < p;
	}
}
