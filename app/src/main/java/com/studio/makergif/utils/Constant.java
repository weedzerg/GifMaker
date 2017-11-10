package com.studio.makergif.utils;

import android.os.Environment;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;

import java.util.ArrayList;


/**
 * Created by DaiPhongPC on 9/8/2017.
 */

public class Constant {
    public static final String PATH = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/video.mp4";
    //    public static final String PATHFOLDER = Environment.getExternalStorageDirectory().getPath() + "/Pictures/FFMPEG";
    //
    public static final String PATHFOLDER = Environment.getExternalStorageDirectory().getPath() + "/GifMaker_Video/";
    public static final String PATHTEMP = Environment.getExternalStorageDirectory().getPath() + "/GifMaker_Video/Temp";
    public static final String PATHMYGIF = Environment.getExternalStorageDirectory().getPath() + "/GifMaker_Video/My gif";
    public static final String PATHTEMPIMAGE = Environment.getExternalStorageDirectory().getPath() + "/GifMaker_Video/Temp/image";
    public static final String PATHTEMPIMAGES = Environment.getExternalStorageDirectory().getPath() + "/GifMaker_Video/Temp/images";
    public static final String PATHTEMPVIDEO = Environment.getExternalStorageDirectory().getPath() + "/GifMaker_Video/Temp/video";
    public static final String PATHTEMPEDITVIDEO = Environment.getExternalStorageDirectory().getPath() + "/GifMaker_Video/Temp/video/EDIT_VIDEO.mp4";
    public static final String PATHTEMPCURVIDEO = Environment.getExternalStorageDirectory().getPath() + "/GifMaker_Video/Temp/video/CUR_VIDEO.mp4";
    public static final String PATHTEMPSCALEVIDEO = Environment.getExternalStorageDirectory().getPath() + "/GifMaker_Video/Temp/video/SCALE_VIDEO.mp4";
    public static final String DATAINTENT = "intentdata";
    public static final String DATAINTENT1 = "intentdata1";
    public static final String PATHCURVE = Environment.getExternalStorageDirectory().getPath() + "/Pictures/curves/cross3.acv";
    public static FFmpeg fFmpeg=null;
    public static ArrayList<String> lspathimg=new ArrayList<>();


}
