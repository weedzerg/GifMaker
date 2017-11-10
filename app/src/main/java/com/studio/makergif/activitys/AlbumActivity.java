package com.studio.makergif.activitys;

import android.content.Intent;
import android.os.AsyncTask;
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
import com.studio.makergif.adapters.AdapterAlbum;
import com.studio.makergif.objects.InfoImage;
import com.studio.makergif.utils.AnimationTranslate;
import com.studio.makergif.utils.Constant;
import com.studio.makergif.utils.FileExternalStorage;
import com.studio.makergif.utils.ShowAds;

import java.util.ArrayList;
import java.util.HashMap;

public class AlbumActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private GridView gdview;
    private AdapterAlbum adapterAlbum;
    private ArrayList<InfoImage> lsAlbum;
    private HashMap<String, ArrayList<InfoImage>> hashMapAlbum;
    private HashMap<String, String> hashMapAlbum1;
    private RelativeLayout rlads;
    private LinearLayout lnpro;
    private ArrayList<InfoImage> lsimage;
    private ImageView iv_back;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activivy_album);
        init();
    }

    public void init() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                AnimationTranslate.previewAnimation(AlbumActivity.this);
            }
        });
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.album_image));
        rlads = (RelativeLayout) findViewById(R.id.layout_ads);
        ShowAds.showAdsNative(rlads, AlbumActivity.this);
        lnpro = (LinearLayout) findViewById(R.id.ln_pro);
        gdview = (GridView) findViewById(R.id.gd_album);
        hashMapAlbum = new HashMap<>();
        hashMapAlbum1 = new HashMap<>();
        lsAlbum = new ArrayList<>();
        lsimage = new ArrayList<>();
        adapterAlbum = new AdapterAlbum(lsAlbum, getLayoutInflater());
        gdview.setAdapter(adapterAlbum);
        gdview.setOnItemClickListener(this);
        lnpro.setVisibility(View.VISIBLE);
        new AsysLoadData().execute();
//        ShowAds.showAdsNative(adsView,AlbumActivity.this);

    }

    public void initFolder(ArrayList<InfoImage> lsFile) {
        for (int i = 0; i < lsFile.size(); i++) {
            InfoImage infoImage = lsFile.get(i);
            if (hashMapAlbum.containsKey(infoImage.getNameAlbum())) {
                hashMapAlbum.get(infoImage.getNameAlbum()).add(infoImage);
            } else {
                ArrayList<InfoImage> lsin = new ArrayList<>();
                lsin.add(infoImage);
                hashMapAlbum.put(infoImage.getNameAlbum(), lsin);
                hashMapAlbum1.put(infoImage.getNameAlbum(), infoImage.getPathfile());
                Log.d("Album", infoImage.getNameAlbum() + "\t" + infoImage.getPathfile());

            }
        }
        initAlbum();
    }

    private void initAlbum() {
        lsAlbum.clear();
        for (String s1 : hashMapAlbum1.keySet()) {
            lsAlbum.add(new InfoImage(s1, hashMapAlbum1.get(s1), s1));
            Log.d("DEBUG", s1 + "\t" + hashMapAlbum1.get(s1));

        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, ImageActivity.class);
        intent.putExtra(Constant.DATAINTENT, hashMapAlbum.get(adapterAlbum.getItem(i).getNameAlbum()));
        startActivity(intent);
        AnimationTranslate.nextAnimation(AlbumActivity.this);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    class AsysLoadData extends AsyncTask<Void, Void, ArrayList<InfoImage>> {

        @Override
        protected ArrayList<InfoImage> doInBackground(Void... voids) {
            lsimage = FileExternalStorage.getAllFileImage(getBaseContext());
            return lsimage;
        }

        @Override
        protected void onPostExecute(ArrayList<InfoImage> infoImages) {
            super.onPostExecute(infoImages);
            lnpro.setVisibility(View.GONE);
            initFolder(infoImages);
            adapterAlbum.notifyDataSetChanged();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimationTranslate.previewAnimation(AlbumActivity.this);
    }
}
