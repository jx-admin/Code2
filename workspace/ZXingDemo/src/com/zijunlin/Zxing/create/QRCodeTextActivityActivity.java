package com.zijunlin.Zxing.create;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.zijunlin.Zxing.Demo.CaptureActivity;
import com.zijunlin.Zxing.Demo.R;

public class QRCodeTextActivityActivity extends Activity {
	private static final int SCAN_CODE = 1;
	// 扫描
	TextView result_code_tv;
	Button scan_code_btn;
	// 编码
	EditText decode_et;
	Button btn1 = null;
	Button btn2 = null;
	ImageView ivImageView = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qrcode_text);
		result_code_tv = (TextView) findViewById(R.id.result_code_tv);
		scan_code_btn = (Button) findViewById(R.id.scan_code_btn);
		scan_code_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				result_code_tv.setText("");
				startActivityForResult(
						new Intent(QRCodeTextActivityActivity.this,
								CaptureActivity.class), SCAN_CODE);
			}
		});
		decode_et = (EditText) findViewById(R.id.decode_et);
		btn1 = (Button) findViewById(R.id.button1);// 条形码
		btn2 = (Button) findViewById(R.id.button2);// 二维码
		ivImageView = (ImageView) findViewById(R.id.imageView1);

		btn1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String strconteString = decode_et.getText().toString().trim();
				Bitmap mBitmap = null;
				mBitmap = creatBarcode(QRCodeTextActivityActivity.this,
						strconteString, barcodeFormat, 300, 300, null, true);
				if (mBitmap != null) {
					ivImageView.setImageBitmap(mBitmap);
				}
			}
		});
		btn2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Bitmap mBitmap = null;
				String strconteString = decode_et.getText().toString().trim();
				if (!TextUtils.isEmpty(strconteString)) {
					Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
					hints.put(EncodeHintType.ERROR_CORRECTION,
							ErrorCorrectionLevel.L); //容错率
					// hints.put(EncodeHintType.CHARACTER_SET, "GBK");
					hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");//编码
					hints.put(EncodeHintType.MARGIN, 2);//边框 
					// 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
					mBitmap = creatBarcode(QRCodeTextActivityActivity.this,
							strconteString, BarcodeFormat.QR_CODE, 500, 500,
							hints, true);

					// 获取图标
					Bitmap bm = BitmapFactory.decodeResource(getResources(),
							R.drawable.icon);
					// 缩放图标
					bm = zoomBitmap(bm, 60, 60);
					// 合并二维码与图标
					mBitmap = createBitmap(mBitmap, bm);
					ivImageView.setImageBitmap(mBitmap);
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SCAN_CODE) {
			if (resultCode == RESULT_OK) {
				if (data != null) {
					String code = data
							.getStringExtra(CaptureActivity.SCAN_CODE);
					if (code != null) {
						result_code_tv.setText(code);
					}
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public File GetCodePath(String name) {
		String EXTERN_PATH = null;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED) == true) {
			EXTERN_PATH = android.os.Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/";
			File f = new File(EXTERN_PATH);
			if (!f.exists()) {
				f.mkdirs();
			}
		}
		return new File(EXTERN_PATH + name);
	}

	/**
	 * 条形码的编码类型
	 */
	private BarcodeFormat barcodeFormat = BarcodeFormat.CODE_128;

	/**
	 * 生成条形码
	 * 
	 * @param context
	 * @param contents
	 *            需要生成的内容
	 * @param desiredWidth
	 *            生成条形码的宽带
	 * @param desiredHeight
	 *            生成条形码的高度
	 * @param displayCode
	 *            是否在条形码下方显示内容
	 * @return
	 */
	public Bitmap creatBarcode(Context context, String contents,
			BarcodeFormat format, int desiredWidth, int desiredHeight,
			Map<EncodeHintType, ?> hints, boolean displayCode) {
		Bitmap ruseltBitmap = encodeAsBitmap(contents, format, desiredWidth,
				desiredHeight, hints);
		if (displayCode) {
			Bitmap codeBitmap = string2Bitmap(contents, desiredWidth,
					desiredHeight, context);
			ruseltBitmap = mixtureBitmap(ruseltBitmap, codeBitmap, new PointF(
					(ruseltBitmap.getWidth() - codeBitmap.getWidth()) >> 1,
					desiredHeight));
		}

		return ruseltBitmap;
	}

	/**
	 * 生成显示编码的Bitmap
	 * 
	 * @param contents
	 * @param width
	 * @param height
	 * @param context
	 * @return
	 */
	protected Bitmap string2Bitmap(String contents, int width, int height,
			Context context) {
		TextView tv = new TextView(context);
		tv.setText(contents);
		tv.setGravity(Gravity.CENTER);
		tv.setDrawingCacheEnabled(true);
		tv.setTextColor(Color.BLACK);
		tv.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());

		tv.buildDrawingCache();
		Bitmap bitmapCode = tv.getDrawingCache();
		return bitmapCode;
	}

	/**
	 * 生成条形码的Bitmap
	 * 
	 * @param contents
	 *            需要生成的内容
	 * @param format
	 *            编码格式
	 * @param desiredWidth
	 * @param desiredHeight
	 * @return
	 * @throws WriterException
	 */
	protected Bitmap encodeAsBitmap(String contents, BarcodeFormat format,
			int desiredWidth, int desiredHeight, Map<EncodeHintType, ?> hints) {
		final int WHITE = 0xFFFFFFFF;
		final int BLACK = 0xFF000000;

		MultiFormatWriter writer = new MultiFormatWriter();
		BitMatrix result = null;
		try {
			result = writer.encode(contents, format, desiredWidth,
					desiredHeight, hints);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int width = result.getWidth();
		int height = result.getHeight();
		int[] pixels = new int[width * height];
		// All are 0, or black, by default
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	/**
	 * 将两个Bitmap拼接成一个图片
	 * 
	 * @param first
	 *            原图
	 * @param second
	 *            其他图片
	 * @param fromPoint
	 *            第二个Bitmap开始绘制的起始位置（相对于第一个Bitmap）
	 * @return
	 */
	protected Bitmap mixtureBitmap(Bitmap first, Bitmap second, PointF fromPoint) {
		if (first == null || second == null || fromPoint == null) {
			return null;
		}
		int desW = Math.max(first.getWidth(),
				(int) fromPoint.x + second.getWidth());
		int desH = Math.max(first.getHeight(),
				(int) fromPoint.y + second.getHeight());
		Bitmap newBitmap = Bitmap.createBitmap(desW, desH, Config.ARGB_4444);
		Canvas cv = new Canvas(newBitmap);
		cv.drawBitmap(first, 0, 0, null);
		cv.drawBitmap(second, fromPoint.x, fromPoint.y, null);
		cv.save(Canvas.ALL_SAVE_FLAG);
		cv.restore();

		return newBitmap;
	}

	/**
	 * <pre>
	 *  图片剪切
	 * @param mBitmap 原图
	 * @param r 裁切后的大小
	 * @param config
	 * @return
	 * </pre>
	 */
	public Bitmap cutBitmap(Bitmap mBitmap, Rect r, Bitmap.Config config) {
		int width = r.width();
		int height = r.height();
		Bitmap croppedImage = Bitmap.createBitmap(width, height, config);
		Canvas cvs = new Canvas(croppedImage);
		Rect dr = new Rect(0, 0, width, height);
		cvs.drawBitmap(mBitmap, r, dr, null);
		return croppedImage;
	}

	/***
	 * 以中心对齐方式，覆盖式合并图片
	 * 
	 * @param src
	 *            底图
	 * @param watermark
	 *            合入的图标
	 * @return
	 */
	private Bitmap createBitmap(Bitmap src, Bitmap watermark) {
		String tag = "createBitmap";
		Log.d(tag, "create a new bitmap");
		if (src == null) {
			return null;
		}
		int w = src.getWidth();
		int h = src.getHeight();
		int ww = watermark.getWidth();
		int wh = watermark.getHeight();
		// create the new blank bitmap
		Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		Canvas cv = new Canvas(newb);

		// draw src into
		cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src

		// 在src的中间画watermark
		cv.drawBitmap(watermark, (w - ww) / 2, (h - wh) / 2, null);// 设置ic_launcher的位置

		// save all clip
		cv.save(Canvas.ALL_SAVE_FLAG);// 保存
		// store
		cv.restore();// 存储
		return newb;
	}

	/***
	 * 缩放图片
	 * 
	 * @param src
	 * @param destWidth
	 * @param destHeigth
	 * @return
	 */
	private Bitmap zoomBitmap(Bitmap src, int destWidth, int destHeigth) {
		String tag = "lessenBitmap";
		if (src == null) {
			return null;
		}
		int w = src.getWidth();// 源文件的大小
		int h = src.getHeight();
		// calculate the scale - in this case = 0.4f
		float scaleWidth = ((float) destWidth) / w;// 宽度缩小比例
		float scaleHeight = ((float) destHeigth) / h;// 高度缩小比例
		Log.d(tag, "bitmap width is :" + w);
		Log.d(tag, "bitmap height is :" + h);
		Log.d(tag, "new width is :" + destWidth);
		Log.d(tag, "new height is :" + destHeigth);
		Log.d(tag, "scale width is :" + scaleWidth);
		Log.d(tag, "scale height is :" + scaleHeight);
		Matrix m = new Matrix();// 矩阵
		m.postScale(scaleWidth, scaleHeight);// 设置矩阵比例
		Bitmap resizedBitmap = Bitmap.createBitmap(src, 0, 0, w, h, m, true);// 直接按照矩阵的比例把源文件画入进行
		return resizedBitmap;
	}

}
