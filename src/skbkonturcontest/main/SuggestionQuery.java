package skbkonturcontest.main;

import java.util.List;

public class SuggestionQuery implements Runnable {

	private Vocabulary vocabulary;
	private String prefix;
	
	public SuggestionQuery(Vocabulary vocabulary, String prefix) {
		this.vocabulary = vocabulary;
		this.prefix = prefix;
	}
	
	@Override
	public void run() {
		List<Word> suggestions = vocabulary.findSuggestions(prefix, 10);
		
		synchronized (System.out) {
			if (!suggestions.isEmpty()) {
				for (Word suggestion : suggestions) {
					System.out.println(suggestion);
				}
				
				System.out.println();
			}							
		}
	}

}
