package com.twh.simplenote;

import java.io.File;
import java.util.Calendar;

import android.database.Cursor;

public class ExportNotes {
	
	private static String DIR_NAME = "simpleNote";
	private static String SDCARD = "/sdcard/";
	
	private DatabaseHelper mDbHelper;
	private Cursor mNoteCursor;
	private long currentId;
	
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
    
    public static void createFile() 
    {
    	String filename = createFileName();
    	if(newFolder(DIR_NAME)){
    		File file = new File(filename);
    		if (!file.exists()) {
    			  try {
    				  //在指定的文件夹中创建文件
    				  file.createNewFile();
    			  } catch (Exception e) {
    			  }
    		}
    	}
    }
    
    private static String createFileName()
    {
    	Calendar calendar = Calendar.getInstance();
    	String fileName = "导出结果 " + calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH)+" "
    			+calendar.get(Calendar.HOUR_OF_DAY)+":"+ DatabaseHelper.getMinute() + ":" + DatabaseHelper.getSecond();
    	return fileName;
    }
}
