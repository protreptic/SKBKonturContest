package skbkonturcontest.extra;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.StringTokenizer;

import skbkonturcontest.main.Vocabulary;
import skbkonturcontest.main.Word;

public class ClientThread implements Runnable {
	
	private Socket socket;
	
	private Vocabulary dictionary;
	
	private PrintWriter writer;
	private BufferedReader reader;
	
	public ClientThread(Socket socket, Vocabulary dictionary) {
		this.socket = socket;
		this.dictionary  = dictionary;
	}
	
	@Override
	public void run() {
		try {
			writer = new PrintWriter(socket.getOutputStream(), true);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			System.out.println(socket.getInetAddress() + ":" + socket.getPort() + " connected!"); 
			
			while (true) {
				String message = reader.readLine();
				
				if (message.startsWith("get ")) {
					StringTokenizer tokenizer = new StringTokenizer(message);
					tokenizer.nextToken();
					
					String prefix = tokenizer.nextToken();
					
					List<Word> suggestions = dictionary.findSuggestions(prefix, 10);
					
					if (!suggestions.isEmpty()) {
						for (Word suggestion : suggestions) {
							writer.println(suggestion);
						}
						
						writer.println();
					}							
				} else {
					break;
				}
			}			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
