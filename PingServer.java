import java.net.*;
import java.io.*;

public class PingServer {
	public static void main(String[] args) throws Exception{
		InetAddress add = InetAddress.getByName("localhost");
		int serverPort = 1992;
		byte[] buf = new byte[1024];
		DatagramPacket dp = new DatagramPacket(buf, buf.length);
		DatagramSocket ds;
		
		try {
			ds = new DatagramSocket(serverPort);
		}catch(BindException be) {
			System.err.println("ERR Port Unavailable");
			return;
		}
		
		
		
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
				
				//Simulates packet loss
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
