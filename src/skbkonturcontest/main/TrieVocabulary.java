package skbkonturcontest.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class TrieVocabulary implements Vocabulary {
	
	private class TrieNode {
		
		public int value;
		public TrieNode firstChild;
		public TrieNode nextSibling;

		public TrieNode(int value) {
			this.value = value;
			firstChild = null;
			nextSibling = null;
		}
		
	}
	
	private TrieNode rootNode;
	private int size;
	private int maxDepth;
	
	public TrieVocabulary(String path) {
		rootNode = new TrieNode('r');
		size = 0;
		
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
				
				add(text);
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
		return null; 
	}
	
	public String[] findSuggestions(String prefix) {
		return suggest(prefix); 
	}
	
	public boolean add(String word) {
		if (add(rootNode, word, 0)) {
			size++;
			int n = word.length();
			if (n > maxDepth) maxDepth = n;
			
			return true;
		}
		
		return false;
	}

	private boolean add(TrieNode root, String word, int offset) {
		if (offset == word.length()) return false;
		int c = word.charAt(offset);

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
		for (int i = offset + 1; i < word.length(); i++) {
			node.firstChild = new TrieNode(word.charAt(i));
			node = node.firstChild;
		}
		
		return true;
	}

	/*
	 * Adds any words found in this branch to the array
	 */
	private void getAll(TrieNode root, ArrayList<String> words, char[] chars, int pointer) {
		TrieNode n = root.firstChild;
		while (n != null) {
			if (n.firstChild == null) {
				words.add(new String(chars, 0, pointer));
			} else {
				chars[pointer] = (char)n.value;
				getAll(n, words, chars, pointer + 1);
			}
			n = n.nextSibling;
		}
	}

	public String[] suggest(String prefix) {
		return suggest(rootNode, prefix, 0);
	}

	private String[] suggest(TrieNode root, String word, int offset) {
		if (offset == word.length()) {
			ArrayList<String> words = new ArrayList<String>(size);
			char[] chars = new char[maxDepth];
			for (int i = 0; i < offset; i++)
				chars[i] = word.charAt(i);
			getAll(root, words, chars, offset);
			if (words.size() > 10) {
				return words.subList(0, 10).toArray(new String[10]);
			}
			return words.toArray(new String[words.size()]);
		}
		int c = word.charAt(offset);

		// Search for node to add to
		TrieNode next = root.firstChild;
		while (next != null) {
			if (next.value < c) next = next.nextSibling;
			else if (next.value == c) return suggest(next, word, offset + 1);
			else break;
		}
		
		return null;
	}

}
