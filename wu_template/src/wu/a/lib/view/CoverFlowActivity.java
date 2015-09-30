package wu.a.lib.view;

import wu.a.template.R;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CoverFlowActivity extends Activity {

	/** Called when the activity is first created. */
	CoverFlow cf;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		ViewGroup cv=(ViewGroup) LayoutInflater.from(this).inflate(R.layout.coverflow_layout, null);
		setContentView(cv);

		cf = (CoverFlow) findViewById(R.id.Gallery01);
		cf.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(CoverFlowActivity.this, "position " + position,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				Toast.makeText(CoverFlowActivity.this, "none select",
						Toast.LENGTH_SHORT).show();
			}
		});

		// cf.setBackgroundResource(R.drawable.shape);

		LayoutAdapter layoutAdapter = new LayoutAdapter(this);

		cf.setAdapter(layoutAdapter);
//		cf.setTranslateModeX(true);
//		cf.setTranslateModeY(true);
//		cf.setTranslateModeZ(true);
		cf.setRotationModeY(true);
		cf.setSelection(2, true);
		// cf.setAnimationDuration(1000);

		initImageFlow(cv);
	}

	private void initImageFlow(ViewGroup vg) {
		CoverFlow cf = new CoverFlow(this);
		cf.setBackgroundColor(Color.BLUE);
		Integer[] images = { R.drawable.img0001, R.drawable.img0030,
				R.drawable.img0100, R.drawable.img0130, R.drawable.img0200,
				R.drawable.img0230, R.drawable.img0300, R.drawable.img0330,
				R.drawable.img0354 };

		ImageAdapter adapter = new ImageAdapter(this, images);
		adapter.createReflectedImages();

		cf.setAdapter(adapter);
		cf.setRotationModeY(true);
		vg.addView(cf);
	}
}
class LayoutAdapter extends BaseAdapter {


	private Context mContext;
	
	LayoutInflater lf;
	
	int account=15;

	public LayoutAdapter(Context c) {

		mContext = c;
		lf=(LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {

		return account;

	}

	public Object getItem(int position) {

		return position;

	}

	public long getItemId(int position) {

		return position;

	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView=lf.inflate(R.layout.coverflow_item, null);
			CoverFlow.LayoutParams lp=new CoverFlow.LayoutParams(100, 100);
			convertView.setLayoutParams(lp);
		}
		((TextView)convertView.findViewById(R.id.textView1)).setText("Item"+position);
		((TextView)convertView.findViewById(R.id.textView1)).setSelected(false);
		((TextView)convertView.findViewById(R.id.textView1)).setFocusable(false);
		return convertView;

	}



}
/**�������е�ͼƬ��Դ���ɴ���Ӱ�ĵ�ͼƬ��Ϊ�������ṩ��ȥ��
 * @author junxu.wang
 *
 */
class ImageAdapter extends BaseAdapter {

	int mGalleryItemBackground;
	private Context mContext;
	private Integer[] mImageIds;
	private ImageView[] mImages;

	public ImageAdapter(Context c, Integer[] ImageIds) {
		mContext = c;
		mImageIds = ImageIds;
		mImages = new ImageView[mImageIds.length];
	}

	/**���ش�����ͶӰ��ͼ��
	 * @return
	 */
	public boolean createReflectedImages() {
		/**ͼƬ��ͶӰ֮���϶�߶�*/
		final int reflectionGap = 4;
		int index = 0;

		for (int imageId : mImageIds) {
			//����ͼ��
			Bitmap originalImage = BitmapFactory.decodeResource(mContext.getResources(), imageId);
			int width = originalImage.getWidth();
			int height = originalImage.getHeight();

			//ͶӰ�任��
			Matrix matrix = new Matrix();
			matrix.preScale(1, -1);
			//����ͼ��ͶӰ����Bitmap���°벿��
			Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
					height / 2, width, height / 2, matrix, false);
			//��ͶӰ��ͼ��
			Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
					(height + height / 2), Config.ARGB_8888);

			Canvas canvas = new Canvas(bitmapWithReflection);
			//����ԭ��
			canvas.drawBitmap(originalImage, 0, 0, null);
			//
			Paint deafaultPaint = new Paint();
			canvas.drawRect(0, height, width, height + reflectionGap,
					deafaultPaint);
			//����ͼ��ͶӰ����
			canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

			Paint paint = new Paint();
			//���Խ���
			LinearGradient shader = new LinearGradient(0, originalImage
					.getHeight(), 0, bitmapWithReflection.getHeight()
					+ reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);

			paint.setShader(shader);

			paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));

			canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
					+ reflectionGap, paint);

			ImageView imageView = new ImageView(mContext);
			imageView.setImageBitmap(bitmapWithReflection);
			imageView.setLayoutParams(new CoverFlow.LayoutParams(180, 240));
//			imageView.setScaleType(ScaleType.MATRIX);
			mImages[index++] = imageView;
		}
		return true;
	}

	private Resources getResources() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getCount() {
		return mImageIds.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		return mImages[position];
	}

	public float getScale(boolean focused, int offset) {
		return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
	}

}
