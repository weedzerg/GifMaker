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
import com.studio.makergif.adapters.AdapterVideo;
import com.studio.makergif.objects.InfoFile;
import com.studio.makergif.utils.AnimationTranslate;
import com.studio.makergif.utils.Constant;
import com.studio.makergif.utils.ShowAds;
import com.studio.makergif.utils.Utils;

import java.util.ArrayList;

public class ListVideoActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private GridView listView;
    private ArrayList<InfoFile> lsFile;
    private AdapterVideo adapterVideo;
    private LinearLayout ln_pro;
    private ImageView iv_back;
    private TextView tv_title;
    private RelativeLayout rlads;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_video);
        init();
    }
    public void init() {
        rlads = (RelativeLayout) findViewById(R.id.layout_ads);
        ShowAds.showAdsNative(rlads,ListVideoActivity.this);
        lsFile = new ArrayList<>();
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                AnimationTranslate.previewAnimation(ListVideoActivity.this);
            }
        });
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.my_video));
        ln_pro = (LinearLayout) findViewById(R.id.ln_pro);
        listView = (GridView) findViewById(R.id.gdview);
        adapterVideo = new AdapterVideo(lsFile, getLayoutInflater());
        listView.setAdapter(adapterVideo);
        listView.setOnItemClickListener(this);
        ln_pro.setVisibility(View.VISIBLE);
        new AsynLoadVideo().execute();

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(ListVideoActivity.this, TrimVideoActivity.class);
        intent.putExtra(Constant.DATAINTENT, adapterVideo.getItem(i).getPathfile());
        startActivity(intent);
        AnimationTranslate.nextAnimation(ListVideoActivity.this);

    }

    class AsynLoadVideo extends AsyncTask<Void, Void, ArrayList<InfoFile>> {

        @Override
        protected ArrayList<InfoFile> doInBackground(Void... voids) {
            lsFile = Utils.getAllFileVideo(getBaseContext());
            return lsFile;
        }

        @Override
        protected void onPostExecute(ArrayList<InfoFile> infoFiles) {
            super.onPostExecute(infoFiles);
            ln_pro.setVisibility(View.GONE);
            adapterVideo.setLs(lsFile);
            adapterVideo.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimationTranslate.previewAnimation(ListVideoActivity.this);
    }
}
