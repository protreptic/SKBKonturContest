package skbkonturcontest.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

public class ListVocabulary implements Vocabulary {

	private List<Word> words = new ArrayList<Word>();
	private Comparator<Word> comparator = new OccurrenceFrequencyComparator(); 
	
	public ListVocabulary(String path) {
		openDictionary(path);  
	}

	private void openDictionary(String path) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));

 			int n = Integer.valueOf(reader.readLine());
			
			for (int i = 0; i < n; i++) {
				String temp = reader.readLine();
				
				StringTokenizer tokenizer = new StringTokenizer(temp);
				
				String text = tokenizer.nextToken();
				Integer occurrenceFrequency = Integer.valueOf(tokenizer.nextToken()); 
				
				words.add(new Word(text, occurrenceFrequency));
			}
			
			reader.close();
			reader = null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public List<Word> findSuggestions(String prefix, Integer maxSuggestions) {
		List<Word> suggestions = new ArrayList<Word>();
		
		for (Word word : words) {
			if (word.getText().startsWith(prefix)) {
				suggestions.add(word);
			}
			
			if (suggestions.size() == maxSuggestions) break;
		}
	
		if (suggestions.size() > 1) {
			Collections.sort(suggestions, comparator);
			suggestions = suggestions.subList(0, (suggestions.size() < maxSuggestions) ? suggestions.size() : maxSuggestions);
		}
		
		return suggestions;
	}

	@Override
	public String[] findSuggestions(String prefix) {
		// TODO Auto-generated method stub
		return null;
	}

}
