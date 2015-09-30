/*

Copyright (C) 2010 Hans-Werner Hilse <hilse@web.de>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

*/

package de.hilses.droidreader;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Provides a scrollable View for PdfPage instances
 * @author hw
 *
 * The class uses three threads: The main View UI thread which
 * handles events like User Input, a Thread that cares for blitting
 * rendered images onto the View's Canvas and implements the scrolling,
 * and finally a third thread that drives the actual rendering. The
 * latter two are run transparently, a user of this class should only
 * deal with the methods it provides itself.
 */
public class DroidReaderView extends SurfaceView
implements OnGestureListener, SurfaceHolder.Callback, DroidReaderDocument.RenderListener {
	/**
	 * Debug helper
	 */
	protected final static String TAG = "DroidReaderView";
	protected final static boolean LOG = false;


	/**
	 * our view thread which does the drawing
	 */
	protected DroidReaderViewThread mThread;

	/**
	 * our gesture detector
	 */
	protected final GestureDetector mGestureDetector;
	
	/**
	 * our context
	 */
	protected final Context mContext;
	
	/**
	 * our SurfaceHolder
	 */
	protected final SurfaceHolder mSurfaceHolder;
	
	protected final DroidReaderDocument mDocument;
	
	/**
	 * constructs a new View
	 * @param context Context for the View
	 * @param attrs attributes (may be null)
	 */
	public DroidReaderView(final Context context, AttributeSet attrs, DroidReaderDocument document) {
		super(context, attrs);
		
		mContext = context;
		mSurfaceHolder = getHolder();
		mDocument = document;
		mDocument.mRenderListener = this;

		// tell the SurfaceHolder to inform this thread on
		// changes to the surface
		mSurfaceHolder.addCallback(this);
		
		mGestureDetector = new GestureDetector(this);
	}

	/* event listeners: */
	
	@Override
	public boolean onTrackballEvent(MotionEvent event) {
		if(LOG) Log.d(TAG, "onTouchEvent(): notifying ViewThread");
		mDocument.offset((int) event.getX() * 20, (int) event.getY() * 20, true);
		mThread.triggerRepaint();
		return true;
	}
	
	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		if(LOG) Log.d(TAG, "onTouchEvent(): notifying mGestureDetector");
		if (mGestureDetector.onTouchEvent(event))
			return true;
		return super.onTouchEvent(event);
	}
	
	/* keyboard events: */
	
	public boolean onKeyDown(int keyCode, KeyEvent msg) {
		if(LOG) Log.d(TAG, "onKeyDown(), keycode "+keyCode);
		return false;
	}
	
	public boolean onKeyUp(int keyCode, KeyEvent msg) {
		if(LOG) Log.d(TAG, "onKeyUp(), keycode "+keyCode);
		return false;
	}
	
	/* interface for the GestureListener: */
	
	@Override
	public boolean onDown(MotionEvent e) {
		// just consume the event
		return true;
	}
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if(LOG) Log.d(TAG, "onFling(): notifying ViewThread");
		mThread.mScroller.fling(0, 0, -(int) velocityX, -(int) velocityY, -4096, 4096, -4096, 4096);
		mThread.triggerRepaint();
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		if(LOG) Log.d(TAG, "onLongPress(): ignoring!");
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		if(LOG) Log.d(TAG, "onScroll(), distance vector: "+distanceX+","+distanceY);
		mDocument.offset((int) distanceX, (int) distanceY, true);
		mThread.triggerRepaint();
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		if(LOG) Log.d(TAG, "onShowPress(): ignoring!");
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// just consume the event...
		return true;
	}
	
	/* surface events: */
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if(LOG) Log.d(TAG, "surfaceCreated(): starting ViewThread");
		mThread = new DroidReaderViewThread(holder, mContext, mDocument);
		mThread.start();
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if(LOG) Log.d(TAG, "surfaceChanged(): size "+width+"x"+height);
		mDocument.startRendering(width, height);
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if(LOG) Log.d(TAG, "surfaceDestroyed(): dying");
		mDocument.stopRendering();
		boolean retry = true;
		mThread.mRun = false;
		mThread.interrupt();
		while (retry) {
			try {
				mThread.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}
	}
	
	/* render events */

	@Override
	public void onNewRenderedPixmap() {
		if(LOG) Log.d(TAG, "new rendered pixmap was signalled");
		mThread.triggerRepaint();
	}
}
