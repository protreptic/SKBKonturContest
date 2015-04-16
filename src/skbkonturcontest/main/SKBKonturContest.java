package skbkonturcontest.main;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SKBKonturContest {

	public static void main(String[] args) {
		if (args.length != 1 && args[0] != null && !args[0].isEmpty()) {
			usageDescription();
			System.exit(1);
		}
		
		String pathToVocabulary = args[0];
		
		final Vocabulary dictionary = new TrieVocabulary(pathToVocabulary);
		
		ExecutorService executorService = Executors.newCachedThreadPool();
		
		Scanner scanner = new Scanner(System.in);
		
		while (scanner.hasNextLine()) {
			executorService.execute(new TrieSuggestionQuery(dictionary, scanner.nextLine()));
		}
		
		scanner.close();
		
		executorService.shutdown();
	}
	
	public static void usageDescription() {
		System.out.println("Usage: <path_to_vocabulary>");
	}
	
}
