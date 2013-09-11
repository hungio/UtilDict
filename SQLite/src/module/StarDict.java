package module;

import java.io.File;
import java.util.ArrayList;

public class StarDict {
	private IfoFile ifo = null;
	private IdxFile idx = null;
	private DictFile dict = null;
	
	private static final String EXT_DICT = ".dict";
    private static final String EXT_INDEX = ".idx";
    private static final String EXT_INFO = ".ifo";
    
    private String urlPath;
    private String ifoPath = null;
    private String idxPath = null;
    private String dictPath = null;
    
    private boolean isAvailable = false;
    
    public StarDict(String urlPath) {
		setUrlPath(urlPath);
		File file = new File(urlPath);
		
		if(file.isDirectory()){
			String []list = file.list();
			
			for(int i = 0; i < list.length; i++){				
				if(list[i].endsWith(EXT_INFO) && ifoPath == null){
					ifoPath = urlPath + File.separator + list[i];
				} else if ( list[i].endsWith(EXT_INDEX ) && idxPath == null){
					idxPath = urlPath + File.separator + list[i];
				} else if( list[i].endsWith(EXT_DICT)  && dictPath == null) {
					dictPath = urlPath + File.separator + list[i];
				} else if (ifoPath != null || idxPath != null || dictPath != null){
					System.out.println("Warning: This dict has conflic dict file!");
				}				
			} 
		} else {
			ifoPath = urlPath + EXT_INFO;
			idxPath = urlPath + EXT_INDEX;
			dictPath = urlPath + EXT_DICT;
		}
		
		if(ifoPath == null || idxPath == null || dictPath == null){
			System.out.println("ERROR: This URL: " + urlPath +" missing dict file!");
			return;
		}
		
		ifo = new IfoFile(ifoPath);
		if(ifo.isAvailable()){
			idx = new IdxFile(idxPath, ifo.getWordcount()	, ifo.getIdxfileseze());
		}
		if(idx.isAvailable()){
			dict = new DictFile(dictPath);
		}
		setAvailable(true);
	}

    public String lookUp(String word){
    		return dict.getWordMeaning( idx.lookUp(word));
    }
    
    public String getVersion(){
    		return ifo.getVersion();
    }
    public String getBookname(){
    		return ifo.getBookname();
    }
    public long getWordcount(){
    		return ifo.getWordcount();
    }
    public long getSynwordcount(){
    		return ifo.getSynwordcount();
    }
    public long getIdxfilesize() {
    		return ifo.getIdxfileseze();
    }
    public long getIdxoffsetbits() {
    		return ifo.getIdxoffsetbits();
    }
    public String getAuthor() {
    		return ifo.getAuthor();
    }
    public String getEmail() {
    		return ifo.getEmail();
    }
    public String getWebsite() {
    		return ifo.getWebsite();
    }
    public String getDescription() {
    		return ifo.getDescripton();
    }
    public String getDate() {
    		return ifo.getDate();
    }
    
	public String getUrlPath() {
		return urlPath;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	
	public ArrayList<Word> getWordList(){
		return idx.getWordlist();
	}
}
