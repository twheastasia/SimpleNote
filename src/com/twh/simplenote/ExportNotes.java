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
    
    public static void createFile(String title, String type, String content, String time) 
    {
    	String filename = createFileName();
    	if(newFolder(DIR_NAME)){
    		File file = new File(filename);
    		if (!file.exists()) {
    			  try {
    				  //在指定的文件夹中创建文件
    				  file.createNewFile();
    				  exportNoteIntoTxt(filename, title, type, content, time);
    			  } catch (Exception e) {
    			  }
    		}
    	}
    }
    
    private static String createFileName()
    {
    	String fileName = "导出结果  " + DatabaseHelper.currentTime();
    	return fileName;
    }
    
  //向已创建的文件中写入数据
  	public static void exportNoteIntoTxt(String name, String title, String type, String content, String time) {
  		FileWriter fw = null;
  		BufferedWriter bw = null;
  		try {
  			fw = new FileWriter(name, true);
  			// 创建FileWriter对象，用来写入字符流
  			bw = new BufferedWriter(fw); // 将缓冲对文件的输出
  			String myreadline =  "标题：" + title + "\n" + "修改时间：" + time + "\n" + "分类：" + type + "\n" + "内容：" + content + "\n";
  			bw.write(myreadline); // 写入文件
  			bw.newLine();
  			bw.flush(); // 刷新该流的缓冲
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