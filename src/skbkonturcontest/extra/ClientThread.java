package skbkonturcontest.extra;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import skbkonturcontest.main.Vocabulary;
import skbkonturcontest.main.Word;

public class ClientThread implements Runnable {
	
	private Socket socket;
	
	private volatile Boolean connected;
	
	private Vocabulary vocabulary;
	
	public ClientThread(Socket socket, Vocabulary vocabulary) {
		this.socket = socket;
		this.vocabulary  = vocabulary;
		
		connected = true;
	}
	
	@Override
	public void run() {
		ExecutorService executorService = Executors.newCachedThreadPool();
		
		try {
			executorService.execute(new IncomingThread(socket.getInputStream()));
			executorService.execute(new OutcomingThread(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while (connected) {
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		executorService.shutdown();
		
		try {
			if (executorService.awaitTermination(10, TimeUnit.SECONDS)) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else throw new RuntimeException("Unexpected behavior");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private class IncomingThread implements Runnable {

		private BufferedReader reader;
		
		public IncomingThread(InputStream inputStream) {
			reader = new BufferedReader(new InputStreamReader(inputStream));
		}
		
		@Override
		public void run() {
			while (connected) {
				try {
					if (reader.ready()) {
						String command = reader.readLine();
						
						if (command != null && !command.isEmpty()) {
							if (command.startsWith("get ")) {
								StringTokenizer tokenizer = new StringTokenizer(command);
								tokenizer.nextToken();
								
								String prefix = tokenizer.nextToken();
								
								List<Word> suggestions = vocabulary.findSuggestions(prefix);
								
								synchronized (System.out) {
									System.out.println("[" + socket.getInetAddress() + ":" + socket.getPort() + "]: get -> " + prefix + " (" + ((suggestions != null) ? suggestions.size() : "0") + ")"); 
								}
								
								if (!suggestions.isEmpty()) {
									for (Word suggestion : suggestions) {
										PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
										
										writer.println(suggestion.getText());
									}
								}
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				try {
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	private class OutcomingThread implements Runnable {

		private PrintWriter writer;
		
		public OutcomingThread(OutputStream outputStream) {
			writer = new PrintWriter(outputStream, true);
		}
		
		@Override
		public void run() {
			Scanner scanner = new Scanner(System.in);
			
			while(connected) {
				if (scanner.hasNextLine()) {
					String command = scanner.nextLine();
					
					if (command != null && !command.isEmpty()) {
 						if (command.startsWith("get ")) {
							writer.println(command); 
						} else if (command.startsWith("quit")) {
							connected = false;
						} else {
							synchronized (System.out) {
								System.out.println("unknown command, use: get <prefix> or quit");  
							}
						}
					}
				}
				
				try {
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			scanner.close();
		}
		
	}
	
}
