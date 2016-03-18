package com.infhaps.pixlrexpress.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Admin on 2016/3/18.
 */
public class BitmapUtils {
    /**
     * 改变bitmap的亮度,  范围为-127~128
     * @param srcBitmap
     * @param brightness  -127~128
     */
    public static Bitmap changeLight(Bitmap srcBitmap, int brightness) {
        int imgWidth = srcBitmap.getWidth();
        int imgHeight = srcBitmap.getHeight();
        Bitmap bmp = Bitmap.createBitmap(imgWidth, imgHeight,
                Bitmap.Config.ARGB_8888);
        //Log.i("PixImg", "progress = " + progress);
        //int brightness = progress - 127;
        ColorMatrix cMatrix = new ColorMatrix();
        cMatrix.set(new float[] { 1, 0, 0, 0, brightness,
                0, 1, 0, 0, brightness,// 改变亮度
                0, 0, 1, 0, brightness,
                0, 0, 0, 1, 0 });

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));

        Canvas canvas = new Canvas(bmp);
        // 在Canvas上绘制一个已经存在的Bitmap。这样，dstBitmap就和srcBitmap一摸一样了
        canvas.drawBitmap(srcBitmap, 0, 0, paint);
        //dstimage.setImageBitmap(bmp);
        return bmp;
    }

    /**
     * 图片变亮
     * @param brightenOffset
     * @param myBitmap
     * @return
     */
    public Bitmap brighten(int brightenOffset,Bitmap myBitmap)
    {
        // Create new array
        int width = myBitmap.getWidth();
        int height = myBitmap.getHeight();
        int[] pix = new int[width * height];
        myBitmap.getPixels(pix, 0, width, 0, 0, width, height);

        // Apply pixel-by-pixel change
        int index = 0;
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                int r = (pix[index] >> 16) & 0xff;
                int g = (pix[index] >> 8) & 0xff;
                int b = pix[index] & 0xff;
                r = Math.max(0, Math.min(255, r + brightenOffset));
                g = Math.max(0, Math.min(255, g + brightenOffset));
                b = Math.max(0, Math.min(255, b + brightenOffset));
                pix[index] = 0xff000000 | (r << 16) | (g << 8) | b;
                index++;
            } // x
        } // y

        // Change bitmap to use new array
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        bitmap.setPixels(pix, 0, width, 0, 0, width, height);
        myBitmap = null;
        pix = null;
        return bitmap;
    }

    /**
     * 中值滤波
     * @param filterWidth
     * @param filterHeight
     * @param myBitmap
     * @return
     */
    // filterWidth and filterHeight must be odd numbers
    public static Bitmap averageFilter(int filterWidth, int filterHeight,Bitmap myBitmap)
    {
        // Create new array
        int width = myBitmap.getWidth();
        int height = myBitmap.getHeight();
        int[] pixNew = new int[width * height];
        int[] pixOld = new int[width * height];
        myBitmap.getPixels(pixNew, 0, width, 0, 0, width, height);
        myBitmap.getPixels(pixOld, 0, width, 0, 0, width, height);

        // Apply pixel-by-pixel change
        int filterHalfWidth = filterWidth/2;
        int filterHalfHeight = filterHeight/2;
        int filterArea = filterWidth * filterHeight;
        for (int y = filterHalfHeight; y < height-filterHalfHeight; y++)
        {
            for (int x = filterHalfWidth; x < width-filterHalfWidth; x++)
            {
                // Accumulate values in neighborhood
                int accumR = 0, accumG = 0, accumB = 0;
                for (int dy = -filterHalfHeight; dy <= filterHalfHeight; dy++)
                {
                    for (int dx = -filterHalfWidth; dx <= filterHalfWidth; dx++)
                    {
                        int index = (y+dy)*width + (x+dx);
                        accumR += (pixOld[index] >> 16) & 0xff;
                        accumG += (pixOld[index] >> 8) & 0xff;
                        accumB += pixOld[index] & 0xff;
                    } // dx
                } // dy

                // Normalize
                accumR /= filterArea;
                accumG /= filterArea;
                accumB /= filterArea;
                int index = y*width + x;
                pixNew[index] = 0xff000000 | (accumR << 16) | (accumG << 8) | accumB;
            } // x
        } // y

        // Change bitmap to use new array
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        bitmap.setPixels(pixNew, 0, width, 0, 0, width, height);
        myBitmap = null;
        pixOld = null;
        pixNew = null;
        return bitmap;
    }

    /**
     * 平滑
     * @param filterWidth
     * @param filterHeight
     * @param myBitmap
     * @return
     */
    // filterWidth and filterHeight must be odd numbers
    public static Bitmap medianFilter(int filterWidth, int filterHeight,Bitmap myBitmap)
    {
        // Create new array
        int width = myBitmap.getWidth();
        int height = myBitmap.getHeight();
        int[] pixNew = new int[width * height];
        int[] pixOld = new int[width * height];
        myBitmap.getPixels(pixNew, 0, width, 0, 0, width, height);
        myBitmap.getPixels(pixOld, 0, width, 0, 0, width, height);

        // Apply pixel-by-pixel change
        int filterHalfWidth = filterWidth/2;
        int filterHalfHeight = filterHeight/2;
        int filterArea = filterWidth * filterHeight;
        for (int y = filterHalfHeight; y < height-filterHalfHeight; y++)
        {
            for (int x = filterHalfWidth; x < width-filterHalfWidth; x++)
            {
                // Accumulate values in neighborhood
                int accumR = 0, accumG = 0, accumB = 0;
                for (int dy = -filterHalfHeight; dy <= filterHalfHeight; dy++)
                {
                    for (int dx = -filterHalfWidth; dx <= filterHalfWidth; dx++)
                    {
                        int index = (y+dy)*width + (x+dx);
                        accumR += (pixOld[index] >> 16) & 0xff;
                        accumG += (pixOld[index] >> 8) & 0xff;
                        accumB += pixOld[index] & 0xff;
                    } // dx
                } // dy

                // Normalize
                accumR /= filterArea;
                accumG /= filterArea;
                accumB /= filterArea;
                int index = y*width + x;
                pixNew[index] = 0xff000000 | (accumR << 16) | (accumG << 8) | accumB;
            } // x
        } // y

        // Change bitmap to use new array
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        bitmap.setPixels(pixNew, 0, width, 0, 0, width, height);
        myBitmap = null;
        pixOld = null;
        pixNew = null;
        return bitmap;
    }

    /** 保存方法 */
    public static String saveBitmap(Bitmap bitmap, String picName) {
        Log.e("Bitmap", "保存图片");
        File f = new File("/sdcard/savepic");
        if (!f.exists()) {
            f.mkdirs();
        }
        try {
            File saveFile = new File("/sdcard/savepic/" + picName);
            FileOutputStream out = new FileOutputStream(saveFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            Log.i("Bitmap", "已经保存");
            return saveFile.getPath();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public static String saveMyBitmap(String bitName,Bitmap mBitmap){
        File saveFile = new File("/sdcard/savePic/");
        if (!saveFile.exists()) {
            saveFile.mkdirs();
        }
        File f = new File("/sdcard/savePic/" + bitName + ".png");
        try {
            f.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("Save", "Out is NUll?" + (fOut == null));
        mBitmap
                .compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  f.getPath();
    }


}