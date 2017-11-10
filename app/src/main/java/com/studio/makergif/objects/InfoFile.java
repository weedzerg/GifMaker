package com.studio.makergif.objects;

import java.io.Serializable;

/**
 * Created by DaiPhongPC on 9/11/2017.
 */

public class InfoFile implements Serializable {
    private String namefile;
    private String pathfile;
    private boolean check;

    public InfoFile(String namefile, String pathfile) {
        this.namefile = namefile;
        this.pathfile = pathfile;
    }

    public InfoFile(String namefile, String pathfile, boolean check) {
        this.namefile = namefile;
        this.pathfile = pathfile;
        this.check = check;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getNamefile() {
        return namefile;
    }

    public void setNamefile(String namefile) {
        this.namefile = namefile;
    }

    public String getPathfile() {
        return pathfile;
    }

    public void setPathfile(String pathfile) {
        this.pathfile = pathfile;
    }
}
