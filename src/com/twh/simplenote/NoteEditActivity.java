package com.twh.simplenote;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import android.R.array;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NoteEditActivity extends Activity{
	
	private static final int EXPORT_ID = Menu.FIRST;
	private static final int SAVE_ID = Menu.FIRST + 1;
	private static final int DELETED_ID = Menu.FIRST + 2;
	
	private static String DIR_NAME = "simpleNote";
	private static String SDCARD = "/sdcard/";
	
	private DatabaseHelper mDbHelper;
	private Cursor mAllTypes;
	private EditText mTitleText;
	private EditText mContentText;
	private TextView mTypeText;
	private Long mRowId;
	private String mCurrentType = "δ����";
	private EditText mAddTypeET;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_note);
        mDbHelper = new DatabaseHelper(this);
        mDbHelper.open();
        
        mTitleText = (EditText)findViewById(R.id.title_text);
        mContentText = (EditText)findViewById(R.id.content_text);
        mTypeText = (TextView)findViewById(R.id.type_textview);
        mTypeText.setClickable(true);
        mTypeText.setFocusable(true);
        mAddTypeET = new EditText(this);
        
        //�����ֵ��ֱ����ʾ
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
        		mTypeText.setText(type);
        	}
        }
        
        //add listener on clicking type text
        mTypeText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showTypeDialog(); 
			}
		});
        
		if(mDbHelper.hasData("types")){
		}else{
			mDbHelper.insertType("����");
			mDbHelper.insertType("����");
			mDbHelper.insertType("����");
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

	private void showTypeDialog() {
        //choose type dialog
        final String[] arrayType = getTypeString(); 
        new AlertDialog.Builder(this)
        		.setTitle("��ѡ���ռǵķ���")
                .setIcon(R.drawable.ic_launcher) 
                .setSingleChoiceItems(arrayType, 0, new DialogInterface.OnClickListener() { 
                    @Override 
                    public void onClick(DialogInterface dialog, int which) { 
                    	mTypeText.setText(arrayType[which]);
                    	mCurrentType = arrayType[which];
                    } 
                })
                .setPositiveButton("��ӷ���", new DialogInterface.OnClickListener() { 

                    @Override 
                    public void onClick(DialogInterface dialog, int which) { 
                        // TODO Auto-generated method stub
                    	addTypeDialog();
                    } 
                })
                .setNegativeButton("ȷ��", new DialogInterface.OnClickListener() { 

                    @Override 
                    public void onClick(DialogInterface dialog, int which) { 
                        // TODO Auto-generated method stub
                    	
                    } 
                })
                .show(); 
	}
	
	private void addTypeDialog() {
		//add type dialog
        new AlertDialog.Builder(this)
		        .setTitle("�������������")
		        .setIcon(android.R.drawable.ic_dialog_info)
		        .setView(mAddTypeET)
		        .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() { 
		
		            @Override 
		            public void onClick(DialogInterface dialog, int which) { 
		                // TODO Auto-generated method stub 
		            	if(mAddTypeET.getText().toString().length() ==0){
		            		Toast.makeText(NoteEditActivity.this,"�벻Ҫ����յķ���",Toast.LENGTH_LONG).show();
		            		//���ڲ��رնԻ���
		            		try {
		            			Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
		            			field.setAccessible(true);
		            			field.set(dialog, false);
		            		} catch (Exception e) {
		            			e.printStackTrace();
		            		}
		            	}else{
		            		String str = mAddTypeET.getText().toString();
		            		mTypeText.setText(str);
		            		mCurrentType = str;
		            		mDbHelper.insertType(str);
		            		//�رնԻ���
		            		try {
		            			Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
		            			field.setAccessible(true);
		            			field.set(dialog, true);
		            		} catch (Exception e) {
		            			e.printStackTrace();
		            		}
		            	}
		            	
		            } 
		        })
		        .setNegativeButton("ȡ��", null)
		        .show();
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
		String titleStr = mTitleText.getText().toString();
		String timeStr = DatabaseHelper.currentTime();
		String typeStr = mTypeText.getText().toString();
		String contentStr = mContentText.getText().toString();
		String text =  "���⣺" + titleStr + "\n" + "�޸�ʱ�䣺" + timeStr + "\n" + "���ࣺ" + typeStr + "\n" + "���ݣ�" + contentStr + "\n";
		createFile(text);
		Toast.makeText(NoteEditActivity.this,"Saved in /sdcard/simpleNote/",Toast.LENGTH_LONG).show();
	}
	
	private void deleteNote() {
		deleteNoteDialog();
	}
	
	public void deleteNoteDialog(){
	 	   new AlertDialog.Builder(NoteEditActivity.this)
	 	   .setTitle("���棡")
	 	   .setMessage("ȷ��Ҫɾ����ǰ��¼��")
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
		ArrayList<String> arrayList = new ArrayList<String>();
		String[] items = new String[100];


		mAllTypes = mDbHelper.getAllTypes();
		startManagingCursor(mAllTypes);
		Cursor c = mAllTypes;
		c.moveToFirst();
		String str;
		do{
			str = c.getString(c.getColumnIndexOrThrow("type"));
			arrayList.add(str);
		}while(c.moveToNext());
		
		items = arrayList.toArray(new String[arrayList.size()]);
		return items;
	}

	
	//�½��ļ���
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

	//ɾ���ļ�
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

	public static void createFile(String text) 
	{
		String filename = createFileName();
		if(newFolder(DIR_NAME)){
			File file = new File(filename);
			if (!file.exists()) {
				try {
					//��ָ�����ļ����д����ļ�
					save(filename, text);
				} catch (Exception e) {
				}
			}
		}
	}

	private static String createFileName()
	{
		String fileName = "�������" + DatabaseHelper.currentTimeNumber();
		return fileName;
	}

	public static void save(String name, String text)
	{
		try {
			File sdCardDir = Environment.getExternalStorageDirectory();//��ȡSDCardĿ¼
			File saveFile = new File(sdCardDir+"/simpleNote/", name +".txt");
			FileOutputStream outStream = new FileOutputStream(saveFile);
			outStream.write(text.getBytes());
			outStream.close();
//			Toast.makeText(NoteEditActivity.this,"Saved in /sdcard/simpleNote/",Toast.LENGTH_LONG).show();
		} catch (FileNotFoundException e) {
			return;
		}
		catch (IOException e){
			return ;
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		mDbHelper.closeclose();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mDbHelper.closeclose();
	}

	
	

}
