package com.studio.makergif.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.studio.makergif.objects.InfoFile;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by DaiPhongPC on 9/8/2017.
 */

public class Utils {
    public static int checkPermission(String[] permissions, Context context) {
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (String permission : permissions) {
            permissionCheck += ContextCompat.checkSelfPermission(context, permission);
        }
        return permissionCheck;
    }

    public static void rateApp(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

    public static String convertNameVideo() {
        String s = "";
        long timestamp = Calendar.getInstance().getTimeInMillis();
        DateFormat fomat = new SimpleDateFormat("_MMddyyyy_HHmmss");
        s = "VIDEO_CURVE" + fomat.format(new Date(timestamp)) + ".mp4";
        return s;
    }

    public static String convertNameGIF() {
        String s = "";
        long timestamp = Calendar.getInstance().getTimeInMillis();
        DateFormat fomat = new SimpleDateFormat("_MMddyyyy_HHmmss");
        s = "GIF_" + fomat.format(new Date(timestamp)) + ".gif";
        return s;
    }

    public static ArrayList<InfoFile> getAllFileVideo(Context context) {
        ArrayList<InfoFile> lsFile = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.VideoColumns.DATA};
        Cursor c = context.getContentResolver().query(uri, projection, null, null, null);
        int vidsCount = 0;
        if (c != null) {
            while (c.moveToNext()) {
                String namefile = c.getString(0);
                String namepath = c.getString(1);
                lsFile.add(new InfoFile(namefile, namepath, false));
            }
            c.close();
        }
        return lsFile;
    }

    public static void rateApp_(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

    public static Bitmap getBitmapFromLocalPath(String path, int sampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;
        Log.d("PATH", path);
        return BitmapFactory.decodeFile(path, options);
    }

    public static int getDirBitmap(String path) {
        ExifInterface exif = null;
        int rotate = 0;
        try {
            exif = new ExifInterface(path);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotate;
    }

    public static Bitmap makeScaled(int size, Bitmap src, String path) {
        Bitmap output = null;
        int dir = getDirBitmap(path);
        try {
            int width = src.getWidth();
            int height = src.getHeight();
            // 480  889
            //
            float scale = (float) size / height;
            float scale1 = (float) size / width;

            float scaledWidth = width * scale1;
            float scaledHeight = height * scale;
            Matrix m = new Matrix();
            m.setRectToRect(new RectF(0, 0, src.getWidth(), src.getHeight()), new RectF(0, 0, scaledWidth, scaledHeight), Matrix.ScaleToFit.CENTER);
            m.postRotate(dir);
            output = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), m, true);
            Canvas xfas = new Canvas();
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            paint.setDither(true);
            xfas.drawBitmap(output, 0, 0, paint);
            return output;
        } catch (Exception e) {
            e.printStackTrace();
            return src;
        }


    }
}
