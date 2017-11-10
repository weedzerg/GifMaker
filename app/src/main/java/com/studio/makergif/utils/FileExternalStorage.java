package com.studio.makergif.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;

import com.studio.makergif.objects.InfoImage;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by DaiPhongPC on 9/11/2017.
 */

public class FileExternalStorage {
    public static ArrayList<InfoImage> getAllFileImage(Context context) {
        ArrayList<InfoImage> lsFile = new ArrayList<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATA};
        Cursor c = context.getContentResolver().query(uri, projection, null, null, null);
        int vidsCount = 0;
        if (c != null) {
            vidsCount = c.getCount();
            while (c.moveToNext()) {
                Log.d("IMAGE", "name=" + c.getString(1) + "\t" + c.getString(0));
                String namealbum = c.getString(0);
                String namefile = c.getString(1);
                String namepath = c.getString(2);
                InfoImage infoImage = new InfoImage(namefile, namepath, namealbum);
                if (namepath.endsWith(".GIF") || namepath.endsWith(".gif")) {

                } else {
                    lsFile.add(infoImage);
                }
            }
            c.close();
        }
        return lsFile;
    }

    public static ArrayList<InfoImage> getAllFileImageGif(Context context) {
        ArrayList<InfoImage> lsFile = new ArrayList<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATA};
        Cursor c = context.getContentResolver().query(uri, projection, null, null, null);
        int vidsCount = 0;
        if (c != null) {
            vidsCount = c.getCount();
            while (c.moveToNext()) {
                String namealbum = c.getString(0);
                String namefile = c.getString(1);
                String namepath = c.getString(2);
                InfoImage infoImage = new InfoImage(namefile, namepath, namealbum);
                if (namepath.endsWith(".GIF") || namepath.endsWith(".gif")) {
                    lsFile.add(infoImage);
                    Log.d("IMAGE", "name=" + c.getString(1) + "\t" + c.getString(0));

                }
            }
            c.close();
        }
        return lsFile;
    }

    public static String convertDate(String myDate) {
        // or you already have long value of date, use this instead of milliseconds variable.
        String dateString = null;
        try {
            long millisecond = Long.parseLong(myDate);
            dateString = DateFormat.format("dd/MM/yyyy", new Date(millisecond)).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateString;
    }


}