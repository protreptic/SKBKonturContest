package skbkonturcontest.main;

import java.util.List;

public interface Vocabulary {
	
	public List<Word> findSuggestions(String prefix);
	
}
