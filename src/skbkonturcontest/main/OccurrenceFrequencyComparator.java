package skbkonturcontest.main;

import java.util.Comparator;

public class OccurrenceFrequencyComparator  implements Comparator<Word> {

	@Override
	public int compare(Word o1, Word o2) {
		if (o1.getOccurrenceFrequency() == o2.getOccurrenceFrequency()) return 0;
		if (o1.getOccurrenceFrequency() > o2.getOccurrenceFrequency()) return -1;
		
		return 1;
	}
	
}