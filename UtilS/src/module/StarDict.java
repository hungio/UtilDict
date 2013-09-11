package module;

import java.io.File;
import java.util.List;

import util.Constant;

public class StarDict implements UtilDict {
	private String dictKind = "Star dict";
	private IfoFile ifo = null;
	private IdxFile idx = null;
	private DictFile dict = null;
    
    private String urlPath;
    private String ifoPath = null;
    private String idxPath = null;
    private String dictPath = null;
    
    private boolean isAvailable = false;
    
    public StarDict(String urlPath, boolean loadIdx) {
		setUrlPath(urlPath);
		File file = new File(urlPath);
		
		if(file.isDirectory()){
			String []list = file.list();
			
			for(int i = 0; i < list.length; i++){
				if(!list[i].endsWith(Constant.EXT_INFO) 
						&& !list[i].endsWith(Constant.EXT_INDEX)  
						&& !list[i].endsWith(Constant.EXT_DICT)){
					continue;
				}
				
				if(list[i].endsWith(Constant.EXT_INFO) && ifoPath == null){
					ifoPath = urlPath + File.separator + list[i];
				} else if ( list[i].endsWith(Constant.EXT_INDEX ) && idxPath == null){
					idxPath = urlPath + File.separator + list[i];
				} else if( list[i].endsWith(Constant.EXT_DICT)  && dictPath == null) {
					dictPath = urlPath + File.separator + list[i];
				} else if (ifoPath != null || idxPath != null || dictPath != null){
					System.out.println("Warning: This dict has conflic dict file!");
				}				
			} 
		} else {
			ifoPath = urlPath + Constant.EXT_INFO;
			idxPath = urlPath + Constant.EXT_INDEX;
			dictPath = urlPath + Constant.EXT_DICT;
		}
		
		if(ifoPath == null || idxPath == null || dictPath == null){
			System.out.println("ERROR: This URL: " + urlPath +" missing dict file!");
			return;
		}
		
		ifo = new IfoFile(ifoPath);
		if(!loadIdx)
			return;
		if(ifo.isAvailable()){
			idx = new IdxFile(idxPath, ifo.getWordcount()	, ifo.getIdxfileseze());
		}
		if(ifo.isAvailable() && idx.isAvailable()){
			dict = new DictFile(dictPath);
		} else {
			return;
		}
		setAvailable(true);
	}
    
    

    public String lookUp(String word){
    		if(!isAvailable()){
    			return "This path \"" + getUrlPath() + "\" is not available\nNo dict found!";
    		}
    		word = normalize(word);
    		return dict.getWordMeaning( idx.lookUp(word));
    }
    
    public List <Word> Search(String word){
    		if(!isAvailable())
    			return null;
    		word = normalize(word);
    		return idx.SearchUdt(word);
    }
    
    public Word Find(String word){
    	if(!isAvailable()){
    		System.out.println("Dict is not available");
			return null;
		}
		word = normalize(word);
		
		return idx.lookUp(word);
    }
    
    private String normalize(String word) {
		word = word.replaceAll("\\s+", " ");
		word = word.trim();
		return word;
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
	
	@Override
	public String getDictKind() {
		return dictKind;
	}

	@Override
	public String getDictName() {
		return getBookname();
	}

}
