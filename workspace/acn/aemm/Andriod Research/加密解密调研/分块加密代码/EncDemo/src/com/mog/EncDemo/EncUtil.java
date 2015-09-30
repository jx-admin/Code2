package com.mog.EncDemo;

import java.io.File;
import java.io.IOException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import com.mog.EncDemo.FileService; 
/*
 * util file. 
 */
public class EncUtil {
	public static int nFileTag = 13;
	public static int MAX_MD5_VAL =	17 ; 
	public static int MAX_DECALG_VAL = 5;
	public static int MAX_FILE_EXTVAL = 13 ;
	public static String FS_ENC_STR	= "MogEncFS 1.0" ; 
	public static String FS_DEC_STR	= "MogDecFS 1.0" ; 
	public static String FS_DST_FNAME = "EncFile.mec" ; 
	public static int MAX_BLOCK_SIZE = 512 ; 
	
	public static boolean InitFile() throws Exception
	{
		//String strTem = new String("abcdefghijklmnopqrstuvwxyz1234567890.+-*/abcdefghijklmnopqrstuvwxyz1234567890.+-*/abcdefghijklmnopqrstuvwxyz1234567890.+-*/abcdefghijklmnopqrstuvwxyz1234567890.+-*/abcdefghijklmnopqrstuvwxyz1234567890.+-*/abcdefghijklmnopqrstuvwxyz1234567890.+-*/abcdefghijklmnopqrstuvwxyz1234567890.+-*/abcdefghijklmnopqrstuvwxyz1234567890.+-*/abcdefghijklmnopqrstuvwxyz1234567890.+-*/abcdefghijklmnopqrstuvwxyz1234567890.+-*/abcdefghijklmnopqrstuvwxyz1234567890.+-*/abcdefghijklmnopqrstuvwxyz1234567890.+-*/abcdefghijklmnopqrstuvwxyz1234567890.+-*/abcdefghijklmnopqrstuvwxyz1234567890.+-*/abcdefghijklmnopqrstuvwxyz1234567890.+-*/abcdefghijklmnopqrstuvwxyz1234567890.+-*/abcdefghijklmnopqrstuvwxyz1234567890.+-*/abcdefghijklmnopqrstuvwxyz1234567890.+-*/abcdefghijklmnopqrstuvwxyz1234567890.+-*/abcdefghijklmnopqrstuvwxyz1234567890.+-*/abcdefghijklmnopqrstuvwxyz1234567890.+-*/abcdefghijklmnopqrstuvwxyz1234567890.+-*/abcdefghijklmnopqrstuvwxyz1234567890.+-*/abcdefghijklmnopqrstuvwxyz1234567890.+-*/abcdefghijklmnopqrstuvwxyz1234567890.+-*/abcdefghijklmnopqrstuvwxyz1234567890.+-*/abcdefghijklmnopqrstuvwxyz1234567890.+-*/abcdefghijklmnopqrstuvwxyz1234567890.+-*/abcdefghijklmnopqrstuvwxyz1234567890.+-*/abcdefghijklmnopqrstuvwxyz1234567890.+-*/abcdefghijklmnopqrstuvwxyz1234567890.+-*/");
		
		//String strfname = new String(getSDPath()) ; 
		//strfname = strfname + "a.txt" ; 
		//if(fileIsExists(strfname))
		{
		//	return false ; 
		}
		//else
		{
			//FileService fs = new FileService() ;
			//fs.save(strfname, strTem) ; 
		}
		return true ; 
	}
	
	public static String getSDPath()
	{ 
       File sdDir = null; 
       boolean sdCardExist = Environment.getExternalStorageState()   
                           .equals(android.os.Environment.MEDIA_MOUNTED);   //ÅĞ¶Ïsd¿¨ÊÇ·ñ´æÔÚ 
       if(sdCardExist)   
       {
         sdDir = Environment.getExternalStorageDirectory();//»ñÈ¡¸úÄ¿Â¼
       }
       return (sdDir.toString() + "/"); 
	}
	
    public static boolean isEmpty(String s) 
    {
        if(s == null || "".equals(s.trim())) 
        {
                return true;
        }
        return false;
    }
    
    public static void showMessage(Context context ,String strTip , String strInfo , String strBText)
    {
        new AlertDialog.Builder(context) 
        .setTitle(strTip)
        .setMessage(strInfo)
        .setPositiveButton( strBText ,
        new DialogInterface.OnClickListener() {
        public void onClick(
        DialogInterface dialoginterface, int i){
        }        
        }).show();    
    }
    
    public static boolean CreateFolder(String path)
    {
    	if(isEmpty(path))
    	{
    		return false ; 
    	}
		File file=new File(path); 
		if(!file.exists()) 
		{
			file.mkdir();
			return true ; 
		}
		else
			return false ; 
    }
    
    public static boolean fileIsExists(String fname)
    {
    	if(isEmpty(fname))
    	{
    		return false ; 
    	}
        File f=new File(fname);
        if(!f.exists())
        {
        	return false;
        }
        return true;
    }
    
    public static boolean createFile(String fname , boolean bCover) throws IOException
    {
    	if( fileIsExists(fname) )
    	{
    		if( bCover )
    		{
    			File f = new File(fname) ; 
    			f.delete();
    		}
    	}
		File fnew = new File(fname) ; 
		fnew.createNewFile() ; 
    	return true ; 
    }
    public static int EncStrlen(byte[] buf)
	{
		int nCount = 0 ;
		nCount = buf.length; 
		/*for( nCount = 0 ; buf[nCount] != 0 ; nCount++ )
			nCount++ ; */
		return nCount ; 
	}
	
    public static int EncStrcpy(byte[] dstbuf , byte[] srcbuf)
	{
		int nCount = 0 ;
		System.arraycopy(srcbuf, 0, dstbuf, 0, srcbuf.length) ;
		nCount = srcbuf.length ; 
		/*while( srcbuf[nCount] != '\0' )
		{
			dstbuf[nCount] = srcbuf[nCount] ;
			nCount++ ; 
		}*/
		return nCount ; 
	}
    public static int EncMin(int m , int n)
	{
		if( m> n )
			return n ; 
		else
			return m ; 
	}
}
