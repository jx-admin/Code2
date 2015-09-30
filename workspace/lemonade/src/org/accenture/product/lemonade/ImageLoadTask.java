package org.accenture.product.lemonade;

import java.io.File;

import org.accenture.product.lemonade.MediaPicListActivity.MediaPicAdapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class ImageLoadTask extends AsyncTask<String, Bitmap, Void>{
	
	private MediaPicAdapter adapter;
	private static final String MOVE_PATH="/sdcard/DCIM/.thumbnails";
	Context context;
	
	public ImageLoadTask(Context context, MediaPicAdapter adapter){		
		this.adapter = adapter;
		this.context=context;
	}
	
	@Override
	protected void onPreExecute() {
		adapter.clear();
		
	}
	
	@Override
	protected Void doInBackground(String... parentPath) {
		   //搜索图片文件
		File f=new File(parentPath[0]);
        File files[] = f.listFiles();
        if(files!=null){
	         for(File tempF:files){
	             if(tempF.isDirectory()){
	            	 doInBackground(tempF.getAbsolutePath());
	             }
	             else{
	                 String path = tempF.getPath();
	                 String fpath = path.substring(path.lastIndexOf(".")+1,path.length());
	                 if("jpg".equals(fpath)||"png".equals(fpath)||"gif".equals(fpath)||"jpeg".equals(fpath)||"bmp".equals(fpath)){
	                	if(isCancelled()) 
	                		return null;           	
	                	
//	                	if(!MOVE_PATH.equals(tempF.getParent())){
//	                		Bitmap bitmap=getThumBitmap(path);
//		                	publishProgress(bitmap);
//	                	}
	                	
	                	
	                	Bitmap bitmap=getThumBitmap(path);
	                	publishProgress(bitmap);
	                	
	                 }
	             }
	         }	         
        }
		
//		for(int i=0;i<300;i++){
//			
//			Bitmap bitmap=getThumBitmap("/sdcard/DCIM/Camera/IMG_20110629_225018.jpg");
//			publishProgress(bitmap);
////			publishProgress("/sdcard/music/Camera/IMG_20110629_225018.jpg");
//		}
        return null;
	}
	
	@Override
    public void onProgressUpdate(Bitmap... bitmap) {
    	if(isCancelled()) 
    		return;
    	((Activity)context).setProgressBarIndeterminateVisibility(true);
    	adapter.add(bitmap[0]);
    	adapter.notifyDataSetChanged();
    }
	
	private Bitmap getThumBitmap(String path)
	{
		try
		{
			// 获取源图片的大小
			Bitmap bm;
			BitmapFactory.Options opts = new BitmapFactory.Options();
			// 当opts不为null时，但decodeFile返回空，不为图片分配内存，只获取图片的大小，并保存在opts的outWidth和outHeight
			bm = BitmapFactory.decodeFile(path, opts);
			int srcWidth = opts.outWidth;
			int srcHeight = opts.outHeight;
			int destWidth = 0;
			int destHeight = 0;
			// 缩放的比例
			double ratio = 0.0;
			// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
			if (srcWidth > srcHeight)
			{
				ratio = srcWidth / MediaPicListActivity.IMAGE_WIDTH;
				destWidth = MediaPicListActivity.IMAGE_WIDTH;
				destHeight = (int) (srcHeight / ratio);
			}
			else
			{
				ratio = srcHeight / MediaPicListActivity.IMAGE_WIDTH;
				destHeight = MediaPicListActivity.IMAGE_WIDTH;
				destWidth = (int) (srcWidth / ratio);
			}
			// 对图片进行压缩，是在读取的过程中进行压缩，而不是把图片读进了内存再进行压缩
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
			newOpts.inSampleSize = (int) ratio + 1;
			// inJustDecodeBounds设为false表示把图片读进内存中
			newOpts.inJustDecodeBounds = false;
			// 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
			newOpts.outHeight = destHeight;
			newOpts.outWidth = destWidth;
			// 获取缩放后图片
			Bitmap destBm = BitmapFactory.decodeFile(path, newOpts);
			bm.recycle();
			return destBm;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	protected void onPostExecute(Void result)
	{
		((Activity)context).setProgressBarIndeterminateVisibility(false);
		super.onPostExecute(result);
	}
}
