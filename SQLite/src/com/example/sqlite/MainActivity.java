package com.example.sqlite;

import java.util.HashMap;

import module.StarDict;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.app.Activity;

public class MainActivity extends Activity {
	EditText key;
	TextView result;
	Button button;
	
	SQLite sqLite;
	StarDict starDict;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		key = (EditText) findViewById(R.id.keysearch);
		result = (TextView) findViewById(R.id.result);
		button = (Button) findViewById(R.id.search);
		
		Long start = System.nanoTime();
		sqLite = new SQLite(getApplicationContext());
		Log.d("DICT", Long.toString(System.nanoTime() - start));
		
	}
	
	public void searchbegin(View view){
		long start = System.nanoTime();
		HashMap<String, Long> str = sqLite.search(key.getText().toString());
		Log.i("DICT", Long.toString(System.nanoTime() - start));
		result.setText(str.toString());
	}
	
	public void storedb(View view){
		long start = System.nanoTime();
		starDict = new StarDict(Environment.getExternalStorageDirectory().getPath() + 
				"/stardict/stardict-dictd_anh-viet-2.4.2");
		long second = System.nanoTime();
		
		if(starDict.isAvailable()){
			sqLite.storeWord(starDict.getWordList());
		} else {
			Log.e("DICT", "Path url not available");
			button.setVisibility(View.INVISIBLE);
		}
		
		long store = System.nanoTime() - second;
		Log.d("DICT", Long.toString(second - start));
		Log.d("DICT",  Long.toString(store));
		
		if(starDict != null){
			starDict = null;
		}
	}

}
