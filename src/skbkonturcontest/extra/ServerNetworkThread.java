package skbkonturcontest.extra;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import skbkonturcontest.main.Vocabulary;

public class ServerNetworkThread extends Thread {

	private ServerSocket serverSocket;
	private Vocabulary vocabulary;
	
	public ServerNetworkThread(Vocabulary vocabulary, int port) {
		this.vocabulary = vocabulary;
		
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		runned = true;
	}
	
	private volatile Boolean runned;
	
	@Override
	public void run() {
		System.out.println("Server has been started at port " + serverSocket.getLocalPort()); 
		System.out.println();
		
		ExecutorService executorService = Executors.newCachedThreadPool();
		
		while (runned) {
			try {
				Socket socket = serverSocket.accept();
				
				executorService.execute(new ClientThread(socket, vocabulary));
				
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			Thread.yield();  
		}
		
		executorService.shutdown();
		
		try {
			if (executorService.awaitTermination(5, TimeUnit.SECONDS)) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else throw new RuntimeException("Unexpected behavior");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println();
		System.out.println("Server has been shut down"); 
	}
	
}
