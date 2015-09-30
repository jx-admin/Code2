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

import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;

import java.lang.String;
import java.nio.ByteBuffer;

/**
 * An instance of this class will provide font file names, reading from Preferences
 */
interface FontProvider {
	/**
	 * callback that is used to retrieve font file names
	 * @param fontName the name of the font to load
	 * @param collection the collection of fonts (CID)
	 * @param flags font flags as understood by the MuPDF library
	 * @return file name with full path
	 */
	String getFontFile(String fontName, String collection, int flags);
	
	/**
	 * callback that is used to retrieve a buffer containing font data
	 * @param fontName the name of the font to load
	 * @param collection the collection of fonts (CID)
	 * @param flags font flags as understood by the MuPDF library
	 * @return allocated ByteBuffer filled with the font data
	 */
	ByteBuffer getFontBuffer(String fontName, String collection, int flags);
	
	/**
	 * callback that is used to retrieve a buffer containing charmap (CMap) data
	 * @param cmapName charmap to load
	 * @return allocated ByteBuffer filled with the cmap data
	 */
	ByteBuffer getCMapBuffer(String cmapName);
}

/**
 * Fallback FontProvider that returns nothing
 */
class NullFontProvider implements FontProvider {
	/**
	 * always returns null
	 */
	@Override
	public String getFontFile(String fontName, String collection, int flags) {
		return null;
	}
	/**
	 * always returns null
	 */
	@Override
	public ByteBuffer getFontBuffer(String fontName, String collection, int flags) {
		return null;
	}
	/**
	 * always returns null
	 */
	@Override
	public ByteBuffer getCMapBuffer(String cmapName) {
		return null;
	}
}

/**
 * Class that just loads the JNI lib and holds some basic configuration
 */
final class PdfRender {
	/**
	 * how much bytes are stored for one pixel
	 */
	protected static int bytesPerPixel = 4;
	/**
	 * how much memory is the MuPDF backend allowed to use
	 */
	protected static int fitzMemory = 512 * 1024;
	
	/**
	 * the FontProvider instance that is queried from JNI code
	 */
	protected static FontProvider fontProvider = new NullFontProvider();

	static {
		/* JNI: load our native library */
		System.loadLibrary("pdfrender");
	}
	
	/**
	 * just checks if a given file can be accessed from JNI code
	 * @param fname the filename to check for
	 * @return 0 if the file was found, libc's errno otherwise
	 */
	private static native int nativeCheckFont(String fname);
	
	/**
	 * convenience method to check if a file is present and accessible from native code
	 * @param fname the filename to check for
	 * @return true if the file is there and we can read it, false otherwise
	 */
	static boolean checkFont(String fname) {
		return (nativeCheckFont(fname) == 0);
	}
	
	/**
	 * Sets a new FontProvider
	 * @param newProvider the new FontProvider
	 */
	static void setFontProvider(FontProvider newProvider) {
		fontProvider = newProvider;
	}
}

class CannotRepairException extends Exception {
	private static final long serialVersionUID = 1L;
	CannotRepairException(String detailMessage) { super(detailMessage); }
}
class CannotDecryptXrefException extends Exception {
	private static final long serialVersionUID = 1L;
	CannotDecryptXrefException(String detailMessage) { super(detailMessage); }
}
class PasswordNeededException extends Exception {
	private static final long serialVersionUID = 1L;
	PasswordNeededException(String detailMessage) { super(detailMessage); }
}
class PageLoadException extends Exception {
	private static final long serialVersionUID = 1L;
	PageLoadException(String detailMessage) { super(detailMessage); }
}
class PageRenderException extends Exception {
	private static final long serialVersionUID = 1L;
	PageRenderException(String detailMessage) { super(detailMessage); }
}
class WrongPasswordException extends Exception {
	private static final long serialVersionUID = 1L;
	WrongPasswordException(String detailMessage) { super(detailMessage); }
}

/**
 * Instantiate this for each PDF document
 */
class PdfDocument {
	/**
	 * the native MuPDF backend will set this to the document title
	 */
	public String metaTitle;
	/**
	 * backend sets this to the number of pages
	 */
	public int pagecount = 0;
	
	/**
	 * this will be used to store a C Pointer (ick!) to the
	 * structure holding our references in the native code
	 */
	protected long mHandle = 0;
	
	/**
	 * will open a PDF document
	 * @param fitzMemory the memory that the MuPDF rendering backend is allowed to claim
	 * @param filename file to be opened
	 * @param password password for the PDF
	 * @return new handle
	 */
	private native long nativeOpen(
			int fitzMemory,
			String filename, String password) 
		throws
			PasswordNeededException,
			WrongPasswordException,
			CannotRepairException,
			CannotDecryptXrefException;
	
	/**
	 * open a PDF
	 * @param filename the PDF file
	 * @param password the password to use for opening
	 */
	public void open(String filename, String password)
	throws
		PasswordNeededException, 
		WrongPasswordException,
		CannotRepairException,
		CannotDecryptXrefException
	{
		if(mHandle != 0)
			this.close();
		mHandle = this.nativeOpen(
				PdfRender.fitzMemory, filename, password);
	}
	
	/**
	 * close a PDF document
	 * @param dochandle the handle that was returned upon opening the PDF
	 */
	private native void nativeClose(long dochandle);
	
	/**
	 * this cleans up the memory used by this document. The document
	 * cannot be used after calling this!
	 */
	public void close() {
		if(mHandle != 0) {
			this.nativeClose(mHandle);
			mHandle = 0;
			pagecount = 0;
		}
	}
	
	/**
	 * destructor, cleans up memory
	 */
	public void finalize() {
		this.close();
	}
}

/**
 * references a page within a PdfDocument
 */
class PdfPage {
	/**
	 * the rotation that the page wants to apply
	 */
	public int rotate;
	/**
	 * the number of this page
	 */
	public int no;
	/**
	 * the MediaBox for the page, still as float[4]
	 */
	protected float[] mMediabox = {0, 0, 0, 0};

	/**
	 * this will be used to store a C Pointer (ick!) to the
	 * structure holding our references in the native code
	 */
	protected long mHandle = 0;
	
	/**
	 * calls the native code to open a page
	 * @param dochandle the Handle (C pointer) for the document
	 * @param mediabox this will be set to the mediabox of the page
	 * @param no page number to open
	 * @return handle for the opened page
	 */
	private native long nativeOpenPage(long dochandle, float[] mediabox, int no)
		throws PageLoadException;

	/**
	 * constructs a new PdfPage object for a given page in a given document
	 * @param doc the PdfDocument
	 * @param no the number of the page (starting at 1) to open
	 */
	public void open(PdfDocument doc, int no)
			throws PageLoadException
	{
		if(mHandle != 0)
			this.close();
		this.no = no;
		mHandle = this.nativeOpenPage(doc.mHandle, mMediabox, no);
	}
	
	/**
	 * cleans up the memory we claimed in native code
	 * @param pagehandle the handle we got upon opening the page
	 */
	private native void nativeClosePage(long pagehandle);

	/**
	 * clean up memory. Note that the object must not be used afterwards!
	 */
	public void close() {
		if(mHandle != 0) {
			this.nativeClosePage(mHandle);
			mHandle = 0;
			no = 0;
		}
	}
	
	/**
	 * destructor caring for cleaning up memory
	 */
	public void finalize() {
		this.close();
	}
	
	/**
	 * getter for the MediaBox of the opened page
	 * @return RectF holding the MediaBox
	 */
	public RectF getMediaBox() {
		return new RectF(mMediabox[0], mMediabox[1], mMediabox[2], mMediabox[3]);
	}
}

/**
 * Object that controls rendering parts of pages to a int[] pixmap buffer
 */
class PdfView {
	/**
	 * the pixmap we will render to
	 */
	public int[] mBuf;
	
	public final Rect mViewBox = new Rect();

	private int[] mRect = { 0, 0, 0, 0 };
	private float[] mMatrixSource = {
			0, 0, 0,
			0, 0, 0,
			0, 0, 0   };
	private float[] mMatrix = { 0, 0, 0, 0, 0, 0 };
	
	/**
	 * Call native code to render part of a page to a buffer
	 * @param dochandle the handle of the document for which we render
	 * @param pagehandle the handle of the page for which we render
	 * @param viewbox the excerpt that we should render, given as int[4] rectangle
	 * @param matrix the transformation matrix used for rendering, MuPDF format (float[6])
	 * @param buffer the int[] buffer we render to
	 */
	private native void nativeCreateView(
			long dochandle, long pagehandle,
			int[] viewbox, float[] matrix, int[] buffer)
		throws PageRenderException;

	/**
	 * Render part of the page to a int[] buffer
	 * @param page the PdfPage we render for
	 * @param viewbox the excerpt Rect that we should render (coordinates after applying the matrix)
	 * @param matrix the Matrix used for rendering
	 */
	public void render(PdfDocument doc, PdfPage page, Rect viewbox, Matrix matrix) 
			throws PageRenderException
	{
		int size = viewbox.width() * viewbox.height()
				* ((PdfRender.bytesPerPixel * 8) / 32);
		if((mBuf == null) || (mBuf.length != size))
			mBuf = new int[size];
		
		mRect[0] = viewbox.left;
		mRect[1] = viewbox.top;
		mRect[2] = viewbox.right;
		mRect[3] = viewbox.bottom;
		
		matrix.getValues(mMatrixSource);
		mMatrix[0] = mMatrixSource[0];
		mMatrix[1] = mMatrixSource[3];
		mMatrix[2] = mMatrixSource[1];
		mMatrix[3] = mMatrixSource[4];
		mMatrix[4] = mMatrixSource[2];
		mMatrix[5] = mMatrixSource[5];

		this.nativeCreateView(
				doc.mHandle, page.mHandle,
				mRect, mMatrix, mBuf);
		mViewBox.set(viewbox);
	}
}