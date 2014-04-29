package com.twh.simplenote;

import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper {

	public static final String KEY_TITLE = "title";
	public static final String KEY_BODY = "body";
	public static final String KEY_ROWID = "_id";
	public static final String KEY_CREATED = "created_at";
	public static final String KEY_UPDATED = "updated_at";
	public static final String KEY_TYPE = "type";
	
	private Notedatabase mDbHelper;
	public static String DATABASE_NAME="notedatabase";
	public static String DATABASE_TABLE="notes";
	public static int version=1;
	public static String DATABASE_CREATE= "create table notes (_id integer primary key autoincrement, "
		+ "title text not null, body text not null, type text not null, created_at text not null, updated_at text not null);";
	private SQLiteDatabase mDb;
	private final Context mCtx;
	

	private class Notedatabase extends SQLiteOpenHelper{//Ë½ÓÐÀà
		public Notedatabase(Context context) {
			super(context, DATABASE_NAME, null, version);
			// TODO Auto-generated constructor stub
		}
		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS notes");
			onCreate(db);
		}
	}
		
	public DatabaseHelper(Context ctx) {
		this.mCtx = ctx;
	}

	public DatabaseHelper open() throws SQLException {
		mDbHelper = new Notedatabase(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void closeclose() {
		mDbHelper.close();
	}
	
	//insert
	public long createNote(String title,String body, String type){
		ContentValues initvalues = new ContentValues();
		Calendar calendar = Calendar.getInstance();
		initvalues.put(KEY_TITLE, title);
		initvalues.put(KEY_BODY, body);
		initvalues.put(KEY_TYPE, type);
		String createdtime = calendar.get(Calendar.YEAR)+"-"+calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.DAY_OF_MONTH)+" "
			+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+":" + calendar.get(Calendar.SECOND);
		initvalues.put(KEY_CREATED, createdtime);
		initvalues.put(KEY_UPDATED, createdtime);
		return mDb.insert(DATABASE_TABLE,null,initvalues);
		
	}
	
	//update
	public boolean updateNote(long rowid,String title,String body, String type){
		ContentValues initvalues = new ContentValues();
		Calendar calendar = Calendar.getInstance();
		initvalues.put(KEY_TITLE, title);
		initvalues.put(KEY_BODY, body);
		initvalues.put(KEY_TYPE, type);
		String createdtime = calendar.get(Calendar.YEAR)+"-"+calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.DAY_OF_MONTH)+" "
			+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+ ":" + calendar.get(Calendar.SECOND);
		initvalues.put(KEY_UPDATED, createdtime);
		return mDb.update(DATABASE_TABLE, initvalues, KEY_ROWID + "=" + rowid, null)>0;
	}
	
	//delete
	public boolean deleteNote(long rowId) {

		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	//get all notes
	public Cursor getAllNotes() {

		return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_TITLE,
				KEY_BODY, KEY_TYPE, KEY_CREATED, KEY_UPDATED }, null, null, null, null, null);
	}

	public Cursor getNote(long rowId) throws SQLException {

		Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[] { KEY_ROWID, KEY_TITLE,
				KEY_BODY, KEY_TYPE, KEY_CREATED, KEY_UPDATED }, KEY_ROWID + "=" + rowId, null, null,
				null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

}
