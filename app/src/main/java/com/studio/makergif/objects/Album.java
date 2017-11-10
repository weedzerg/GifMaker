package com.studio.makergif.objects;

import java.util.ArrayList;

/**
 * Created by Peih Gnaoh on 8/20/2017.
 */

public class Album {
    private String idAlbum;
    private String bucket;
    private String pathFirstImage;
    private ArrayList<InfoImage> arrImage;

    public Album(String bucket, String pathFirstImage, ArrayList<InfoImage> arrImage) {
        this.bucket = bucket;
        this.pathFirstImage = pathFirstImage;
        this.arrImage = arrImage;
    }

    public String getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(String idAlbum) {
        this.idAlbum = idAlbum;
    }

    public void addImage(InfoImage image) {
        arrImage.add(image);
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getPathFirstImage() {
        return pathFirstImage;
    }

    public void setPathFirstImage(String pathFirstImage) {
        this.pathFirstImage = pathFirstImage;
    }

    public ArrayList<InfoImage> getArrImage() {
        return arrImage;
    }

    public void setArrImage(ArrayList<InfoImage> arrImage) {
        this.arrImage = arrImage;
    }
}
