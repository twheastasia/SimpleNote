package com.twh.simplenote;


import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainListActivity extends ListActivity {

	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private static final int INSERT_ID = Menu.FIRST;
//	private static final int DELETE_ID = Menu.FIRST + 1;
	private static final int EXPORT_ALL_ID = Menu.FIRST + 1;
	
	private DatabaseHelper mDbHelper;
	private Cursor mNoteCursor;
	private long currentId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
	    mDbHelper = new DatabaseHelper(this);
        mDbHelper.open();
        renderListView();
        
        getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int pos, long id) {
				// TODO Auto-generated method stub
				currentId = pos;
				deleteDialog();
				return false;
			}
        	
        	
		});
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
		menu.add(0, EXPORT_ALL_ID, 0, R.string.menu_export);
		
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
			case EXPORT_ALL_ID:
				exportAllNotes();
				return true;
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
		SimpleCursorAdapter notes = new SimpleCursorAdapter(this, R.layout.diary_row, mNoteCursor, from, to);
//		SimpleCursorAdapter notes=new SimpleCursorAdapter(this,android.R.layout.two_line_list_item,mNoteCursor,from,to);
		setListAdapter(notes);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Cursor c = mNoteCursor;
		c.moveToPosition(position);
		Intent i = new Intent(this, NoteEditActivity.class);
		i.putExtra(DatabaseHelper.KEY_ROWID, id);
		i.putExtra(DatabaseHelper.KEY_TITLE, c.getString(c.getColumnIndexOrThrow(DatabaseHelper.KEY_TITLE)));
		i.putExtra(DatabaseHelper.KEY_BODY, c.getString(c.getColumnIndexOrThrow(DatabaseHelper.KEY_BODY)));
		i.putExtra(DatabaseHelper.KEY_TYPE, c.getString(c.getColumnIndexOrThrow(DatabaseHelper.KEY_TYPE)));
		startActivityForResult(i, ACTIVITY_EDIT);
	}
	
	public void deleteDialog(){
	 	   new AlertDialog.Builder(MainListActivity.this)
	 	   .setTitle("警告！")
	 	   .setMessage("确定要删除当前记录？")
	 	   .setPositiveButton(R.string.ok_label, 
	 			   new DialogInterface.OnClickListener() {
	 				@Override
	 				public void onClick(DialogInterface dialog, int which) {
	 					if (currentId > -1){
	 						mDbHelper.deleteNote(currentId);
	 					}
	 					renderListView();
	 				}
	 			})
	 		.setNegativeButton(R.string.back_label, 
	 			   new DialogInterface.OnClickListener() {
	 				@Override
	 				public void onClick(DialogInterface dialog, int which) {
	 				}
	 			})
	 	   .show();
	}
	
	
	private void exportAllNotes() {
		String allStr = "";
		String titleStr;
		String timeStr;
		String typeStr;
		String contentStr;
		Cursor c = mNoteCursor;
		c.moveToFirst();
		do{
			titleStr = "";
			timeStr = "";
			typeStr = "";
			contentStr = "";
			titleStr = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.KEY_TITLE));
			timeStr = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.KEY_UPDATED));
			typeStr = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.KEY_TYPE));
			contentStr = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.KEY_BODY));
			allStr  +=  "标题：" + titleStr + "\n" + "修改时间：" + timeStr + "\n" + "分类：" + typeStr + "\n" + "内容：" + contentStr + "\n\n";
		}while(c.moveToNext());
		NoteEditActivity.createFile(allStr);
		Toast.makeText(MainListActivity.this,"Saved all notes in /sdcard/simpleNote/",Toast.LENGTH_LONG).show();
	}
	
}
