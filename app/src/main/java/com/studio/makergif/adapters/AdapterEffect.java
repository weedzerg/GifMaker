package com.studio.makergif.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.studio.makergif.R;
import com.studio.makergif.objects.InfoEffect;

import java.util.ArrayList;

/**
 * Created by DaiPhongPC on 11/3/2017.
 */

public class AdapterEffect extends RecyclerView.Adapter<AdapterEffect.ItemImage> {
    private ArrayList<InfoEffect> lsefect;
    private LayoutInflater inflater;
    private OnClickImage onClickImage;

    public ArrayList<InfoEffect> getLsefect() {
        return lsefect;
    }

    public void setLsefect(ArrayList<InfoEffect> lsefect) {
        this.lsefect = lsefect;
    }

    public AdapterEffect(ArrayList<InfoEffect> lsefect, LayoutInflater inflater, OnClickImage onClickImage) {
        this.lsefect = lsefect;
        this.inflater = inflater;
        this.onClickImage = onClickImage;
    }

    @Override
    public ItemImage onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_effect, parent, false);
        return new ItemImage(itemView);
    }

    @Override
    public void onBindViewHolder(ItemImage holder, final int position) {
        Glide.with(inflater.getContext()).load(lsefect.get(position).getIdeffect()).into(holder.thumbnail);
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickImage.onClickImage(position);
            }
        });
        if (lsefect.get(position).isCheck()) {
            holder.tv.setVisibility(View.VISIBLE);

        } else {
            holder.tv.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return lsefect.size();
    }

    public class ItemImage extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView tv;

        public ItemImage(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.imgeffect);
            tv = (TextView) itemView.findViewById(R.id.tv_background);
        }
    }

    public interface OnClickImage {
        void onClickImage(int position);
    }
}
