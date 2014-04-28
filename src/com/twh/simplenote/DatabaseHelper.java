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
	public static final String KEY_CREATED = "created";

	private Diarydatabase mDbHelper;
	public static String DATABASE_NAME="dairydatabase";
	public static String DATABASE_TABLE="diary";
	public static int version=1;
	public static String DATABASE_CREATE= "create table diary (_id integer primary key autoincrement, "
		+ "title text not null, body text not null, created text not null);";
	private SQLiteDatabase mDb;
	private final Context mCtx;
	

	private class Diarydatabase extends SQLiteOpenHelper{//私有类
		public Diarydatabase(Context context) {
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
			db.execSQL("DROP TABLE IF EXISTS diary");
			onCreate(db);
		}
	}
		
	public DatabaseHelper(Context ctx) {
		this.mCtx = ctx;
	}

	public DatabaseHelper open() throws SQLException {
		mDbHelper = new Diarydatabase(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void closeclose() {
		mDbHelper.close();
	}
	
	
	public long creatediary(String title,String body){
		ContentValues initvalues=new ContentValues();
		Calendar calendar=Calendar.getInstance();
		initvalues.put(KEY_TITLE, title);
		initvalues.put(KEY_BODY, body);
		String createdtime=calendar.get(Calendar.YEAR)+"年"+calendar.get(Calendar.MONTH)+"月"+calendar.get(Calendar.DAY_OF_MONTH)+"日"
			+calendar.get(Calendar.HOUR_OF_DAY)+"时"+calendar.get(Calendar.MINUTE)+"分";
		initvalues.put(KEY_CREATED, createdtime);
		return mDb.insert(DATABASE_TABLE,null,initvalues);
		
	}
	
	public boolean updatediary(long rowid,String title,String body){
		ContentValues initvalues=new ContentValues();
		Calendar calendar=Calendar.getInstance();
		initvalues.put(KEY_TITLE, title);
		initvalues.put(KEY_BODY, body);
		String createdtime=calendar.get(Calendar.YEAR)+"年"+calendar.get(Calendar.MONTH)+"月"+calendar.get(Calendar.DAY_OF_MONTH)+"日"
			+calendar.get(Calendar.HOUR_OF_DAY)+"时"+calendar.get(Calendar.MINUTE)+"分";
		initvalues.put(KEY_CREATED, createdtime);
		return mDb.update(DATABASE_TABLE,initvalues,KEY_ROWID+"="+rowid,null)>0;
	}
	
	public boolean deleteDiary(long rowId) {

		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	public Cursor getAllNotes() {

		return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_TITLE,
				KEY_BODY, KEY_CREATED }, null, null, null, null, null);
	}

	public Cursor getDiary(long rowId) throws SQLException {

		Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[] { KEY_ROWID, KEY_TITLE,
				KEY_BODY, KEY_CREATED }, KEY_ROWID + "=" + rowId, null, null,
				null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

}
