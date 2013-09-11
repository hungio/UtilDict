package module;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import util.Constant;

class UdtIdx{
	private String word;
	private long offset;
	
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public long getOffset() {
		return offset;
	}
	public void setOffset(long offset) {
		this.offset = offset;
	}
}

public class IdxFile {
	private long wordCount;
	private long fileSize;
	private int udtIdxNum;
	private String udtPath = null;
	private boolean isAvailable = false;
	private List<Word> wordList;
	private List<UdtIdx> udtIdxList;
	private byte[] idxData;
	
	/* Constructor */
	public IdxFile (String fileName, long wordCount, long fileSize) {
		setWordCount(wordCount);
		setFileSize(fileSize);
		setUdtFile(fileName);
		setUdtIdxNum();
		if(!(new File(udtPath)).exists()){
			createIndex(fileName);
		}
		setUdtIdxList();
		
		try {
			DataInputStream dis = new DataInputStream(
						new FileInputStream(fileName));
			idxData = new byte[(int) fileSize];
			dis.readFully(idxData);
			dis.close();
			wordList = new ArrayList<Word>();
			setAvailable(true);
			System.out.println("Loading \".idx\" file successful!");
			
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: Reading meaning word in \".idx\" file!" + e);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("ERROR: Reading meaning word in \".idx\" file!" + e);
			e.printStackTrace();
		}
	}

	private void setUdtFile(String fileName){
		File file = new File(fileName);
		String name = file.getName();
		int pos = name.lastIndexOf(".");
		String udtString = pos > 0 ? name.substring(0, pos) : name;
		udtPath = file.getParent() + File.separator + udtString + Constant.EXT_UDT;
	}
	
	public List<Word>SearchUdt(String word){
		if(word == "")
			return null;
		
		word = encodeUTF8(word);
		if(!wordList.isEmpty()){
			int l = word.toLowerCase().compareTo(wordList.get(0).getStrWord().toLowerCase());
			int r = 0;
			if(wordList.size() > udtIdxNum)
				r = word.toLowerCase().compareTo(wordList.get(udtIdxNum-1).getStrWord().toLowerCase());
			else
				r = word.toLowerCase().compareTo(wordList.get(wordList.size()-1).getStrWord().toLowerCase());
			if(l >= 0 && r <= 0)
				return SearchWordList(word);
		}
		
		int left = 0;
		int right = udtIdxList.size() - 1;
		int root = (left + right)/2;
		String lWord = word.toLowerCase();
		while(right >= left && root <= right && root >= left){
			int cmp = lWord.compareTo(udtIdxList.get(root).getWord().toLowerCase());
			if(cmp >= 0){
				if(lWord.compareTo(udtIdxList.get(root + 1).getWord().toLowerCase()) < 0){
					if(root == udtIdxList.size() -1)
						SetWordList(udtIdxList.get(root).getOffset(), udtIdxList.size() - 1);
					else
						SetWordList(udtIdxList.get(root).getOffset(), udtIdxList.get(root+1).getOffset() - 1);
					return SearchWordList(word);
				} else
					left = root + 1;
			} else {
				if(lWord.compareTo(udtIdxList.get(root - 1).getWord().toLowerCase()) > 0){
					SetWordList(udtIdxList.get(root - 1).getOffset(), udtIdxList.get(root).getOffset());
					return SearchWordList(word);
				} else
					right = root - 1;
			}
			
			root = (left + right)/2;
		}
		return null;
	}
	
	private List<Word> SearchWordList(String word) {
		long left = 0;
		long right = wordList.size() - 1;
		long mid = (long) (left + right) / 2;
		String lWord = word.toLowerCase();
		int cmp  = 0;
		while( right >= left){
			cmp = lWord.compareTo(wordList.get((int) mid).getStrWord().toLowerCase());
			
			if( cmp == 0){ System.out.println(mid);
				return wordList.subList((int)mid, (int)mid+10);
			} else if (cmp > 0 ){
				left = mid + 1;
			} else {
				right = mid - 1;
			}
			
			mid = (long) (left + right) / 2;
		}
		
		if(cmp > 0 && wordList.size() - mid > 11){
			return wordList.subList((int)mid + 1, (int)mid+11);
		} else if ( cmp > 0 && wordList.size() - mid > 11 ){
			return wordList.subList((int)mid + 1, (int) (wordList.size() - mid));
		} else if (wordList.size() - mid > 11){
			return wordList.subList((int)mid, (int)mid+10);
		} else {
			return wordList.subList((int)mid, (int) (wordList.size() - mid));
		}
	}

	public void SetWordList(long start, long end){
		if(!wordList.isEmpty())
			wordList.clear();
		
		long endMore = 0;
		if(idxData.length - end < 1000){
			endMore = idxData.length - end;
		} else {
			endMore = end + 1000;
		}
		
		int startOffset = (int) start;
		int endOffset = (int) start;
		Word word = null;
		while(endOffset < endMore){
			word = new Word();
			startOffset = endOffset;
			while(idxData[endOffset] != '\0'){
				endOffset++;
			}
			try {
				word.setStrWord( new String(idxData, startOffset, endOffset - startOffset, "UTF8"));
				endOffset ++;
				word.setDataOffsetWord( readLong(idxData, endOffset));
				endOffset += 4;
				word.setDataSizeWord( readLong(idxData, endOffset));
				endOffset += 4;
				wordList.add(word);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (EOFException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Word lookUp(String word){
		if(!wordList.isEmpty()){
			int l = word.toLowerCase().compareTo(wordList.get(0).getStrWord().toLowerCase());
			int r = word.toLowerCase().compareTo(wordList.get(wordList.size()-1).getStrWord().toLowerCase());
			if(l < 0 || r > 0){
				wordList.clear();
				SearchUdt(word);
			}
		}
//		if(!wordList.isEmpty())
//			wordList.clear();
//		SearchUdt(word);
		
		
		long left = 0;
		long right = wordList.size() - 1;
		long mid = (long) (left + right) / 2;
		String lWord = word.toLowerCase();
		while( right >= left){
			int cmp = lWord.compareTo(wordList.get((int) mid).getStrWord().toLowerCase());
			
			if( cmp == 0){ System.out.println(mid);
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
	
	private void createIndex(String fileName){
				
		FileOutputStream fos = null;
		DataInputStream dis = null;
		try {
			fos = new FileOutputStream(udtPath);
			dis = new DataInputStream(
					new BufferedInputStream(
							new FileInputStream(fileName)));
			byte[] b = new byte[(int) getFileSize()];
			dis.read(b);
			dis.close();
			int startOffset = 0;
			int endOffset = 0;
			for(long l = 0; l < getWordCount(); l++){
				startOffset = endOffset;
				while(b[endOffset] != '\0'){
					endOffset++;
				}
				if(l % udtIdxNum == 0){
					fos.write(b, startOffset, endOffset - startOffset);
					fos.write('\0');
					fos.write(writeInt(startOffset));
					fos.flush();
				}
				endOffset += 9;
			}
			fos.close();
			setAvailable(true);
			System.out.println("Store \".udt\" file successful!");
			
		} catch (FileNotFoundException e) {
			System.err.println("ERROR: Storing \".udt\" " + e);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("ERROR: Storing \".udt\" " + e);
			e.printStackTrace();
		} finally {
			if(fos != null)
				try {
					fos.close();
				} catch (IOException e) {
					System.err.println("ERROR: Closing FileOutputStream");
				}
			if(dis != null)
				try {
					dis.close();
				} catch (IOException e) {
					System.err.println("ERROR: Closing DataInputStream");
				}
		}
	}

	private long readLong(byte[] b, int offset) throws EOFException {
		int ch1 = 0xFF & (int)b[offset];
		int ch2 = 0xFF & (int)b[offset + 1];
		int ch3 = 0xFF & (int)b[offset + 2];
		int ch4 = 0xFF & (int)b[offset + 3];
		if((ch1 | ch2 | ch3 | ch4) < 0)
			throw new EOFException();
		return (long) ( (ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0)) & 0xFFFFFFFF;
	}
	
	private byte[] writeInt(int size) throws EOFException {
		byte[] b = new byte[4];
		b[0] = (byte) (size >> 24);
		b[1] = (byte) (size >> 16);
		b[2] = (byte) (size >> 8);
		b[3] = (byte) (size);
//		if((b[0] | b[1] | b[2] | b[3]) < 0)
//			throw new EOFException();
		return b;
	}
	
	private String encodeUTF8(String str){
		try {
			return new String(str.getBytes("UTF-8"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
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
	
	public int getUdtIdxNum(){
		return udtIdxNum;
	}
	
	public void setUdtIdxNum(){
		udtIdxNum = 0;
		while(udtIdxNum * udtIdxNum < getWordCount()){
			udtIdxNum++;
		}
		
	}

	public void setUdtIdxList() {
		File udtFile = new File(udtPath);
		try {
			DataInputStream dis = new DataInputStream(
					new FileInputStream(udtFile));
			byte[] b = new byte[(int) udtFile.length()];
			dis.readFully(b);
			dis.close();
			
			udtIdxList = new ArrayList<UdtIdx>();
			UdtIdx aIdx = null;
			int offStar = 0;
			int offEnd = 0;
			long size = udtFile.length();
			while(offEnd < size){
				offStar = offEnd;
				while(b[offEnd] != '\0'){
					offEnd++;
				}
				
				aIdx = new UdtIdx();
				aIdx.setWord(new String(b, offStar, offEnd - offStar));
				offEnd ++;
				aIdx.setOffset(readLong(b, offEnd));
				offEnd += 4;
				udtIdxList.add(aIdx);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
