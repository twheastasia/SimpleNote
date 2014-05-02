package com.twh.simplenote;


import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;

public class MainListActivity extends ListActivity {

	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private static final int INSERT_ID = Menu.FIRST;
//	private static final int DELETE_ID = Menu.FIRST + 1;
	
	private DatabaseHelper mDbHelper;
	private Cursor mNoteCursor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
	    mDbHelper = new DatabaseHelper(this);
        mDbHelper.open();
        renderListView();
	}

    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		renderListView();
	}
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		super.onCreateOptionsMenu(menu);
		menu.add(0, INSERT_ID, 0, R.string.menu_insert);
//		menu.add(0, DELETE_ID, 0, R.string.menu_delete);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
			case INSERT_ID:
				createNote();
				return true;
			/*case DELETE_ID:
				mDbHelper.deleteDiary(getListView().getSelectedItemId());
				renderListView();
				return true;*/
			default:
				break;
			
		}
		
		return super.onMenuItemSelected(featureId, item);
		
	}
	
	private void createNote() {
		Intent i = new Intent(this,NoteEditActivity.class);
		startActivityForResult(i,ACTIVITY_CREATE);
	}
	
	private void renderListView() {
		// TODO Auto-generated method stub
		mNoteCursor = mDbHelper.getAllNotes();
		startManagingCursor(mNoteCursor);
		String[] from = new String[] { DatabaseHelper.KEY_TITLE,
				DatabaseHelper.KEY_TYPE, DatabaseHelper.KEY_UPDATED };
		int[] to = new int[] { R.id.text1, R.id.type, R.id.created };
		SimpleCursorAdapter notes=new SimpleCursorAdapter(this, R.layout.diary_row, mNoteCursor, from, to);
//		SimpleCursorAdapter notes=new SimpleCursorAdapter(this,android.R.layout.two_line_list_item,mNoteCursor,from,to);
		setListAdapter(notes);
	}
}
