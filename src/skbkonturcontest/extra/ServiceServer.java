package skbkonturcontest.extra;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import skbkonturcontest.main.ListVocabulary;
import skbkonturcontest.main.Vocabulary;

public class ServiceServer {

	public static void main(String[] args) {
		//String pathToVocabulary = args[0];
		//int port = Integer.valueOf(args[1]);
		
		Vocabulary dictionary = new ListVocabulary("/home/petronic/Загрузки/Тестовое задание Java/test.in");
		
		ExecutorService executorService = Executors.newCachedThreadPool();
		
		try {
			ServerSocket serverSocket = new ServerSocket(34123);
			
			while (true) {
				Socket socket = serverSocket.accept();
				executorService.execute(new ClientThread(socket, dictionary)); 
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void usageDescription() {
		System.out.println("Usage: <path_to_vocabulary> <port>");
	}

}
