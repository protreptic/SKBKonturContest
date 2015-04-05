package skbkonturcontest.main;

import java.util.Comparator;

public class AlphabeticalOrderComparator implements Comparator<Word> {

	@Override
	public int compare(Word o1, Word o2) {
		int res = String.CASE_INSENSITIVE_ORDER.compare(o1.getText(), o2.getText());
		return res != 0 ? res : o1.getText().compareTo(o2.getText()); 
	}
 
}
