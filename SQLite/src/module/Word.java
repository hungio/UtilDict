package module;

public class Word {
	private String strWord;				// A utf-8 string terminated by '\0'.
	private long dataOffsetWord;	// Word data's offset in .dict file.
	private long dataSizeWord;		// Word data's total size in .dict file.
	
	public String getStrWord() {
		return strWord;
	}
	public void setStrWord(String strWord) {
		this.strWord = strWord;
	}
	public long getDataOffsetWord() {
		return dataOffsetWord;
	}
	public void setDataOffsetWord(long dataOffsetWord) {
		this.dataOffsetWord = dataOffsetWord;
	}
	public long getDataSizeWord() {
		return dataSizeWord;
	}
	public void setDataSizeWord(long dataSizeWord) {
		this.dataSizeWord = dataSizeWord;
	}
}
