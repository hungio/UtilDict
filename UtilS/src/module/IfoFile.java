package module;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class IfoFile {
	private final String	starDictIfo = "StarDict's dict ifo file";
	private String 	version = "";								// Must be 2.4.2 or 3.0.0.
	private String	bookname = "";							// Name of dictionary.
	private long		wordcount = 0;							// Count of word entries in ".idx" file, must be right.
	private long		synwordcount = 0;					// If ".syn" file exists.
	private long		idxfilesize = 0;							// Size (in bytes) of the ".idx" file.
	private long		idxoffsetbits = 0;						// New in ver 3.0.0.
	private String	author = "";
	private String	email = "";
	private String	website = "";
	private String	description = "";						// You can use <br> for new line.
	private String	date = "";
	private char		sametypesequence = '\0';			// Word's data in the .dict file have the this same sequence of datatypes.
	
	private boolean	isAvailable =  false;				// True if load .ifo file successful.
	
	/**************************/
	/*  Assessor of this class  */
	/*--------------------*/
	public void setVersion(String version) { this.version = version; }
	public String getVersion() { return this.version; }
	
	public void setBookname(String bookname) { this.bookname = bookname; }
	public String getBookname() { return this.bookname; }
	
	public void setWordcount( long wordcount) { this.wordcount = wordcount; }
	public long getWordcount() { return this.wordcount; }
	
	public void setSynwordcount( long synwordcount ) { this.synwordcount = synwordcount; }
	public long getSynwordcount() { return this.synwordcount; }
	
	public void setIdxfilesize(long idxfilesize) { this.idxfilesize = idxfilesize; }
	public long getIdxfileseze() { return this.idxfilesize; }
	
	public void setIdxoffsetbits( long idxoffsetbits) { this.idxoffsetbits = idxoffsetbits; }
	public long getIdxoffsetbits() { return this.idxoffsetbits; }
	
	public void setAuthor (String author) { this.author = author; }
	public String getAuthor () { return this.author; }
	
	public void setEmail (String email) { this.email = email; }
	public String getEmail () { return this.email; }
	
	public void setWebsite (String website) { this.website = website; }
	public String getWebsite () { return this.website; }
	
	public void setDescription(String description) { this.description = description; }
	public String getDescripton () { return this.description; }
	
	public void setDate( String date) { this.date = date; }
	public String getDate() { return this.date; }
	
	public void setSametypesequence( char sametypesequence) { this.sametypesequence = sametypesequence; }
	public char getSametypesequence() { return this.sametypesequence; }
	
	public boolean isAvailable() { return this.isAvailable; }
	
	
	/* Constructor */
	public IfoFile(String infoFilePath){
		try {
			BufferedReader bfr = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(
									new File(infoFilePath)), "UTF8")); 
			
			String aline = bfr.readLine();
			if(aline.compareTo(starDictIfo) != 0 ){
				System.out.println("This is not of Stardict ifo file");
				bfr.close();
				return;
			}
			while ((aline = bfr.readLine()) != null) {
				String parser[] = aline.split("=", 2);
				
				if(parser[0].compareTo("version") == 0){
					setVersion(parser[1]);
				}else if (parser[0].compareTo("bookname") == 0){
					setBookname(parser[1]);
				} else if (parser[0].compareTo("wordcount") == 0){
					setWordcount(Long.parseLong(parser[1], 10));
				} else if (parser[0].compareTo("idxfilesize") == 0){
					setIdxfilesize(Long.parseLong(parser[1], 10));
				} else if (parser[0].compareTo("idxoffsetbits") == 0){
					setIdxoffsetbits(Long.parseLong(parser[1], 10));
				} else if (parser[0].compareTo("author") == 0){
					setAuthor( parser[1]);
				}  else if (parser[0].compareTo("email") == 0){
					setEmail(parser[1]);
				} else if (parser[0].compareTo("website") == 0){
					setWebsite(parser[1]);
				}  else if (parser[0].compareTo("description") == 0){
					setDescription(parser[1]);
				} else if (parser[0].compareTo("date") == 0){
					setDate(parser[1]);
				} else if (parser[0].compareTo("sametypesequence") == 0){
					setSametypesequence(parser[1].charAt(0));
				} else {
					System.out.printf("ERROR: The option \"%s\" don't available in \".ifo\" file!", parser[1]);
					return;
				}
								
			}
			
			bfr.close();	
			if(getWordcount() > 0 && getIdxfileseze() > 0){
				isAvailable = true;
				System.out.println("Loading \".ifo\" file successful!");
			} else {
				System.out.println("ERROR: Invalid value of dict in \".ifo\" file!");
				return;
			}
		} catch (Exception e) {
			System.out.println("ERROR: Loading .ifo file " + e);
		}
		
	}
}
