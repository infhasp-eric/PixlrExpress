package com.infhaps.pixlrexpress.view;

import android.graphics.Bitmap;

public class ImageProcess {

    public ImageProcess() {
        // TODO Auto-generated constructor stub
    }

    public Bitmap brighten(int brightenOffset, Bitmap myBitmap) {
        // Create new array
        int width = myBitmap.getWidth();
        int height = myBitmap.getHeight();
        int[] pix = new int[width * height];
        myBitmap.getPixels(pix, 0, width, 0, 0, width, height);

        // Apply pixel-by-pixel change
        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
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

    // filterWidth and filterHeight must be odd numbers
    public Bitmap averageFilter(int filterWidth, int filterHeight, Bitmap myBitmap) {
        // Create new array
        int width = myBitmap.getWidth();
        int height = myBitmap.getHeight();
        int[] pixNew = new int[width * height];
        int[] pixOld = new int[width * height];
        myBitmap.getPixels(pixNew, 0, width, 0, 0, width, height);
        myBitmap.getPixels(pixOld, 0, width, 0, 0, width, height);

        // Apply pixel-by-pixel change
        int filterHalfWidth = filterWidth / 2;
        int filterHalfHeight = filterHeight / 2;
        int filterArea = filterWidth * filterHeight;
        for (int y = filterHalfHeight; y < height - filterHalfHeight; y++) {
            for (int x = filterHalfWidth; x < width - filterHalfWidth; x++) {
                // Accumulate values in neighborhood
                int accumR = 0, accumG = 0, accumB = 0;
                for (int dy = -filterHalfHeight; dy <= filterHalfHeight; dy++) {
                    for (int dx = -filterHalfWidth; dx <= filterHalfWidth; dx++) {
                        int index = (y + dy) * width + (x + dx);
                        accumR += (pixOld[index] >> 16) & 0xff;
                        accumG += (pixOld[index] >> 8) & 0xff;
                        accumB += pixOld[index] & 0xff;
                    } // dx
                } // dy

                // Normalize
                accumR /= filterArea;
                accumG /= filterArea;
                accumB /= filterArea;
                int index = y * width + x;
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

    // filterWidth and filterHeight must be odd numbers
    public Bitmap medianFilter(int filterWidth, int filterHeight, Bitmap myBitmap) {
        // Create new array
        int width = myBitmap.getWidth();
        int height = myBitmap.getHeight();
        int[] pixNew = new int[width * height];
        int[] pixOld = new int[width * height];
        myBitmap.getPixels(pixNew, 0, width, 0, 0, width, height);
        myBitmap.getPixels(pixOld, 0, width, 0, 0, width, height);

        // Apply pixel-by-pixel change
        int filterHalfWidth = filterWidth / 2;
        int filterHalfHeight = filterHeight / 2;
        int filterArea = filterWidth * filterHeight;
        for (int y = filterHalfHeight; y < height - filterHalfHeight; y++) {
            for (int x = filterHalfWidth; x < width - filterHalfWidth; x++) {
                // Accumulate values in neighborhood
                int accumR = 0, accumG = 0, accumB = 0;
                for (int dy = -filterHalfHeight; dy <= filterHalfHeight; dy++) {
                    for (int dx = -filterHalfWidth; dx <= filterHalfWidth; dx++) {
                        int index = (y + dy) * width + (x + dx);
                        accumR += (pixOld[index] >> 16) & 0xff;
                        accumG += (pixOld[index] >> 8) & 0xff;
                        accumB += pixOld[index] & 0xff;
                    } // dx
                } // dy

                // Normalize
                accumR /= filterArea;
                accumG /= filterArea;
                accumB /= filterArea;
                int index = y * width + x;
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
}