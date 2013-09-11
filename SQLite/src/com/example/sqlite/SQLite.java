package com.example.sqlite;

import java.util.ArrayList;
import java.util.HashMap;

import module.Word;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLite extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "utildict";
	
	private static final String TABLE_DICT = "users";
	private static final String ID = "id";
	private static final String WORD = "word";
	private static final String OFFSET = "offset";
	private static final String SIZE = "size";
	
	
	public SQLite(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_IDX_DICT = "CREATE TABLE IF NOT EXISTS " + TABLE_DICT + " ("
//				+ ID + " INTEGER PRIMARY KEY, "
				+ WORD + " TEXT PRIMARY KEY, "
				+ OFFSET + " INTEGER, "
				+ SIZE + " INTEGER) ";
		db.execSQL(CREATE_IDX_DICT);
//		db.execSQL("CREATE INDEX IF NOT EXISTS utilindex on " + TABLE_DICT + " (" + WORD + ")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DICT);		
	}

	public void storeWord(String word, long offset, long size){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_DICT, null);
		if(cursor != null){
			cursor.moveToFirst();
			if(cursor.getInt(0) > 0){
				db.close();
				return;
			}
		}
		
		db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(WORD, word);
		values.put(OFFSET, offset);
		values.put(SIZE, size);
		
		db.insert(TABLE_DICT, null, values);
		db.close();
	}
	
	public void storeWord(ArrayList<Word> words){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_DICT, null);
		if(cursor != null){
			cursor.moveToFirst();
			if(cursor.getInt(0) > 0){
				db.close();
				return;
			}
		}
		
		db = this.getWritableDatabase();
		try{
			db.beginTransaction();
			int count = 0;
			for (Word word : words) {
//				if (count++ > 1000) {
//					break;
//				}
				ContentValues value = new ContentValues();
				value.put(WORD, word.getStrWord());
				value.put(OFFSET, word.getDataOffsetWord());
				value.put(SIZE, word.getDataSizeWord());
				db.insert(TABLE_DICT, null, value);
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		db.close();
	}
	
	public HashMap<String, Long> search(String word){
		HashMap<String, Long>result = new HashMap<String, Long>();
		
		String  query = "SELECT " + OFFSET + ", " + SIZE
						+ " FROM " + TABLE_DICT
						+ " WHERE " + WORD + " = '" + word +"'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		cursor.moveToFirst();
		if(cursor.getCount() > 0){
			result.put(OFFSET, cursor.getLong(0));
			result.put(SIZE, cursor.getLong(1));
		}
		cursor.close();
		db.close();
		
		return result;
	}
	
	public void dropTable(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_DICT, null, null);
		db.close();
	}
}
