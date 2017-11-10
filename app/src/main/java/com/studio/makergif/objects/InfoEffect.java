package com.studio.makergif.objects;

/**
 * Created by DaiPhongPC on 11/4/2017.
 */

public class InfoEffect {
    private int ideffect;
    private boolean check;

    public InfoEffect(int ideffect, boolean check) {
        this.ideffect = ideffect;
        this.check = check;
    }

    public int getIdeffect() {
        return ideffect;
    }

    public void setIdeffect(int ideffect) {
        this.ideffect = ideffect;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
