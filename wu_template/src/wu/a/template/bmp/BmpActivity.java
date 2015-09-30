package wu.a.template.bmp;

import java.io.File;

import wu.a.template.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;

public class BmpActivity extends Activity{
	private ImageView imageThumbnail;
	private ImageView videoThumbnail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bmp_layout);
		
		Bitmap src=BitmapFactory.decodeResource(getResources(), R.drawable.face);
		
		CircleImageViewA cia=(CircleImageViewA) findViewById(R.id.cia);
		cia.setImageBitmap(src);
		

		CircleImageViewB cib=(CircleImageViewB) findViewById(R.id.cib);
		cib.setImageBitmap(src);
		
		ImageView img=(ImageView) findViewById(R.id.imageView1);
		img.setImageBitmap(ImageUtils.toRoundedCornerBitmap(src,1f));
		

		/**
		 * android视频录制(调用系统视频录制)，生成缩略图
		 * 获取图片和视频的缩略图 这两个方法必须在2.2及以上版本使用，因为其中使用了ThumbnailUtils这个类
		 */
		imageThumbnail = (ImageView) findViewById(R.id.image_thumbnail);
		videoThumbnail = (ImageView) findViewById(R.id.video_thumbnail);

		String imagePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ File.separator
				+ "DCIM"
				+ File.separator
				+ "Camera" + File.separator + "IMG_000003.jpg";

		String videoPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ File.separator
				+ "DCIM"
				+ File.separator
				+ "Camera" + File.separator + "VID_000001.mp4";

		imageThumbnail.setImageBitmap(ImageUtils.getImageThumbnail(imagePath, 160, 160));
		videoThumbnail.setImageBitmap(ImageUtils.getVideoThumbnail(videoPath, 160, 160,
				MediaStore.Images.Thumbnails.MICRO_KIND));
		
		((ImageView) findViewById(R.id.gerZoomRotateBitmap)).setImageBitmap(ImageUtils.gerZoomRotateBitmap(src, 200, 200, 30));
		((ImageView) findViewById(R.id.toGrayscale)).setImageBitmap(ImageUtils.toGrayscale(src));
		
	}

}
