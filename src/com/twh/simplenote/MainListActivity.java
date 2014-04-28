package com.twh.simplenote;


import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.widget.SimpleCursorAdapter;

public class MainListActivity extends ListActivity {

	private DatabaseHelper mDbHelper;
	private Cursor mDiaryCursor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
	    mDbHelper = new DatabaseHelper(this);
        mDbHelper.open();
        renderListView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list, menu);
		return true;
	}
	
	private void renderListView() {
		// TODO Auto-generated method stub
//		mDiaryCursor = mDbHelper.getAllNotes();
//		startManagingCursor(mDiaryCursor);
//		String[] from = new String[] { DatabaseHelper.KEY_TITLE,
//				DatabaseHelper.KEY_CREATED };
//		int[] to = new int[] { R.id.text1, R.id.created };
//		SimpleCursorAdapter notes=new SimpleCursorAdapter(this,R.layout.diary_row,mDiaryCursor,from,to);
		//SimpleCursorAdapter notes=new SimpleCursorAdapter(this,android.R.layout.two_line_list_item,mDiaryCursor,from,to);
//		setListAdapter(notes);
	}
}
