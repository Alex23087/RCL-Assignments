import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Client {
	public static final int RESPONSE_LENGTH = 32;

	public static void main(String[] args) {
		if(args.length < 2){
			System.err.println("Invalid number of arguments passed");
			return;
		}
		String name = args[0];
		int port;
		try{
			port = Integer.parseInt(args[1]);
		}catch (NumberFormatException nfe){
			System.err.println("ERR -arg 1");
			return;
		}
		InetAddress address;
		try {
			address = InetAddress.getByName(name);
		}catch (UnknownHostException uhe){
			System.err.println("ERR -arg 0");
			return;
		}

		DatagramSocket socket;
		try {
			socket = new DatagramSocket(0);
			socket.setSoTimeout(2000);
		} catch (SocketException se){
			se.printStackTrace();
			return;
		}

		ArrayList<Long> rtts = new ArrayList<>(10);
		for(int i = 0; i < 10; i ++) {
			try {
				long requestTimestamp = System.currentTimeMillis();
				String msg = "PING " + i + " " + requestTimestamp;
				System.out.println(msg);
				DatagramPacket request = new DatagramPacket(msg.getBytes(StandardCharsets.US_ASCII), msg.length(), address, port);
				socket.send(request);
				DatagramPacket response = new DatagramPacket(new byte[RESPONSE_LENGTH], RESPONSE_LENGTH);
				socket.receive(response);
				long responseTimestamp = System.currentTimeMillis();
				System.out.println("RTT: " + (responseTimestamp - requestTimestamp) + "ms");
				rtts.add(responseTimestamp - requestTimestamp);
			} catch (SocketTimeoutException ste){
				System.out.println("*");
			} catch (IOException ioe){
				ioe.printStackTrace();
			}
		}

		System.out.println(
				new StringBuilder("\t\t---- PING Statistics ----\n10 packets transmitted, ")
						.append(rtts.size())
						.append(" packets received, ")
						.append(10 - rtts.size())
						.append("0% packet loss\n\tround-trip (ms) min/avg/max = ")
						.append(rtts.stream().min(Long::compare).orElse(0L))
						.append('/')
						.append(String.format("%.2f", rtts.stream().reduce(Long::sum).orElse(0L) / (double)(rtts.isEmpty() ? 1 : rtts.size())))
						.append('/')
						.append(rtts.stream().max(Long::compare).orElse(0L))
		);
	}
}
