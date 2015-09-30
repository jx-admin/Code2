package org.ikags.util;

import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

// // #ifdef Nokia/7610
// // # import com.nokia.mid.ui.DirectGraphics;
// // # import com.nokia.mid.ui.DirectUtils;
// // #endif
/**
 * I.K.A Engine <BR>
 * 创建游戏绘画的类，方便移植和规范。 <BR>
 * 用于创建图片,绘画动画以及相关的内容 <BR>
 * 
 * @author http://airzhangfish.spaces.live.com
 * @since 2005.11.15 最后更新 2008.7.31
 * @version 0.4
 */
public class ImageTools
{

    /**
     * 创建图片时候的缓冲区
     */
    private static Image isload_buffer_image;

    /**
     * 创建一张图片
     * 
     * @param path 图片路径
     */
    public static final Image creatImage(String path)
    {
        isload_buffer_image = null;
        try
        {
            isload_buffer_image = Image.createImage(path);
        }
        catch (IOException e)
        {
            System.out.println(" ika.imageTools.createImage  Image load error:" + path);
            return null;
        }
        return isload_buffer_image;
    }

    /**
     * drawRegion函数封装,用法同2.0,封装目的是根据版本不同,方便移植
     * 
     * @param g
     * @param img
     * @param img_x
     * @param img_y
     * @param img_w
     * @param img_h
     * @param mo
     * @param x
     * @param y
     * @param ach
     * @since MIDP2.0
     */
    public final static void drawRegion(Graphics g, Image img, int img_x, int img_y, int img_w, int img_h, int mo, int x, int y, int ach)
    {
        g.drawRegion(img, img_x, img_y, img_w, img_h, mo, x, y, ach);
    }

    /**
     * 创建一张半透明图片,黑色不显示的半透明,用于创建雪花,萤火虫等有透明感觉的东西
     * 
     * @param Alphalike 默认0x00000000,根据情况修改
     * @param src 图片
     * @since MIDP2.0
     */
    public static final Image CreateAlphaImage(int Alphalike, Image src)
    {
        int w, h;
        w = src.getWidth();
        h = src.getHeight();
        int len = w * h;
        int srcRgb[] = new int[len];
        int dscRgb[] = new int[len];
        try
        {
            src.getRGB(srcRgb, 0, w, 0, 0, w, h);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        for (int i = 0; i < len; i++)
        {
            if (srcRgb[i] == 0xff000000)
            {
                dscRgb[i] = 0x00000000;
            }
            else if (srcRgb[i] < 0xff330000)
            {
                dscRgb[i] = srcRgb[i] + Alphalike - 0xcc000000; // 最接近透明
            }
            else if (srcRgb[i] < 0xff660000)
            {
                dscRgb[i] = srcRgb[i] + Alphalike - 0xaa000000; //
            }
            else if (srcRgb[i] < 0xff990000)
            {
                dscRgb[i] = srcRgb[i] + Alphalike - 0x99000000; //
            }
            else if (srcRgb[i] < 0xffcc0000)
            {
                dscRgb[i] = srcRgb[i] + Alphalike - 0x66000000; //
            }
            else if (srcRgb[i] <= 0xffff0000)
            {
                dscRgb[i] = srcRgb[i] + Alphalike - 0x44000000; //
            }
            else if (srcRgb[i] <= 0xffffffff)
            {
                dscRgb[i] = srcRgb[i] + Alphalike - 0x22000000; //
            }
        }
        Image result = Image.createRGBImage(dscRgb, w, h, true);
        return result;
    }

    /**
     * 创建2张图片颜色减淡的效果(详细效果请参考Photoshop)
     * 
     * @param back 背景图片
     * @param light 光影图片
     * @since MIDP2.0
     */
    public static final Image CreateColorImage(Image back, Image light)
    {
        int w, h;
        w = light.getWidth();
        h = light.getHeight();
        int len = w * h;
        int back_Rgb[] = new int[len];
        int light_Rgb[] = new int[len];
        int dscRgb[] = new int[len];
        try
        {
            back.getRGB(back_Rgb, 0, w, 0, 0, w, h);
            light.getRGB(light_Rgb, 0, w, 0, 0, w, h);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        // a+ab/(255-b)
        for (int i = 0; i < len; i++)
        {
            int R1, R2, G1, G2, B1, B2, RR, GG, BB;
            R1 = (back_Rgb[i] - 0xff000000) >> 16;
            G1 = ((back_Rgb[i] - 0xff000000) - (R1 << 16)) >> 8;
            B1 = (back_Rgb[i] - 0xff000000) - (R1 << 16) - (G1 << 8);
            R2 = (light_Rgb[i] - 0xff000000) >> 16;
            G2 = ((light_Rgb[i] - 0xff000000) - (R2 << 16)) >> 8;
            B2 = (light_Rgb[i] - 0xff000000) - (R2 << 16) - (G2 << 8);
            // 以上运算都正确
            RR = R1 + (R1 * R2) / (255 - R2);
            GG = G1 + (G1 * G2) / (255 - G2);
            BB = B1 + (B1 * B2) / (255 - B2);
            if (RR >= 255)
            {
                RR = 255;
            }
            if (GG >= 255)
            {
                GG = 255;
            }
            if (BB >= 255)
            {
                BB = 255;
            }
            // 以上运算假设正确
            dscRgb[i] = (RR << 16) + (GG << 8) + BB + 0xff000000;
        }
        Image result = Image.createRGBImage(dscRgb, w, h, true);
        return result;
    }

    // int[] NOKIA_back_Rgb;
    // int[] NOKIA_light_Rgb;
    // int[] dscRgb;
    // Image back,light;
    // public void CreateImageNOKIA(Graphics g) {//NOKIA UI的实现方法,创建NOKIA版本的图片
    // int w, h;
    // w = 176;
    // h = 208;
    // int len = w * h;
    // NOKIA_back_Rgb = new int[len];
    // NOKIA_light_Rgb = new int[len];
    // dscRgb = new int[len];
    // DirectGraphics dg = DirectUtils.getDirectGraphics(g);
    // g.drawImage(back,0,0,0);
    // dg.getPixels(NOKIA_back_Rgb, 0, 176, 0, 0, 176, 208, 8888);
    // g.drawImage(light,0,0,0);
    // dg.getPixels(NOKIA_light_Rgb, 0, 176, 0, 0, 176, 208, 8888);
    // //a+ab/(255-b)
    // for (int i = 0; i < len; i++) {
    //
    // int R1, R2, G1, G2, B1, B2, RR, GG, BB;
    // R1 = (NOKIA_back_Rgb[i] - 0xff000000) >> 16;
    // G1 = ( (NOKIA_back_Rgb[i] - 0xff000000) - (R1 << 16)) >> 8;
    // B1 = (NOKIA_back_Rgb[i] - 0xff000000) - (R1 << 16) - (G1 << 8);
    // R2 = (NOKIA_light_Rgb[i] - 0xff000000) >> 16;
    // G2 = ( (NOKIA_light_Rgb[i] - 0xff000000) - (R2 << 16)) >> 8;
    // B2 = (NOKIA_light_Rgb[i] - 0xff000000) - (R2 << 16) - (G2 << 8);
    // //以上运算都正确
    // RR = R1 + (R1 * R2) / (255 - R2);
    // GG = G1 + (G1 * G2) / (255 - G2);
    // BB = B1 + (B1 * B2) / (255 - B2);
    // if (RR >= 255) {
    // RR = 255;
    // }
    // if (GG >= 255) {
    // GG = 255;
    // }
    // if (BB >= 255) {
    // BB = 255;
    // }
    // //以上运算假设正确
    // dscRgb[i] = (RR << 16) + (GG << 8) + BB + 0xff000000;
    // }
    // dg.drawPixels(dscRgb, false, 0, 176, 0, 0, 176, 208, 0, 8888);
    // }
    // public static final void drawRegion(Graphics g, Image image, int transform, int draw_x, int draw_y) {
    // DirectGraphics dg = DirectUtils.getDirectGraphics(g);// NOKIA UI的实现方法(原来绘画地图用)
    // switch (transform) {
    // case 1:
    // dg.drawImage(image, draw_x, draw_y, 0, DirectGraphics.FLIP_VERTICAL);
    // break;
    // case 2:
    // dg.drawImage(image, draw_x, draw_y, 0, DirectGraphics.FLIP_HORIZONTAL);
    // break;
    // case 3:
    // dg.drawImage(image, draw_x, draw_y, 0, DirectGraphics.ROTATE_180);
    // break;
    // case 4:
    // dg.drawImage(image, draw_x, draw_y, 0, DirectGraphics.ROTATE_270 | DirectGraphics.FLIP_HORIZONTAL);
    // break;
    // case 5:
    // dg.drawImage(image, draw_x, draw_y, 0, DirectGraphics.ROTATE_270);
    // break;
    // case 6:
    // dg.drawImage(image, draw_x, draw_y, 0, DirectGraphics.ROTATE_90);
    // break;
    // case 7:
    // dg.drawImage(image, draw_x, draw_y, 0, DirectGraphics.ROTATE_90 | DirectGraphics.FLIP_HORIZONTAL);
    // break;
    // default:
    // dg.drawImage(image, draw_x, draw_y, 0, 0);
    // break;
    // }
    // }
    /**
     * 创建BMP图片.
     */
    public static Image createBMPImage(byte[] imageData)
    {
        if (imageData == null)
        {
            return null;
        }
        if (imageData[0] == 0x42 && imageData[1] == 0x4d)
        {
            // 确定是BMP图片
            int width = byte2int(imageData, 18);
            int height = byte2int(imageData, 22);
            int headersize = byte2int(imageData, 10);
            // 数据开始
            int[] rgb = new int[width * height];
            int count = 0;
            int skip = (4 - ((width * 3) % 4)) % 4;
            System.out.println("BMP图片信息:色位数:" + imageData[28] + ",宽,高:" + width + "," + height + ",头部长度:" + headersize + ",rgb大小:" + rgb.length + ",byte大小:" + imageData.length
                               + ",每行补位:" + skip);
            int startpos = width * height - width;
            try
            {
                for (int i = 0; i < rgb.length; i++)
                {
                    int number = startpos + (i % width) - (i / width) * width;
                    if (i % width == 0 && i != 0)
                    {
                        // 换行补齐4的倍数
                        count = count + skip;
                    }
                    rgb[number] = byte2int(imageData, headersize + count);
                    count = count + 3;
                }
            }
            catch (Exception ex)
            {
                System.out.println("非24位标准BMP图片.");
            }
            // for(int i=0;i<height;i++){
            // for(int j=0;j<width;j++){
            // System.out.print(Integer.toHexString(rgb[i*width+j])+",");
            // }
            // System.out.println();
            // }
            return Image.createRGBImage(rgb, width, height, false);
        }
        return null;
    }

    /**
     * 3个byte拼装成一个int
     */
    private static int byte2int(byte[] imageData, int start)
    {
        int b1 = imageData[start] & 0x000000ff;
        int b2 = (imageData[start + 1] << 8) & 0x0000ff00;
        int b3 = (imageData[start + 2] << 16) & 0x00ff0000;
        return b1 + b2 + b3;
    }
}
