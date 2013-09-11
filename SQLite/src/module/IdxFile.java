package module;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


public class IdxFile {
	private long wordCount;
	private long fileSize;
	private boolean isAvailable = false;
	private ArrayList<Word> wordList;
	
	/* Constructor */
	public IdxFile (String fileName, long wordCount, long fileSize) {
		setWordCount(wordCount);
		setFileSize(fileSize);
		try {
			DataInputStream dis = new DataInputStream(
					new BufferedInputStream(
							new FileInputStream(fileName)));
			byte[] b = new byte[(int) getFileSize()];
			dis.read(b);
			dis.close();
			
			int startOffset = 0;
			int endOffset = 0;
			Word word = null;
			wordList = new ArrayList<Word>();
			for(long l = 0; l < getWordCount(); l++){
				word = new Word();
				startOffset = endOffset;
				while(b[endOffset] != '\0'){
					endOffset++;
				}
				
				word.setStrWord( new String(b, startOffset, endOffset - startOffset, "UTF8"));
				endOffset ++;
				word.setDataOffsetWord( readInt(b, endOffset));
				endOffset += 4;
				word.setDataSizeWord( readInt(b, endOffset));
				endOffset += 4;
				wordList.add(word);
				
			}
			setAvailable(true);
			System.out.println("Loading \".idx\" file successful!");
			
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: Reading meaning word in \".idx\" file!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("ERROR: Reading meaning word in \".idx\" file!");
			e.printStackTrace();
		}
	}
	
	public Word lookUp(String word){
		long left = 0;
		long right = getWordCount() - 1;
		long mid = (long) (left + right) / 2;
		String lWord = word.toLowerCase();
		while( right >= left){
			int cmp = lWord.compareTo(wordList.get((int) mid).getStrWord().toLowerCase());
			
			if( cmp == 0){
				return wordList.get((int) mid);
			} else if (cmp > 0 ){
				left = mid + 1;
			} else {
				right = mid - 1;
			}
			
			mid = (long) (left + right) / 2;
		}
		
		return null;
	}

	private long readInt(byte[] b, int offset) throws EOFException {
		int ch1 = 0xFF & (int)b[offset];
		int ch2 = 0xFF & (int)b[offset + 1];
		int ch3 = 0xFF & (int)b[offset + 2];
		int ch4 = 0xFF & (int)b[offset + 3];
		if((ch1 | ch2 | ch3 | ch4) < 0)
			throw new EOFException();
		return (long) ( (ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0)) & 0xFFFFFFFF;
	}

	public long getWordCount() {
		return wordCount;
	}

	public void setWordCount(long wordCount) {
		this.wordCount = wordCount;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	
	public ArrayList<Word> getWordlist(){
		return wordList;
	}
}
