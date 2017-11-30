import java.net.*;
import java.io.*;
import java.sql.Timestamp;

public class PingClient {
	public static void main(String[] args) throws Exception {
		if(args.length < 2) {
			System.err.println("ERR Insert Server Name and Server Port");
			return;
		}
		String serverName = args[0];
		int serverPort = Integer.parseInt(args[1]);

		if(!serverName.equals("localhost")) {
			System.err.println(serverName);
			System.err.println("ERR -arg 0");
			return;
		} //in questa maniera fisso la porta del server
		//avrei potuto testare in maniera analoga al PingServer sul sollevamento dell'eccezione
		//ovvero se il PingServer è collegato su quella porta allora si solleva una BindException,
		//ma si solleverebbe anche su tutte le porte note e avrebbe poco senso come controllo
		else if(serverPort != 1992) {
			System.err.println("ERR -arg 1");
			return;
		}
		
		InetAddress add = InetAddress.getByName(serverName);
		int persi = 0, min = 1000, max = 0;
		float avg = 0;
		boolean timeOut = false;
		DatagramSocket ds = new DatagramSocket();
		byte[] buf = new byte[1024];
		DatagramPacket dp = new DatagramPacket(buf, buf.length);		
		
		for(int i=0; i<10; i++) {
			ByteArrayOutputStream bois = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(bois);
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			String tss = ts.toString();
			
			dos.writeUTF("PING "+i+" "+tss);
			buf = bois.toByteArray();
			dp.setData(buf, 0, buf.length);
			dp.setAddress(add);
			dp.setPort(serverPort);
			ds.send(dp);
			ds.setSoTimeout(2000);
			try {
				ds.receive(dp);
			}catch(SocketTimeoutException e) {
				timeOut = true;
			}
			
			Timestamp ts2 = new Timestamp(System.currentTimeMillis());
			int diff = (ts2.getNanos()-ts.getNanos())/1000000;
			if(diff < 0)
				diff = 1000+diff;
			
			if(!timeOut) {
				System.out.println("PING "+i+" "+tss+" "+diff+" ms");
				if(diff<min)
					min = diff;
				else if(diff > max)
					max = diff;
				avg += diff;
			}
			else {
				System.out.println("*");
				timeOut = false;
				persi++;
			}
		}
		
		avg = avg/(10-persi);
		System.out.println();
		System.out.println("---- PING STATISTICS ----");
		System.out.println("10 packets transmitted, "+(10-persi)+" received, "+persi+"0% packet loss");
		System.out.println("round-trip (ms) min/avg/max = "+min+"/"+avg+"/"+max);
		System.out.println();
		
	}
}
