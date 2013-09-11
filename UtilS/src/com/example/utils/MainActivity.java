package com.example.utils;

import module.StarDict;
import module.Word;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.app.Activity;

public class MainActivity extends Activity {
	StarDict starDict;
	
	EditText search;
	TextView result;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		search = (EditText) findViewById(R.id.keysearch);
		result = (TextView) findViewById(R.id.result);
	}
	
	public void loaddict(View view){
		long start = System.nanoTime();
		starDict = new StarDict(Environment.getExternalStorageDirectory().getPath() + 
				"/stardict/stardict-dictd_anh-viet-2.4.2", true);
		Log.d("DICT", Long.toString(System.nanoTime() - start));
	}
	
	public void searchbegin(View view){
		long start = System.nanoTime();
		String key = search.getText().toString();
		Word word = starDict.Find(key);
		Log.i("DICT", Long.toString(System.nanoTime() - start));
		
		result.setText( "{" +
				Long.toString(word.getDataOffsetWord())
				+ ", " + Long.toString(word.getDataSizeWord()) + "}");
	}

}
