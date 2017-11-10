package com.studio.makergif.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.studio.makergif.R;
import com.studio.makergif.adapters.AdapterImage;
import com.studio.makergif.objects.InfoImage;
import com.studio.makergif.utils.AnimationTranslate;
import com.studio.makergif.utils.Constant;
import com.studio.makergif.utils.ShowAds;

import java.util.ArrayList;

public class ImageActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private GridView gdview;
    private ArrayList<InfoImage> lsFile;
    private AdapterImage adapterImage;
    private LinearLayout ln_pro;
    private ImageView iv_back;
    private TextView tv_title, tv_next;
    private RelativeLayout rlads;
    private ArrayList<String> lschoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_video);
        lsFile = new ArrayList<>();
        lschoice = new ArrayList<>();
        lschoice=Constant.lspathimg;
        lsFile = (ArrayList<InfoImage>) getIntent().getSerializableExtra(Constant.DATAINTENT);
        init();
    }

    public void init() {
        rlads = (RelativeLayout) findViewById(R.id.layout_ads);
        ShowAds.showAdsNative(rlads,ImageActivity.this);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.lspathimg=lschoice;
                finish();
                AnimationTranslate.previewAnimation(ImageActivity.this);
            }
        });
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_next = (TextView) findViewById(R.id.tv_save);
        tv_next.setVisibility(View.VISIBLE);
        tv_next.setText(getString(R.string.next));
        tv_title.setText(getString(R.string.images));
        ln_pro = (LinearLayout) findViewById(R.id.ln_pro);
        ln_pro.setVisibility(View.GONE);
        gdview = (GridView) findViewById(R.id.gdview);
        adapterImage = new AdapterImage(lsFile, getLayoutInflater());
        gdview.setAdapter(adapterImage);
        gdview.setOnItemClickListener(this);
        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.lspathimg=lschoice;
                Intent intent = new Intent(ImageActivity.this, EditImageActivity.class);
//                intent.putExtra(Constant.DATAINTENT, lschoice);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        boolean check = adapterImage.getLs().get(i).isCheck();
        if (!check) {
            lschoice.add(adapterImage.getItem(i).getPathfile());
        } else {
            lschoice.remove(adapterImage.getItem(i).getPathfile());
        }
        adapterImage.getLs().get(i).setCheck(!check);
        adapterImage.notifyDataSetChanged();
        Log.d("DEBUG", lschoice.size() + "");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimationTranslate.previewAnimation(ImageActivity.this);
    }
}
