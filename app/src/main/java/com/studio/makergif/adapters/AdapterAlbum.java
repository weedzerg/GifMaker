package com.studio.makergif.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.studio.makergif.R;
import com.studio.makergif.objects.InfoImage;

import java.util.ArrayList;

/**
 * Created by DaiPhongPC on 9/12/2017.
 */

public class AdapterAlbum extends BaseAdapter {
    private ArrayList<InfoImage> lsFile;
    private LayoutInflater inflater;
    //true in folder
    //false out folder

    public AdapterAlbum(ArrayList<InfoImage> lsFile, LayoutInflater inflater) {
        this.lsFile = lsFile;
        this.inflater = inflater;
    }

    public ArrayList<InfoImage> getLsFile() {
        return lsFile;
    }

    public void setLsFile(ArrayList<InfoImage> lsFile) {
        this.lsFile = lsFile;
    }

    @Override
    public int getCount() {
        return lsFile.size();
    }

    @Override
    public InfoImage getItem(int i) {
        return lsFile.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    ImageView iv_image;
    TextView tv_nameFile;

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.item_album, null);
        }
        iv_image = (ImageView) view.findViewById(R.id.iv_album);
        tv_nameFile = (TextView) view.findViewById(R.id.tv_album);
        InfoImage item = getItem(i);
        Glide.with(view.getContext())
                .load(item.getPathfile())
                .into(iv_image);
        Log.d("DEBUG", item.getPathfile());
        tv_nameFile.setText(item.getNamefile());
        return view;
    }

}
