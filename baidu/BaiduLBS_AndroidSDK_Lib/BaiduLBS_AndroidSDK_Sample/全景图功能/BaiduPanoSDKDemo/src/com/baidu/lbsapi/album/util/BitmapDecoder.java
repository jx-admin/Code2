package com.baidu.lbsapi.album.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * MSc project
 * <p/>
 * This class is reference to Google developer guide:
 * "Loading Large Bitmaps Efficiently" url:
 * http://developer.android.com/training/displaying-bitmaps/load-bitmap.html
 * <p/>
 * Created by Ziji Wang on 13-7-15.
 */
public class BitmapDecoder {

    private int mTargetW, mTargetH;
    private BitmapFactory.Options mOptions;

    public BitmapDecoder(int tarWidth, int tarHeight) {
        mTargetW = tarWidth;
        mTargetH = tarHeight;

    }

    public int calculateInSampleSize(BitmapFactory.Options options) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > mTargetH || width > mTargetW) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) mTargetH);
            final int widthRatio = Math.round((float) width / (float) mTargetW);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    private synchronized Bitmap setOption(InputStream in) throws Exception {
        if (mOptions == null) {
            byte[] byt = transformInputstream(in);
            mOptions = new BitmapFactory.Options();
            // First decode with inJustDecodeBounds=true to check dimensions
            mOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(byt, 0, byt.length, mOptions);
            // Calculate inSampleSize
            mOptions.inSampleSize = calculateInSampleSize(mOptions);
            mOptions.inJustDecodeBounds = false;
            mOptions.outWidth = mTargetW;
            mOptions.outHeight = mTargetH;
            mOptions.inPreferredConfig = Bitmap.Config.ARGB_4444;
            mOptions.inPurgeable = true;
            mOptions.inInputShareable = true;
            return BitmapFactory.decodeByteArray(byt, 0, byt.length, mOptions);
        } else {
            return null;
        }
    }

    private byte[] transformInputstream(InputStream input) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int b = 0;
        b = input.read();
        while (b != -1) {
            baos.write(b);
            b = input.read();
        }
        return baos.toByteArray();
    }

    public Bitmap decodeSampledBitmapFromInputStream(InputStream in) {
        try {
            Bitmap b = setOption(in);
            if (b == null) {
                return BitmapFactory.decodeStream(in, null, mOptions);
            } else {
                return b;
            }
        } catch (Exception e) {
            return BitmapFactory.decodeStream(in, null, mOptions);
        }
    }

    public Bitmap decodeSampledBitmapFromFile(String path) {
        Bitmap b = setOption(path);
        if (b == null) {
            return BitmapFactory.decodeFile(path, mOptions);
        } else {
            return b;
        }

    }

    private Bitmap setOption(String path) {
        if (mOptions == null) {
            mOptions = new BitmapFactory.Options();
            // First decode with inJustDecodeBounds=true to check dimensions
            mOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, mOptions);
            // Calculate inSampleSize
            mOptions.inSampleSize = calculateInSampleSize(mOptions);
            mOptions.inJustDecodeBounds = false;
            mOptions.outWidth = mTargetW;
            mOptions.outHeight = mTargetH;
            mOptions.inPreferredConfig = Bitmap.Config.ARGB_4444;
            mOptions.inPurgeable = true;
            mOptions.inInputShareable = true;
            return BitmapFactory.decodeFile(path, mOptions);
        } else {
            return null;
        }
    }

}