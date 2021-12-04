import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

class ClientMain{
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

		MulticastSocket multicastSocket = null;
		InetAddress group;
		try {
			group = InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return;
		}
		try {
			multicastSocket = new MulticastSocket(port);
			multicastSocket.joinGroup(group);

			for(int i = 0; i < 10; i++){
				DatagramPacket packet = new DatagramPacket(new byte[64], 64);
				multicastSocket.receive(packet);
				String msg = new String(packet.getData(), StandardCharsets.UTF_8);
				System.out.println(msg);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(multicastSocket != null){
				try {
					multicastSocket.leaveGroup(group);
					multicastSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}