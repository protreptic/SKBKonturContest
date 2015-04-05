package skbkonturcontest.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class CopyOfTrieVocabulary implements Vocabulary {
	
	private class TrieNode {
		
		public int value;
		public TrieNode firstChild;
		public TrieNode nextSibling;

		public TrieNode(int value) {
			this.value = value;
		}
		
	}
	
	private TrieNode rootNode = new TrieNode('r');
	private int size = 0;
	private int maxDepth;
	
	public CopyOfTrieVocabulary(String path) {
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
				
				add(new Word(text, occurrenceFrequency)); 
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
		return suggest(prefix);
	}
	
	public boolean add(Word word) {
		if (add(rootNode, word, 0)) {
			size++;
			int n = word.getText().length();
			if (n > maxDepth) maxDepth = n;
			
			return true;
		}
		
		return false;
	}

	private boolean add(TrieNode root, Word word, int offset) {
		if (offset == word.getText().length()) return false;
		int c = word.getText().charAt(offset);

		// Search for node to add to
		TrieNode last = null, next = root.firstChild;
		while (next != null) {
			if (next.value < c) {
				// Not found yet, continue searching
				last = next;
				next = next.nextSibling;
			} else if (next.value == c) {
				// Match found, add remaining word to this node
				return add(next, word, offset + 1);
			}
			
			// Because of the ordering of the list getting here means we won't
			// find a match
			else break;
		}

		// No match found, create a new node and insert
		TrieNode node = new TrieNode(c);
		
		if (last == null) {
			// Insert node at the beginning of the list (Works for next == null too)
			root.firstChild = node;
			node.nextSibling = next;
		} else {
			// Insert between last and next
			last.nextSibling = node;
			node.nextSibling = next;
		}

		// Add remaining letters
		for (int i = offset + 1; i < word.getText().length(); i++) {
			node.firstChild = new TrieNode(word.getText().charAt(i));
			node = node.firstChild;
		}
		
		return true;
	}

	/*
	 * Adds any words found in this branch to the array
	 */
	private void getAll(TrieNode root, ArrayList<Word> words, char[] chars, int pointer) {
		TrieNode n = root.firstChild;
		while (n != null) {
			if (n.firstChild == null) {
				words.add(new Word(new String(chars, 0, pointer), 0));
			} else {
				chars[pointer] = (char)n.value;
				getAll(n, words, chars, pointer + 1);
			}
			n = n.nextSibling;
		}
	}

	public List<Word> suggest(String prefix) {
		return suggest(rootNode, prefix, 0);
	}

	private List<Word> suggest(TrieNode root, String word, int offset) {
		ArrayList<Word> words = null;
		
		if (offset == word.length()) {
			words = new ArrayList<Word>(size);
			char[] chars = new char[maxDepth];
			for (int i = 0; i < offset; i++)
				chars[i] = word.charAt(i);
			getAll(root, words, chars, offset);
			if (words.size() > 10) {
				return words.subList(0, 10);
			}
			return words;
		}
		int c = word.charAt(offset);

		// Search for node to add to
		TrieNode next = root.firstChild;
		while (next != null) {
			if (next.value < c) next = next.nextSibling;
			else if (next.value == c) return suggest(next, word, offset + 1);
			else break;
		}
		
		return words;
	}

	@Override
	public String[] findSuggestions(String prefix) {
		// TODO Auto-generated method stub
		return null;
	}

}
