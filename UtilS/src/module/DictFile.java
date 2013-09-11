package module;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

@SuppressWarnings("serial")
public class DictFile implements Serializable {
	private String dictFile;

	public void setDictFile(String dictFile) {
		this.dictFile = dictFile;
	}
	
	/* Constructor */
	public DictFile( String dictFile) {
		setDictFile(dictFile);
	}
	
	public String getWordMeaning(Word word) {
		String wordMeaning = "Not found";
		if(word == null)
			return wordMeaning;
		
		DataInputStream dis = null;
		try {
			dis = new DataInputStream(
					new BufferedInputStream( 
							new FileInputStream(dictFile)));
			byte[] b = new byte[(int) word.getDataSizeWord()];
			dis.skip(word.getDataOffsetWord());
			dis.read(b, 0, (int) word.getDataSizeWord());
			wordMeaning = new String(b, "UTF8");
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: Reading meaning word in \".dict\" file!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("ERROR: Reading meaning word in \".dict\" file!");
			e.printStackTrace();
		} finally {
			if(dis != null){
				try {
					dis.close();
				} catch (IOException e) {
					System.out.println("ERROR: Close DataInputStrem in \".dict\" file");
					e.printStackTrace();
				}
			}
		}
		
		return wordMeaning;
	}
}
