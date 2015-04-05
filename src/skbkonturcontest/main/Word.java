package skbkonturcontest.main;

public class Word {

	private String text;
	private int occurrenceFrequency;
	
	public Word(String text, int occurrenceFrequency) {
		this.text = text;
		this.occurrenceFrequency = occurrenceFrequency;
	}
	
	public String getText() {
		return this.text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public int getOccurrenceFrequency() {
		return this.occurrenceFrequency;
	}
	
	public void setOccurrenceFrequency(Integer occurrenceFrequency) {
		this.occurrenceFrequency = occurrenceFrequency;
	}
	
	@Override
	public String toString() {
		return text;
	}

}
