package com.studio.makergif.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.studio.makergif.R;
import com.studio.makergif.objects.InfoFile;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by DaiPhongPC on 9/12/2017.
 */

public class AdapterVideo extends BaseAdapter {
    private ArrayList<InfoFile> ls;
    private LayoutInflater inflater;

    public AdapterVideo(ArrayList<InfoFile> ls, LayoutInflater inflater) {
        this.ls = ls;
        this.inflater = inflater;
    }

    public ArrayList<InfoFile> getLs() {
        return ls;
    }

    public void setLs(ArrayList<InfoFile> ls) {
        this.ls = ls;
    }

    @Override
    public int getCount() {
        return ls.size();
    }

    @Override
    public InfoFile getItem(int i) {
        return ls.get(i);
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
            view = inflater.inflate(R.layout.item_video, null);
        }
        iv_image = (ImageView) view.findViewById(R.id.iv_image);
        tv_nameFile = (TextView) view.findViewById(R.id.tv_name_folder);

        InfoFile item = getItem(i);
        tv_nameFile.setText(item.getNamefile());
        Glide.with(view.getContext())
                .load(Uri.fromFile(new File(item.getPathfile())))
                .into(iv_image);
        return view;
    }
}
