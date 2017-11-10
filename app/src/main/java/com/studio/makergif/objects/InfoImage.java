package com.studio.makergif.objects;

/**
 * Created by DaiPhongPC on 11/4/2017.
 */

public class InfoImage extends InfoFile {
    private String nameAlbum;
    private String idAlbum;
    private String bucket;

    public InfoImage(String namefile, String pathfile, String nameAlbum) {
        super(namefile, pathfile);
        this.nameAlbum = nameAlbum;
    }

    public InfoImage(String namefile, String pathfile, boolean check, String nameAlbum, String idAlbum, String bucket) {
        super(namefile, pathfile, check);
        this.nameAlbum = nameAlbum;
        this.idAlbum = idAlbum;
        this.bucket = bucket;
    }

    public String getNameAlbum() {
        return nameAlbum;
    }

    public void setNameAlbum(String nameAlbum) {
        this.nameAlbum = nameAlbum;
    }

    public String getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(String idAlbum) {
        this.idAlbum = idAlbum;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }
}
