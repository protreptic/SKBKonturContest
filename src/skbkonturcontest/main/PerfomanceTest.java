package skbkonturcontest.main;

import java.sql.Timestamp;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PerfomanceTest {
	
	public static void main(String[] args) {
		if (args.length != 1 && args[0] != null && !args[0].isEmpty()) {
			usageDescription();
			System.exit(1);
		}
		
		String pathToVocabulary = args[0];
		
		Vocabulary dictionary = new TrieVocabulary(pathToVocabulary);
		UserQueries userQueries = new UserQueries(pathToVocabulary);

		ExecutorService exec = Executors.newCachedThreadPool();
		
		Timestamp begin = new Timestamp(System.currentTimeMillis());
		
		for (String userQuery : userQueries.getUserQueries()) {
			exec.execute(new TrieSuggestionQuery(dictionary, userQuery));
		}

		exec.shutdown();
		
		try {
			if (exec.awaitTermination(10, TimeUnit.SECONDS)) {
				Timestamp end = new Timestamp(System.currentTimeMillis());
				
				System.out.println(String.format("Runtime success: %f (sec)", (float) (end.getTime() - begin.getTime()) / 1000)); 
			} else {
				System.out.println("Runtime failed: 10 seconds passed!"); 
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void usageDescription() {
		System.out.println("Usage: <path_to_vocabulary>");
	}
	
}
