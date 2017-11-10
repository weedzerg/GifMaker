package com.studio.makergif.objects;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by DaiPhongPC on 11/6/2017.
 */

public class InfoBitmap implements Serializable {
    private String pathfile;
    private Bitmap bmp;

    public InfoBitmap(String pathfile, Bitmap bmp) {
        this.pathfile = pathfile;
        this.bmp = bmp;
    }

    public String getPathfile() {
        return pathfile;
    }

    public void setPathfile(String pathfile) {
        this.pathfile = pathfile;
    }

    public Bitmap getBmp() {
        return bmp;
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }
}
