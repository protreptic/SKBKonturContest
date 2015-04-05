package skbkonturcontest.main;

import java.util.List;

public class CopyOfTrieSuggestionQuery implements Runnable {

	private Vocabulary vocabulary;
	private String prefix;
	
	public CopyOfTrieSuggestionQuery(Vocabulary vocabulary, String prefix) {
		this.vocabulary = vocabulary;
		this.prefix = prefix;
	}
	
	@Override
	public void run() {
		List<Word> suggestions = vocabulary.findSuggestions(prefix, 0);
		
		if (suggestions != null && !suggestions.isEmpty()) {
			synchronized (System.out) {
				for (Word suggestion : suggestions) {
					System.out.println(suggestion);
				}
				
				System.out.println();						
			}
		}
	}

}
