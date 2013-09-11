package util;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlParser {
	private String conf = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
	
    public static class Entry {
        public final String dictKind;
        public String dictName;
        public final String dictPath;

        public Entry(String kind, String name, String path) {
            this.dictKind = kind;
            this.dictName = name;
            this.dictPath = path;
        }
    }

    public List<Entry> parse(InputStream in) {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(in, null);
            parser.nextTag();
            return readDict(parser);
        } catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
            try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
		return null;
    }
    
   
	public String getStringfromDict(List<Entry> allDict) {
		conf += "<data>\n";
		for(Entry entry : allDict){
			conf += "\t<dict>\n";
			conf += "\t\t<kind>" + entry.dictKind + "</kind>\n";
			conf += "\t\t<name>" + entry.dictName + "</name>\n";
			conf += "\t\t<path>" + entry.dictPath + "</path>\n";
			conf += "\t</dict>\n";
		}
		conf += "</data>\n";
		return conf;
	}

    private List<Entry> readDict(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Entry> entries = new ArrayList<Entry>();

        parser.require(XmlPullParser.START_TAG, null, "data");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            if (name.equals("dict")) {
                entries.add(readEntry(parser));
            } else {
            		Log.e("XML PARSER", "invalid dict");
                return null;
            }
        }
        return entries;
    }

    private Entry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "dict");
        String dictKind = null;
        String dictName = null;
        String dictPath = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("kind")) {
                dictKind = readTitle(parser, "kind");
            } else if (name.equals("name")) {
                dictName = readTitle(parser, "name");
            } else if (name.equals("path")) {
                dictPath = readTitle(parser, "path");
            } else {
                Log.e("XML PARSER", "invalid entry");
                return null;
            }
        }
        return new Entry(dictKind, dictName, dictPath);
    }

    private String readTitle(XmlPullParser parser, String title) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, title);
        String gTitle = parser.nextText();
        if(parser.getEventType() != XmlPullParser.END_TAG){
        		parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, null, title);
        return gTitle;
    }
}
