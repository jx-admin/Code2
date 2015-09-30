package com.aess.aemm.view.msg;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.aess.aemm.R;
import com.aess.aemm.db.NewsContent;
import com.aess.aemm.view.pdf.PdfViewerActivity;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFImage;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFPaint;

public class AccessoryActivity   extends Activity{
	private static final int STARTPAGE = 1;
	private static final float STARTZOOM = 1.0f;
	
	private static final String TAG = "AccessoryActivity";
	
	private final static int MEN_NEXT_PAGE = 1;
	private final static int MEN_PREV_PAGE = 2;
	private final static int MEN_ZOOM_IN   = 3;
	private final static int MEN_ZOOM_OUT  = 4;
	private final static int MEN_BACK      = 5;
	
	private GraphView mGraphView;
	private String pdffilename;
	private PDFFile mPdfFile;
	private int mPage;
	private float mZoom;
    private File mTmpFile;
    
    private PDFPage mPdfPage; 
    
    private Thread backgroundThread;
    //////////////////////////////////////////////////////////
	public static final String IMAGE_UNSPECIFIED = "image/";
	public static final String VIDEO_UNSPECIFIED = "video/";
	public static final String DOC_PDF = "application/pdf";
	public static final String AUDIO="audio/";
	public static String curMiMe="unkonw";
	public int curPro=-1;
	private Class cls;
	public static void start(Activity a,String type,Uri uri ,String title,int flag,Class cls){
		if(DOC_PDF.equals(type)){
			PdfViewerActivity.Start(a, uri.getEncodedPath(),flag,cls);
		}else{
			Intent i=new Intent(a,AccessoryActivity.class);
			i.putExtra("type", type);
			i.putExtra("uri", uri);
			i.putExtra("title", title);
			i.putExtra("class", cls);
			a.startActivityForResult(i, flag);
		}
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accessory_layout);
		Intent intent=getIntent();
		String type=(String) intent.getExtra("type");
		Uri uri=(Uri) intent.getExtra("uri");
		cls=(Class) intent.getExtra("class");
		Log.d("AccessoryActivity",type+uri.toString());
		curPro=-1;
		Log.d(TAG,"uri:"+uri.toString());
		if(type!=null)
		if(type.startsWith(IMAGE_UNSPECIFIED)){
			ImageView iv=(ImageView) findViewById(R.id.image);
			iv.setVisibility(View.VISIBLE);
			iv.setImageURI(uri);
			curMiMe=IMAGE_UNSPECIFIED;
			curPro=NewsContent.MSG_SUCCESSFUL;
		}else if(type.startsWith(VIDEO_UNSPECIFIED)){
			VideoView videoView=(VideoView) findViewById(R.id.video);
			MediaController mc=new MediaController(this);
			mc.bringToFront();
			videoView.setMediaController(mc);
			videoView.setOnCompletionListener(new OnCompletionListener(){
				@Override
				public void onCompletion(MediaPlayer arg0) {
					curPro=NewsContent.MSG_SUCCESSFUL;
				}});
			videoView.setVisibility(View.VISIBLE);
			videoView.setVideoURI(uri);
			videoView.start();
			curMiMe=VIDEO_UNSPECIFIED;
		}else if(type.startsWith(AUDIO)){
			TextView title_tv=(TextView)findViewById(R.id.title_tv);
			title_tv.setVisibility(View.VISIBLE);
			title_tv.setText((String) intent.getExtra("title"));
			VideoView videoView=(VideoView) findViewById(R.id.video);
			MediaController mc=new MediaController(this);
			mc.bringToFront();
			videoView.setMediaController(mc);
			videoView.setOnCompletionListener(new OnCompletionListener(){
				@Override
				public void onCompletion(MediaPlayer arg0) {
					curPro=NewsContent.MSG_SUCCESSFUL;
				}});
			videoView.setVisibility(View.VISIBLE);
			videoView.setVideoURI(uri);
			videoView.start();
			mc.show();
			curMiMe=VIDEO_UNSPECIFIED;
		}else if(DOC_PDF.equals(type)){
			if (mGraphView == null) {
		        mGraphView = new GraphView(this);
		        
		        byte[] pdfBinary = null;
		        
		        pdffilename =uri.getEncodedPath();
		        
		        if (pdffilename == null)
		        	pdffilename = "no file selected";
		        boolean showImages =true;// getIntent().getBooleanExtra(PdfFileSelectActivity.EXTRA_SHOWIMAGES, PdfFileSelectActivity.DEFAULTSHOWIMAGES);
		        PDFImage.sShowImages = showImages;
		        boolean antiAlias = true;//getIntent().getBooleanExtra(PdfFileSelectActivity.EXTRA_ANTIALIAS, PdfFileSelectActivity.DEFAULTANTIALIAS);
		        PDFPaint.s_doAntiAlias = antiAlias;
		
		        parsePDF(pdffilename, pdfBinary);
		        
		        setContentView(mGraphView);
		        
				mPage = STARTPAGE;
				mZoom = STARTZOOM;
		        startRenderThread(mPage, mZoom);
				curMiMe=DOC_PDF;
	        }
			finish();
		}else{
//			Toast.makeText(this, "unknow type", 1).show();
			curPro=-3;
		}
	}
	



	private void showRenderStatus(Canvas c, int x, int y, Paint p) {
		int maxCmds = PDFPage.getParsedCommands();
		int curCmd = PDFPage.getLastRenderedCommand()+1;
		c.drawText("PDF-Commands: "+curCmd+"/"+maxCmds, x, y, p);
    }
    
	private synchronized void startRenderThread(final int page, final float zoom) {
		if (backgroundThread != null)
			return;
        mGraphView.mOffX = 0;
        mGraphView.mOffY = 0;
        mGraphView.uiInvalidate();
        backgroundThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
			        if (mPdfFile != null) {

		    			Log.d(TAG,"startRenderThread "+mPage);
//			        	File f = new File("/sdcard/andpdf.trace");
//			        	f.delete();
//			        	Log.e(TAG, "DEBUG.START");
//			        	Debug.startMethodTracing("andpdf");
			        	showPage(page, zoom);
//			        	Debug.stopMethodTracing();
//			        	Log.e(TAG, "DEBUG.STOP");
			        }
			        if(page==mPdfFile.getNumPages()){
						curPro=NewsContent.MSG_SUCCESSFUL;
			        }
				} catch (Exception e) {
					Log.e(TAG, e.getMessage(), e);
				}
		        backgroundThread = null;
		        Log.d(TAG,"endRenderThread "+mPage);
			}
		});
        updateImageStatus();
        backgroundThread.start();
	}


	private void updateImageStatus() {
//		Log.i(TAG, "updateImageStatus: " +  (System.currentTimeMillis()&0xffff));
		if (backgroundThread == null) {
			mGraphView.uiInvalidate();
			return;
		}
		mGraphView.uiInvalidateText();
		mGraphView.postDelayed(new Runnable() {
			@Override public void run() {
				updateImageStatus();
			}
		}, 1000);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if(mPdfFile!=null){
        menu.add(Menu.NONE, MEN_PREV_PAGE, Menu.NONE, "Previous Page");
        menu.add(Menu.NONE, MEN_NEXT_PAGE, Menu.NONE, "Next Page");
        menu.add(Menu.NONE, MEN_ZOOM_OUT, Menu.NONE, "Zoom Out");
        menu.add(Menu.NONE, MEN_ZOOM_IN, Menu.NONE, "Zoom In");
        menu.add(Menu.NONE, MEN_BACK, Menu.NONE, "Back");
        return true;
        }else{
        	return false;
        }
    }
    
    /**
     * Called when a menu item is selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
    	switch (item.getItemId()) {
    	case MEN_NEXT_PAGE: {
    		nextPage();
    		break;
    	}
    	case MEN_PREV_PAGE: {
    		prevPage();
    		break;
    	}
    	case MEN_ZOOM_IN: {
    		zoomIn();
    		break;
    	}
    	case MEN_ZOOM_OUT: {
    		zoomOut();
    		break;
    	}
    	case MEN_BACK: {
    		 exit();
            finish();
            break;
    	}
    	}
    	return true;
    }
    
    
    private void zoomIn() {
    	if (mPdfFile != null) {
    		if (mZoom < 4) {
    			mZoom *= 1.5;
    			if (mZoom > 4)
    				mZoom = 4;
    			startRenderThread(mPage, mZoom);
    		}
    	}
	}

    private void zoomOut() {
    	if (mPdfFile != null) {
    		if (mZoom > 0.25) {
    			mZoom /= 1.5;
    			if (mZoom < 0.25)
    				mZoom = 0.25f;
    			startRenderThread(mPage, mZoom);
    		}
    	}
	}

	private void nextPage() {
    	if (mPdfFile != null) {
    		if (mPage < mPdfFile.getNumPages()) {
    			mPage += 1;
    			Log.d(TAG,"nextPage"+mPage);
    			startRenderThread(mPage, mZoom);
    		}
    	}
	}

    private void prevPage() {
    	if (mPdfFile != null) {
    		if (mPage > 1) {
    			mPage -= 1;
    			Log.d(TAG,"nextPage"+mPage);
    			startRenderThread(mPage, mZoom);
    		}
    	}
	}

	private class GraphView extends View {
    	private String mText;
		private float mLastX;
    	private float mLastY;
    	public float mOffX;
    	public float mOffY;
    	private long fileMillis;
    	private long pageMillis;
    	Canvas mCan;
    	Bitmap mBi;
        
        public GraphView(Context context) {
            super(context);
            mOffX = 0;
            mOffY = 0;
            
//            setPageBitmap();
            setBackgroundColor(Color.TRANSPARENT);
        }

        private void showText(String text) {
        	Log.i(TAG, "ST='"+text+"'");
        	mText = text;
        	uiInvalidate();
		}
        
        private void uiInvalidate() {
    		Log.d(TAG,"uiInvalidate "+mPage);
        	postInvalidate();
		}

        private void uiInvalidateText() {
        	postInvalidate(0, 40, 320, 60);
		}

		private void setPageBitmap() {
			mBi = Bitmap.createBitmap(100, 100, Config.RGB_565);
            mCan = new Canvas(mBi);
            mCan.drawColor(Color.RED);
            
			Paint paint = new Paint();
            paint.setColor(Color.BLUE);
            mCan.drawCircle(50, 50, 50, paint);
            
            paint.setStrokeWidth(0);
            paint.setColor(Color.BLACK);
            mCan.drawText("Bitmap", 10, 50, paint);

		}
        
        @Override
        public boolean onTouchEvent(MotionEvent event) {
        	super.onTouchEvent(event);
        	if (event.getAction() == MotionEvent.ACTION_DOWN) {
        		mLastX = event.getRawX();
        		mLastY = event.getRawY();
        	}
        	else if (event.getAction() == MotionEvent.ACTION_MOVE) {

        		float x = event.getRawX();
        		float y = event.getRawY();
        		float dx = x-mLastX;
        		float dy = y-mLastY;
        		mLastX = x;
        		mLastY = y;
        		mOffX += dx;
        		mOffY += dy;
    			uiInvalidate();
        	}
        	return true;
        }
        
		@Override protected void onDraw(Canvas canvas) {
			Paint paint = new Paint();

			canvas.drawColor(Color.LTGRAY);

          /*  paint.setStrokeWidth(0);
            paint.setColor(Color.BLACK);
            canvas.drawText("PdfViewer: "+mText, 10, 20, paint);
            
            float fileTime = fileMillis*0.001f;
            float pageTime = pageMillis*0.001f;
            canvas.drawText("seconds: parse="+fileTime+" show="+pageTime, 10, 40, paint);

            showRenderStatus(canvas, 10, 60, paint);
            
            // draw the normal strings
            paint.setColor(Color.BLUE);
            canvas.drawCircle(mOffX, mOffY, 5, paint);
            canvas.drawCircle(mOffX+mBi.getWidth(), mOffY, 5, paint);
            canvas.drawCircle(mOffX+mBi.getWidth(), mOffY+mBi.getHeight(), 5, paint);
            canvas.drawCircle(mOffX, mOffY+mBi.getHeight(), 5, paint);*/
			if(mBi!=null)
            canvas.drawBitmap(mBi, mOffX, mOffY, paint);
//            canvas.drawBitmap(mBi, Utils.createMatrix(1,0,0,-1,mOffX,mOffY+mBi.getHeight()), paint);

        }
		
   } private void showPage(int page, float zoom) throws Exception {
       long startTime = System.currentTimeMillis();
   	try {
   		Log.d(TAG,"showPage "+startTime);
	        mPdfPage = mPdfFile.getPage(page, true);
	        int num = mPdfPage.getPageNumber();
	        int maxNum = mPdfFile.getNumPages();
	        float wi = mPdfPage.getWidth();
	        float hei = mPdfPage.getHeight();
	        String pageInfo= new File(pdffilename).getName() + " - " + num +"/"+maxNum+ ": " + wi + "x" + hei;
	        mGraphView.showText(pageInfo);
	        Log.i(TAG, pageInfo);
	        RectF clip = null;
	        // free memory from previous page
//	        mGraphView.setPageBitmap();
	        mGraphView.mBi = mPdfPage.getImage((int)(wi*zoom), (int)(hei*zoom), clip, true, true);
	        mGraphView.uiInvalidate();
		} catch (Throwable e) {
			Log.e(TAG, e.getMessage(), e);
			mGraphView.showText("Exception: "+e.getMessage());
		}
		Log.d(TAG,"end showPage "+startTime);
       long stopTime = System.currentTimeMillis();
       mGraphView.pageMillis = stopTime-startTime;
   }
   
   private void parsePDF(String filename, byte[] pdfBinary) {
       long startTime = System.currentTimeMillis();
   	try {
   		if (pdfBinary != null) {
	        	long len = pdfBinary.length;
       		mGraphView.showText("uri '" + filename + "' has " + len + " bytes");
   	    	openFile(null, pdfBinary);
   		}
   		else {
	        	File f = new File(filename);
	        	long len = f.length();
	        	if (len == 0) {
	        		mGraphView.showText("file '" + filename + "' not found");
	        	}
	        	else {
	        		mGraphView.showText("file '" + filename + "' has " + len + " bytes");
	    	    	openFile(f, null);
	        	}
   		}
		} catch (Throwable e) {
			e.printStackTrace();
			mGraphView.showText("Exception: "+e.getMessage());
		}
       long stopTime = System.currentTimeMillis();
       mGraphView.fileMillis = stopTime-startTime;
	}
   
   /**
    * <p>Open a specific pdf file.  Creates a DocumentInfo from the file,
    * and opens that.</p>
    *
    * <p><b>Note:</b> Mapping the file locks the file until the PDFFile
    * is closed.</p>
    *
    * @param file the file to open
    * @throws IOException
    */
   public void openFile(File file, byte[] pdfBinary) throws IOException {
   	byte[] buf = pdfBinary;
   	if (buf == null)
   		buf = readBytes(file);
//       // first open the file for random access
//       RandomAccessFile raf = new RandomAccessFile(file, "r");
//
//       // extract a file channel
//       FileChannel channel = raf.getChannel();
//
//       // now memory-map a byte-buffer
//       ByteBuffer buf =
//               channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
       // create a PDFFile from the data
   	
//       mPdfFile = new PDFFile(new ByteBuffers(buf));
       mGraphView.showText("Anzahl Seiten:" + mPdfFile.getNumPages());
   }
   
   
   private byte[] readBytes(File srcFile) throws IOException {
   	long fileLength = srcFile.length();
   	int len = (int)fileLength;
   	byte[] result = new byte[len];
   	FileInputStream fis = new FileInputStream(srcFile);
   	int pos = 0;
		int cnt = fis.read(result, pos, len-pos);
   	while (cnt > 0) {
   		pos += cnt;
   		cnt = fis.read(result, pos, len-pos);
   	}
		return result;
	}

//	private String storeUriContentToFile(Uri uri) {
//   	String result = null;
//   	try {
//	    	if (mTmpFile == null) {
//				File root = Environment.getExternalStorageDirectory();
//				if (root == null)
//					throw new Exception("external storage dir not found");
//				mTmpFile = new File(root,"AndroidPdfViewer/AndroidPdfViewer_temp.pdf");
//				mTmpFile.getParentFile().mkdirs();
//	    		mTmpFile.delete();
//	    	}
//	    	else {
//	    		mTmpFile.delete();
//	    	}
//	    	InputStream is = getContentResolver().openInputStream(uri);
//	    	OutputStream os = new FileOutputStream(mTmpFile);
//	    	byte[] buf = new byte[1024];
//	    	int cnt = is.read(buf);
//	    	while (cnt > 0) {
//	    		os.write(buf, 0, cnt);
//		    	cnt = is.read(buf);
//	    	}
//	    	os.close();
//	    	is.close();
//	    	result = mTmpFile.getCanonicalPath();
//	    	mTmpFile.deleteOnExit();
//   	}
//   	catch (Exception e) {
//   		Log.e(TAG, e.getMessage(), e);
//		}
//		return result;
//	}

   
   private byte[] readUriContent(Uri uri) {
 	  byte[] result = null;
 	try {
     	InputStream is = getContentResolver().openInputStream(uri);
     	int size = is.available();
     	result = new byte[size];
     	int pos = 0;
     	int cnt = is.read(result, pos, size-pos);
     	while (cnt > 0) {
     		pos += cnt;
         	cnt = is.read(result, pos, size-pos);
     	}
     	is.close();
 	}
 	catch (Exception e) {
 		Log.e(TAG, e.getMessage(), e);
 	}
 	return result;
 }
   @Override
	public boolean onKeyDown(int arg0, KeyEvent arg1) {
		   if(arg0==KeyEvent.KEYCODE_BACK){
			   exit();
		   }
		// TODO Auto-generated method stub
		return super.onKeyDown(arg0, arg1);
	}
   private void exit(){
//	   Toast.makeText(this, "send:"+curPro, 0).show();
		Intent i=new Intent(this,cls);
		i.putExtra("r", curPro);
		setResult(curPro, i);
	   
   }
   
   @Override
   protected void onDestroy() {
   	super.onDestroy();
   	if (mTmpFile != null) {
   		mTmpFile.delete();
   		mTmpFile = null;
 
   	  	}
   }
   

Runnable mRenderTask = new Runnable() {
       public void run() {
       	
       }
   };
}
