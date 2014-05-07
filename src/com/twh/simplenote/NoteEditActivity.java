package com.twh.simplenote;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class NoteEditActivity extends Activity{
	
	private static final int EXPORT_ID = Menu.FIRST;
	private static final int SAVE_ID = Menu.FIRST + 1;
	
	private DatabaseHelper mDbHelper;
	private EditText mTitleText;
	private EditText mContentText;
	private Spinner mTypeSpinner;
	private Long mRowId;
	private String mCurrentType;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_note);
        mDbHelper = new DatabaseHelper(this);
        mDbHelper.open();
        
        mTitleText = (EditText)findViewById(R.id.title_text);
        mContentText = (EditText)findViewById(R.id.content_text);
        mTypeSpinner = (Spinner)findViewById(R.id.type_spinner);
        
        // 建立数据源
        String[] mItems = getResources().getStringArray(R.array.type_spinner);
        //String[] mItems = getTypeString();
        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> _Adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
        //绑定 Adapter到控件
        mTypeSpinner.setAdapter(_Adapter);

        mTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				mCurrentType = parent.getItemAtPosition(position).toString();
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
        
        //如果有值就直接显示
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
        	String title = extras.getString(DatabaseHelper.KEY_TITLE);
        	String body = extras.getString(DatabaseHelper.KEY_BODY);
        	String type = extras.getString(DatabaseHelper.KEY_TYPE);
        	mRowId = extras.getLong(DatabaseHelper.KEY_ROWID);
        	if(title != null){
        		mTitleText.setText(title);
        	}
        	if(body != null){
        		mContentText.setText(body);
        	}
        	if(type != null){
        		mTypeSpinner.setSelection(getIndex(mItems, type), true);
        	}
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		menu.add(0, EXPORT_ID, 0, R.string.menu_export);
		menu.add(0, SAVE_ID, 0, R.string.menu_save);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case EXPORT_ID:
			exportNote();
			return true;
		case SAVE_ID:
			saveNote();
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void saveNote() {
		// TODO Auto-generated method stub
		String title=mTitleText.getText().toString();
		String body=mContentText.getText().toString();
		if (mRowId!=null){
			mDbHelper.updateNote(mRowId, title, body, mCurrentType);
		}else
			mDbHelper.createNote(title, body, mCurrentType);
		Intent mIntent=new Intent();
		setResult(RESULT_OK,mIntent);
		finish();
	}

	private void exportNote() {
		// TODO Auto-generated method stub
		
	}
	
	private String[] getTypeString() {
		String[] items = new String[40];
		items[0] = "未分类";
		items[1] = "日记";
		items[2] = "账单";
		items[3] = "心情";
		items[4] = "技术";
		return items;
	}

	private int getIndex(String[] items, String match){
		for(int index = 0; index < items.length; index++){
			if(match.equals(items[index])){
				return index;
			}
		}
		return 0;
	}
	
}
