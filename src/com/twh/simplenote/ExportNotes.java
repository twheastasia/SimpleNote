package com.twh.simplenote;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

public class ExportNotes {
	
	private static String DIR_NAME = "simpleNote";
	private static String SDCARD = "/sdcard/";
	
	private DatabaseHelper mDbHelper;
	private static Cursor mNoteCursor;
	
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
    
    public static void createFile(String title, String type, String content, String time) 
    {
    	String filename = createFileName();
    	if(newFolder(DIR_NAME)){
    		File file = new File(filename);
    		if (!file.exists()) {
    			  try {
    				  //��ָ�����ļ����д����ļ�
    				  file.createNewFile();
    				  exportNoteIntoTxt(filename, title, type, content, time);
    			  } catch (Exception e) {
    			  }
    		}
    	}
    }
    
    private static String createFileName()
    {
    	String fileName = "�������  " + DatabaseHelper.currentTime();
    	return fileName;
    }
    
  //���Ѵ������ļ���д������
  	public static void exportNoteIntoTxt(String name, String title, String type, String content, String time) {
  		FileWriter fw = null;
  		BufferedWriter bw = null;
  		try {
  			fw = new FileWriter(name, true);
  			// ����FileWriter��������д���ַ���
  			bw = new BufferedWriter(fw); // ��������ļ������
  			String myreadline =  "���⣺" + title + "\n" + "�޸�ʱ�䣺" + time + "\n" + "���ࣺ" + type + "\n" + "���ݣ�" + content + "\n";
  			bw.write(myreadline); // д���ļ�
  			bw.newLine();
  			bw.flush(); // ˢ�¸����Ļ���
  			bw.close();
  			fw.close();
  		} catch (IOException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  			try {
  				bw.close();
  				fw.close();
  			} catch (IOException e1) {
  				// TODO Auto-generated catch block
  			}
  		}
  	}
  	
}