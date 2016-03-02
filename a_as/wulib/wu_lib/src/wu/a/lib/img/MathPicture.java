package wu.a.lib.img;


import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/**android 图片素描算法 
 * @author junxu.wang
 *
 */
public class MathPicture {
	/**
	 * 原图片去色
	 * 
	 * @param pixels
	 * @param width
	 * @param height
	 * @return
	 */
	int[] getGray(int[] pixels, int width, int height) {
		int gray[] = new int[width * height];
		for (int i = 0; i < width - 1; i++) {
			for (int j = 0; j < height - 1; j++) {
				int index = width * j + i;
				int rgba = pixels[index];
				int g = ((rgba & 0x00FF0000) >> 16) * 3
						+ ((rgba & 0x0000FF00) >> 8) * 6
						+ ((rgba & 0x000000FF)) * 1;
				gray[index] = g / 10;
			}
		}

		return gray;
	}

	/**
	 * 对去色灰度图取反色
	 * 
	 * @param gray
	 * @return
	 */
	public int[] getInverse(int[] gray) {
		int[] inverse = new int[gray.length];

		for (int i = 0, size = gray.length; i < size; i++) {
			inverse[i] = 255 - gray[i];
		}
		return inverse;
	}

	/**
	 * 对反色高斯模糊
	 * 
	 * @param inverse
	 * @param width
	 * @param height
	 * @return
	 */
	public int[] guassBlur(int[] inverse, int width, int height) {
		int[] guassBlur = new int[inverse.length];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int temp = width * (j) + (i);
				if ((i == 0) || (i == width - 1) || (j == 0)
						|| (j == height - 1)) {
					guassBlur[temp] = 0;
				} else {
					int i0 = width * (j - 1) + (i - 1);
					int i1 = width * (j - 1) + (i);
					int i2 = width * (j - 1) + (i + 1);
					int i3 = width * (j) + (i - 1);
					int i4 = width * (j) + (i);
					int i5 = width * (j) + (i + 1);
					int i6 = width * (j + 1) + (i - 1);
					int i7 = width * (j + 1) + (i);
					int i8 = width * (j + 1) + (i + 1);

					int sum = inverse[i0] + 2 * inverse[i1] + inverse[i2] + 2
							* inverse[i3] + 4 * inverse[i4] + 2 * inverse[i5]
							+ inverse[i6] + 2 * inverse[i7] + inverse[i8];

					sum /= 16;

					guassBlur[temp] = sum;
				}
			}
		}
		return guassBlur;
	}

	/**
	 * 对取得高斯灰度值与 去色灰度值 进行颜色减淡混合
	 * 
	 * @param guassBlur
	 * @param gray
	 * @param width
	 * @param height
	 * @return
	 */
	public int[] deceasecolorCompound(int[] guassBlur, int[] gray, int width,
			int height) {
		int a, b, temp;
		float ex;
		int[] output = new int[guassBlur.length];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int index = j * width + i;
				b = guassBlur[index];
				a = gray[index];

				temp = a + a * b / (256 - b);
				ex = temp * temp * 1.0f / 255 / 255;
				temp = (int) (temp * ex);

				a = Math.min(temp, 255);

				output[index] = a;
			}
		}
		return output;
	}

	/**
	 * 根据混合结果灰度值生成图片
	 * 
	 * @param pixels
	 * @param output
	 * @param width
	 * @param height
	 * @return
	 */
	public Bitmap create(int[] pixels, int[] output, int width, int height) {
		for (int i = 0, size = pixels.length; i < size; i++) {
			int gray = output[i];
			int pixel = (pixels[i] & 0xff000000) | (gray << 16) | (gray << 8)
					| gray;// 注意加上原图的 alpha通道

			output[i] = pixel;
		}

		return Bitmap.createBitmap(output, width, height, Config.ARGB_8888);
	}

	/**
	 * 整合
	 * 
	 * @param bitmap
	 * @return
	 */
	public Bitmap createPencli(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int[] pixels = new int[width * height];
		bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

		int[] gray = getGray(pixels, width, height);
		int[] inverse = getInverse(gray);

		int[] guassBlur = guassBlur(inverse, width, height);

		int[] output = deceasecolorCompound(guassBlur, gray, width, height);

		return create(pixels, output, width, height);
	}
}
