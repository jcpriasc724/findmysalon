package com.findmysalon.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

import com.findmysalon.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

public final class Helper {
    private static final String TAG = "Helper";

    /**
     *  Error message display dialog
     * @param context
     * @param msg
     */
    public static void errorMsgDialog(@NonNull Context context, @StringRes int msg) {
        AlertDialog d = new AlertDialog.Builder(context)
                .setTitle(R.string.warning)
                .setMessage(msg)
                .setPositiveButton(R.string.ok, null)
                .create();

        d.show();
    }

    /**
     *  check file size, return as MB
     * @param fileS
     * @return
     */
    public static int toFileSize(String path) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        int size = 0;
        Bitmap bitmap = Helper.openImage(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        size =  baos.toByteArray().length / 1048576;
//        Log.i(null, String.valueOf(size));
//        if (fileS < 1024) {
//            fileSizeString = df.format((double) fileS) + "B";
//        } else if (fileS < 1048576) {
//            fileSizeString = df.format((double) fileS / 1024) + "KB";
//        } else if (fileS < 1073741824) {
//            fileSizeString = df.format((double) fileS / 1048576) + "MB";
//        } else {
//            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
//        }
        return size;
    }


    /**
     * Rotate the image if the image not in right direction
     * @param path
     * @return
     */
    public static boolean checkAndRotateImg(String path){
        // 1. check degree
        int degree = Helper.getBitmapDegree(path);
//        Log.i(null, String.valueOf(degree));
//        if (degree == 0)
//            return true;
        // 2. bitmap
        Bitmap bitmap = Helper.rotateBitmapByDegree(Helper.openImage(path), degree);
        // 3.
        // save bitmap
        Helper.saveImage(path, bitmap);

        return true;
    }


    /**
     * get image degress
     * @param path
     * @return
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // fetch image EXIF
            ExifInterface exifInterface = new ExifInterface(path);
            // rotation info
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * rotate img
     *
     * @param bm
     * @param degree
     * @return
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // rotate depend on the anyle
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            //
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /**
     * img path to bitmap
     *
     * @param path exited img path
     * @return
     */
    public static Bitmap openImage(String path) {
        Bitmap bitmap = null;
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));
            bitmap = BitmapFactory.decodeStream(bis);
            bis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    /**
     * Bitmap to path
     *
     * @param path   path
     * @param bitmap Bitmap
     */
    public static void saveImage(String path, Bitmap bitmap) {
        try {

            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);

            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}