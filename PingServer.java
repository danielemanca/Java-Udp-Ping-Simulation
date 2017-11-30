import java.net.*;
import java.io.*;

public class PingServer {
	public static void main(String[] args) throws Exception{
		InetAddress add = InetAddress.getByName("localhost");
		if(args.length == 0) {
			System.err.println("ERR Insert Server Port");
			return;
		}
		//lanciare con serverPort = 1992 come spiegato su commento in PingClient
		int serverPort = Integer.parseInt(args[0]);
		DatagramSocket ds;
		try {
			ds = new DatagramSocket(serverPort);
		}catch(BindException be) {
			System.err.println("ERR -arg 0");
			return;
		}
		byte[] buf = new byte[1024];
		DatagramPacket dp = new DatagramPacket(buf, buf.length);
		while(true) {
			for(int i=0; i<10; i++) {
				ds.receive(dp);				
				
				ByteArrayInputStream bais = new ByteArrayInputStream(dp.getData(), 0, dp.getLength());
				DataInputStream dis = new DataInputStream(bais);
				
				InetAddress sourceIp = dp.getAddress();
				int sourcePort = dp.getPort();
				String data = dis.readUTF();
				
				int rand = (int) (Math.random()*100);
				int delay = (int) (Math.random()*300)+1;
				
				if(rand > 25) {
					dp.setAddress(add);
					dp.setPort(sourcePort);
					Thread.sleep(delay);
					ds.send(dp);
					System.out.println(sourceIp+":"+sourcePort+" "+data+" ACTION: delayed "+delay+" ms");
				}
				else {
					System.out.println(sourceIp+":"+sourcePort+" "+data+" ACTION: not sent");
				}		
			}
		}
	}
}
