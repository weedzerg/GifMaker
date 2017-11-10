package com.studio.makergif.activitys;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.studio.makergif.R;
import com.studio.makergif.adapters.AdapterImageGif;
import com.studio.makergif.objects.InfoImage;
import com.studio.makergif.utils.AnimationTranslate;
import com.studio.makergif.utils.Constant;
import com.studio.makergif.utils.FileExternalStorage;
import com.studio.makergif.utils.ShowAds;

import java.util.ArrayList;

public class GifActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private GridView gdview;
    private ArrayList<InfoImage> lsFile;
    private AdapterImageGif adapterImage;
    private LinearLayout ln_pro;
    private ImageView iv_back;
    private TextView tv_title, tv_next;
    private RelativeLayout rlads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_video);
        lsFile = new ArrayList<>();
        init();
    }

    public void init() {
        rlads = (RelativeLayout) findViewById(R.id.layout_ads);
        ShowAds.showAdsNative(rlads,GifActivity.this);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                AnimationTranslate.previewAnimation(GifActivity.this);
            }
        });
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_next = (TextView) findViewById(R.id.tv_save);
        tv_next.setVisibility(View.VISIBLE);
        tv_next.setText(getString(R.string.next));
        tv_next.setVisibility(View.GONE);
        tv_title.setText(getString(R.string.my_gif));
        ln_pro = (LinearLayout) findViewById(R.id.ln_pro);
        gdview = (GridView) findViewById(R.id.gdview);
        adapterImage = new AdapterImageGif(lsFile, getLayoutInflater());
        gdview.setAdapter(adapterImage);
        gdview.setOnItemClickListener(this);
        new AsysLoadData().execute();


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(GifActivity.this, SlideGifActivity.class);
        intent.putExtra(Constant.DATAINTENT, adapterImage.getItem(i).getPathfile());
        startActivity(intent);
    }

    class AsysLoadData extends AsyncTask<Void, Void, ArrayList<InfoImage>> {

        @Override
        protected ArrayList<InfoImage> doInBackground(Void... voids) {
            lsFile = FileExternalStorage.getAllFileImageGif(getBaseContext());
            return lsFile;
        }

        @Override
        protected void onPostExecute(ArrayList<InfoImage> infoImages) {
            super.onPostExecute(infoImages);
            ln_pro.setVisibility(View.GONE);
            adapterImage.setLs(infoImages);
            adapterImage.notifyDataSetChanged();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimationTranslate.previewAnimation(GifActivity.this);
    }
}