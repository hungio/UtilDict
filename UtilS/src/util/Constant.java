package util;

import java.io.File;

public class Constant {
	public static final int RESULT_FRESH = 1001;
	
	public static final String EXT_DICT = ".dict";
    public static final String EXT_INDEX = ".idx";
    public static final String EXT_INFO = ".ifo";
    public static final String EXT_UDT = ".udt";
    
    public static String DictData = "dictdata.xml"; 
    public static String DictIntStor = "dict.cof";
    public static String kindStardict = "StarDict";
    public static String kindFavorite = "FAVORITES";
    public static String kindRecent = "RECENT";
    public static String kindAddNew = "ADD NEW";
    public static String dictPreferences = "dictPreferences";
    public static String defaultDictKind = "defaultDictKind";
    public static String defaultDictPath = "defaultDictPath";
    
    public static String DICT_KIND_INTENT = "name of dict";
    public static String DICT_PATH_INTENT = "path of dict";
    
    public static File ROOT_FILE = new File("mnt/sdcard");
    
    public static float fontSize = 14;
}
