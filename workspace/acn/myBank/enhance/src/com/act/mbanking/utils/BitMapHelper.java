
package com.act.mbanking.utils;

import com.act.mbanking.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;

public class BitMapHelper {

    public static int LEVEL_COLOR = 0x0a0a0a;

    public static BitmapDrawable getBitmapDrawable(int res, Context context, int level) {

        BitmapDrawable bitmapDrawable = (BitmapDrawable)context.getResources().getDrawable(res);
        Bitmap bitmap = bitmapDrawable.getBitmap();

        // Bitmap newBitmap = Bitmap.createBitmap(bitmap);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int pixels[] = new int[width * height];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        for (int i = 0; i < pixels.length; i++) {
            int color = pixels[i];
            int newColor = pixels[i];
            if (color != Color.TRANSPARENT) {
                LogManager.d("color" + pixels[i]);
                newColor = color + LEVEL_COLOR * level;
                int green = Color.green(newColor);
                if (green < Color.green(color)) {
                    newColor = newColor | 0x0000ff00;
                }
                int red = Color.red(newColor);
                if (red < Color.red(color)) {
                    newColor = newColor | 0x00ff0000;
                }
                int blue = Color.blue(newColor);
                if (blue < Color.blue(color)) {
                    newColor = newColor | 0x000000ff;
                }

                pixels[i] = newColor;

            }
        }
        Bitmap newBitmap = Bitmap.createBitmap(pixels, width, height, Config.ARGB_8888);

        BitmapDrawable bitmapDrawable2 = new BitmapDrawable(newBitmap);
        return bitmapDrawable2;

    }

    public static int getColor(int res, Context context, int level) {

        int color = getColor(res, context);

        int newColor = color + LEVEL_COLOR * level;
        int green = Color.green(newColor);
        if (green < Color.green(color)) {
            newColor = newColor | 0x0000ff00;
        }
        int red = Color.red(newColor);
        if (red < Color.red(color)) {
            newColor = newColor | 0x00ff0000;
        }
        int blue = Color.blue(newColor);
        if (blue < Color.blue(color)) {
            newColor = newColor | 0x000000ff;
        }
        return newColor;
    }

    public static int getColor(int res, Context context) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable)context.getResources().getDrawable(res);
        int width = bitmapDrawable.getBitmap().getWidth();
        int height = bitmapDrawable.getBitmap().getHeight();
        int color = bitmapDrawable.getBitmap().getPixel(width >> 2, height >> 2);
        return color;
    }
}
