package com.twh.simplenote;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class NoteEditActivity extends Activity{
	
	private static final int EXPORT_ID = Menu.FIRST;
	private static final int SAVE_ID = Menu.FIRST + 1;
	private static final int DELETED_ID = Menu.FIRST + 2;
	
	private static String DIR_NAME = "simpleNote";
	private static String SDCARD = "/sdcard/";
	
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
		menu.add(0, DELETED_ID, 0, R.string.menu_delete);
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
		case DELETED_ID:
			deleteNote();
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
		createFile(mTitleText.getText().toString(), mTypeSpinner.getSelectedItem().toString(), mContentText.getText().toString(), DatabaseHelper.currentTime());
	}
	
	private void deleteNote() {
		deleteNoteDialog();
	}
	
	public void deleteNoteDialog(){
	 	   new AlertDialog.Builder(NoteEditActivity.this)
	 	   .setTitle("警告！")
	 	   .setMessage("确定要删除当前记录？")
	 	   .setPositiveButton(R.string.ok_label, 
	 			   new DialogInterface.OnClickListener() {
	 				@Override
	 				public void onClick(DialogInterface dialog, int which) {
	 					if (mRowId!=null){
	 						mDbHelper.deleteNote(mRowId);
	 					}
	 					Intent mIntent=new Intent();
	 					setResult(RESULT_OK,mIntent);
	 					finish();
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
	
	//新建文件夹
	public static boolean newFolder(String file)
	{
		File dirFile = new File( SDCARD +file);
		try
		{
			if (!(dirFile.exists()) && !(dirFile.isDirectory()))
			{
				boolean creadok = dirFile.mkdirs();
				if (creadok)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e);
			return false;
		}
		return true;
	}

	//删除文件
	public boolean deleteFile(File file)
	{
		boolean result = false;
		if (file != null)
		{
			try
			{
				File file2 = file;
				file2.delete();
				result = true;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				result = false;
			}
		}
		return result;
	} 

	public void createFile(String title, String type, String content, String time) 
	{
		String filename = createFileName();
		if(newFolder(DIR_NAME)){
			File file = new File(filename);
			if (!file.exists()) {
				try {
					//在指定的文件夹中创建文件
					save(filename, title, type, content, time);
				} catch (Exception e) {
				}
			}
		}
	}

	private static String createFileName()
	{
		String fileName = "导出结果" + DatabaseHelper.currentTimeNumber();
		return fileName;
	}

	public void save(String name, String title, String type, String content, String time)
	{
		try {
			String text =  "标题：" + title + "\n" + "修改时间：" + time + "\n" + "分类：" + type + "\n" + "内容：" + content + "\n";
			File sdCardDir = Environment.getExternalStorageDirectory();//获取SDCard目录
			File saveFile = new File(sdCardDir+"/simpleNote/", name +".txt");
			FileOutputStream outStream = new FileOutputStream(saveFile);
			outStream.write(text.getBytes());
			outStream.close();
			Toast.makeText(NoteEditActivity.this,"Saved",Toast.LENGTH_LONG).show();
		} catch (FileNotFoundException e) {
			return;
		}
		catch (IOException e){
			return ;
		}
	}

}
