package skbkonturcontest.main;

public class TrieSuggestionQuery implements Runnable {

	private Vocabulary vocabulary;
	private String prefix;
	
	public TrieSuggestionQuery(Vocabulary vocabulary, String prefix) {
		this.vocabulary = vocabulary;
		this.prefix = prefix;
	}
	
	@Override
	public void run() {
		String[] suggestions = vocabulary.findSuggestions(prefix);
		
		if (suggestions != null) {
			synchronized (System.out) {
				for (String suggestion : suggestions) {
					System.out.println(suggestion);
				}
				
				System.out.println();						
			}
		}
	}

}
