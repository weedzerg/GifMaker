package com.studio.makergif.adapters;

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
 * Created by DaiPhongPC on 11/5/2017.
 */

public class AdapterImage extends BaseAdapter {
    private ArrayList<InfoImage> ls;
    private LayoutInflater inflater;

    public AdapterImage(ArrayList<InfoImage> ls, LayoutInflater inflater) {
        this.ls = ls;
        this.inflater = inflater;
    }

    public ArrayList<InfoImage> getLs() {
        return ls;
    }

    public void setLs(ArrayList<InfoImage> ls) {
        this.ls = ls;
    }

    @Override
    public int getCount() {
        return ls.size();
    }

    @Override
    public InfoImage getItem(int i) {
        return ls.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    ImageView iv_image;
    TextView tv_nameFile, tv_choice;

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.item_video, null);
        }
        iv_image = (ImageView) view.findViewById(R.id.iv_image);
        tv_nameFile = (TextView) view.findViewById(R.id.tv_name_folder);
        tv_choice = (TextView) view.findViewById(R.id.tv_choice);
        tv_nameFile.setVisibility(View.GONE);
        InfoImage item = getItem(i);
        if (item.isCheck()) {
            tv_choice.setVisibility(View.VISIBLE);
        } else {
            tv_choice.setVisibility(View.GONE);
        }
        Glide.with(view.getContext())
                .load(item.getPathfile())
                .into(iv_image);
        return view;
    }
}
