package skbkonturcontest.main;

import java.util.List;

public class TrieSuggestionQuery implements Runnable {

	private Vocabulary vocabulary;
	private String prefix;
	
	public TrieSuggestionQuery(Vocabulary vocabulary, String prefix) {
		this.vocabulary = vocabulary;
		this.prefix = prefix;
	}
	
	@Override
	public void run() {
		List<Word> suggestions = vocabulary.findSuggestions(prefix);
		
		if (!suggestions.isEmpty()) {
			synchronized (System.out) {
				for (Word suggestion : suggestions) {
					System.out.println(suggestion.getText());
				}
				
				System.out.println();						
			}
		}
	}

}
