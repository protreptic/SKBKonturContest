package skbkonturcontest.main;

import java.sql.Timestamp;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SKBKonturContest {

	public static void main(String[] args) {
		Vocabulary dictionary = new TrieVocabulary("/home/petronic/Загрузки/Тестовое задание Java/test.in");
		UserQueries userQueries = new UserQueries("/home/petronic/Загрузки/Тестовое задание Java/test.in");

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

}
