package skbkonturcontest.extra;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServiceClient {
	
	public static void main(String[] args) {
//		String server = args[0];
//		int port = Integer.valueOf(args[1]);
		
		try {
			Socket socket = new Socket("localhost", 34123);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			writer.println("get bbc");
			
			while (true) {
				String message = reader.readLine();
				
				System.out.println(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void usageDescription() {
		System.out.println("Usage: <server> <port>");
	}
}
