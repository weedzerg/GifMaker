package com.studio.makergif.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.studio.makergif.R;
import com.studio.makergif.adapters.AdapterEffect;
import com.studio.makergif.dialog.QualtyDialog;
import com.studio.makergif.dialog.XacThucAppDialog;
import com.studio.makergif.gpuimage.GPUImage;
import com.studio.makergif.gpuimage.GPUImageToneCurveFilter;
import com.studio.makergif.interfaces.ResultComand;
import com.studio.makergif.interfaces.UpdateProcessing;
import com.studio.makergif.objects.InfoEffect;
import com.studio.makergif.utils.AnimationTranslate;
import com.studio.makergif.utils.Constant;
import com.studio.makergif.utils.EditVideo;
import com.studio.makergif.utils.ShowAds;
import com.studio.makergif.utils.Utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class EditVideoActivity extends AppCompatActivity implements ResultComand,
        AdapterEffect.OnClickImage,
        UpdateProcessing {
    private VideoView video;
    private String pathvideo, pathout;
    private ImageView iv_back, img_review;
    private TextView title, tv_apply, tv_save, txtupdate;
    private int duration;
    private Runnable r;
    private LinearLayout ln_effect, ln_process;
    private RelativeLayout rlads;
    private FFmpeg ffmpeg;
    private EditVideo editVideo;
    private ArrayList<InfoEffect> lseffect;
    private RecyclerView rcview;
    private AdapterEffect adapter;
    private boolean check = true;
    private int with, height;
    private String pathcuver;
    private Bitmap thuml;
    private XacThucAppDialog diag;
    private String size;
    private QualtyDialog qualtyDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_video);
        pathvideo = getIntent().getStringExtra(Constant.DATAINTENT);
        ffmpeg = Constant.fFmpeg;
        editVideo = new EditVideo(ffmpeg, this, this);
        lseffect = new ArrayList<>();
        loadEffect();
        init();
    }

    public void init() {
        qualtyDialog = new QualtyDialog(EditVideoActivity.this, new QualtyDialog.OnButtonClicked() {
            @Override
            public void onOkClicked(boolean check) {
                ln_process.setVisibility(View.VISIBLE);
                video.pause();
                pathout = Constant.PATHMYGIF + "/" + Utils.convertNameGIF();
                if (with > 640) {
                    float scale = (float) 640 / with;
                    int w = (int) ((float) scale * with);
                    int h = (int) ((float) scale * height);
                    size = w + ":" + h;
                } else {
                    size = with + ":" + height;
                }
                Log.d("SIZE", size);
                if (check) {
                    //chat luong cao
                    editVideo.excuteVideotoGifHeight(pathvideo, pathout, size);
                } else {
                    //chat luong thap
                    editVideo.excuteVideotoGif(pathvideo, pathout, size);
                }
            }
        });
        diag = new XacThucAppDialog(EditVideoActivity.this, new XacThucAppDialog.OnButtonClicked() {
            @Override
            public void onOkClicked() {
                finish();
                AnimationTranslate.previewAnimation(EditVideoActivity.this);
            }

            @Override
            public void onCancelClicked() {

            }
        });
        rcview = (RecyclerView) findViewById(R.id.rc_effect);
        rcview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new AdapterEffect(lseffect, getLayoutInflater(), this);
        adapter.setHasStableIds(true);
        rcview.setAdapter(adapter);

        rlads = (RelativeLayout) findViewById(R.id.layout_ads);
        ShowAds.showAdsNative(rlads, EditVideoActivity.this);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        img_review = (ImageView) findViewById(R.id.img_preview);
        tv_apply = (TextView) findViewById(R.id.tv_apply);
        tv_save = (TextView) findViewById(R.id.tv_save);
        txtupdate = (TextView) findViewById(R.id.txt);
        tv_apply.setVisibility(View.GONE);
        tv_save.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(on_click);
        tv_save.setOnClickListener(on_click);
        tv_apply.setOnClickListener(on_click);
        title = (TextView) findViewById(R.id.tv_title);
        title.setText(getString(R.string.edit_video));
        ln_process = (LinearLayout) findViewById(R.id.ln_pro);
        ln_process.setVisibility(View.GONE);
        ln_effect = (LinearLayout) findViewById(R.id.ln_effect);
        video = (VideoView) findViewById(R.id.video);
        ln_effect.setOnClickListener(on_click);
        setupvideo();
        showhideView(check);

    }

    private View.OnClickListener on_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_save: {
                    qualtyDialog.show();
                    break;
                }
                case R.id.iv_back: {
                    diag.show();
                    break;
                }
                case R.id.tv_apply: {
                    if (!pathcuver.equals("")) {
                        if (video.isPlaying()) {
                            video.pause();
                        }
                        ln_process.setVisibility(View.VISIBLE);
                        if (!check) {
                            //add effect
                            pathout = Constant.PATHTEMPCURVIDEO;
//                        pathout = Constant.PATHFOLDER + "palette.png";
//                        editVideo.excuteVideotoGifHeight(pathvideo, pathout);
                            editVideo.addCurveintoVideo(pathvideo, pathcuver, pathout);
                        }
                        check = true;
                    } else {
                        Toast.makeText(EditVideoActivity.this, getString(R.string.noeffect), Toast.LENGTH_SHORT).show();
                    }

                    break;
                }
                case R.id.ln_effect: {
                    check = !check;
                    thuml = ThumbnailUtils.createVideoThumbnail(pathvideo,
                            MediaStore.Images.Thumbnails.MINI_KIND);
                    showhideView(check);
                    break;
                }
            }
        }
    };

    public void showhideView(boolean check) {
        if (check) {
            video.setVisibility(View.VISIBLE);
            if (!video.isPlaying()) {
                setupvideo();
            }
            tv_apply.setVisibility(View.GONE);
            img_review.setVisibility(View.GONE);
            rcview.setVisibility(View.GONE);
            tv_save.setVisibility(View.VISIBLE);
        } else {
            if (video.isPlaying()) {
                video.pause();
            }
            tv_apply.setVisibility(View.VISIBLE);
            img_review.setVisibility(View.VISIBLE);
            video.setVisibility(View.INVISIBLE);
            rcview.setVisibility(View.VISIBLE);
            tv_save.setVisibility(View.GONE);
            img_review.setImageBitmap(thuml);
        }
    }

    public void setupvideo() {
        video.setVideoPath(pathvideo);
        video.start();
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                // TODO Auto-generated method stub
                with = mp.getVideoWidth();
                height = mp.getVideoHeight();
                Log.d("DEBUG", with + "\t" + height);
                duration = mp.getDuration() / 1000;
                mp.setLooping(true);
            }
        });
    }


    @Override
    public void resultComand(String resutl) {

        if (resutl.equals("ok")) {
            if (pathout.endsWith(".gif")) {
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.fromFile(new File(pathout))));
                Intent intent = new Intent(EditVideoActivity.this, SlideGifActivity.class);
                intent.putExtra(Constant.DATAINTENT, pathout);
                intent.putExtra(Constant.DATAINTENT1, 0);
                startActivity(intent);
                AnimationTranslate.nextAnimation(EditVideoActivity.this);
            } else {
                ln_process.setVisibility(View.GONE);
                check = true;
                showhideView(check);
                pathvideo = pathout;
                video.setVideoPath(pathvideo);
                video.start();
            }
        } else {
            ln_process.setVisibility(View.GONE);
            Toast.makeText(this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClickImage(int position) {
        for (InfoEffect f : lseffect) {
            f.setCheck(false);
        }
        lseffect.get(position).setCheck(true);
        adapter.notifyDataSetChanged();
        pathcuver = getCacheDir() + "/cross " + position + ".acv";
        Bitmap bmp = thuml;
        InputStream is = null;
        try {
            is = FileUtils.openInputStream(new File(pathcuver));
            GPUImageToneCurveFilter filter = new GPUImageToneCurveFilter();
            try {
                filter.setFromCurveFileInputStream(is);
                is.close();
            } catch (IOException e) {
                Log.e("MainActivity", "Error");
            }
// Use GPUImage processed image
            GPUImage gpuImage = new GPUImage(getBaseContext());
            gpuImage.setImage(bmp);
            gpuImage.setFilter(filter);
            bmp = gpuImage.getBitmapWithFilterApplied();
// Later in the ImageView image display processing
            img_review.setImageBitmap(bmp);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        diag.show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ln_process.setVisibility(View.GONE);
        check = true;
        showhideView(check);
    }

    public void loadEffect() {

        lseffect.add(new InfoEffect(R.drawable.cross1, false));
        lseffect.add(new InfoEffect(R.drawable.cross2, false));
        lseffect.add(new InfoEffect(R.drawable.cross3, false));
        lseffect.add(new InfoEffect(R.drawable.cross4, false));
        lseffect.add(new InfoEffect(R.drawable.cross5, false));
        lseffect.add(new InfoEffect(R.drawable.cross6, false));
        lseffect.add(new InfoEffect(R.drawable.cross7, false));
        lseffect.add(new InfoEffect(R.drawable.cross8, false));
        lseffect.add(new InfoEffect(R.drawable.cross9, false));
        lseffect.add(new InfoEffect(R.drawable.cross10, false));
        lseffect.add(new InfoEffect(R.drawable.cross11, false));
        lseffect.add(new InfoEffect(R.drawable.cross12, false));
        lseffect.add(new InfoEffect(R.drawable.cross13, false));
        lseffect.add(new InfoEffect(R.drawable.cross14, false));
        lseffect.add(new InfoEffect(R.drawable.cross15, false));
        lseffect.add(new InfoEffect(R.drawable.cross16, false));
        lseffect.add(new InfoEffect(R.drawable.cross17, false));
        lseffect.add(new InfoEffect(R.drawable.cross18, false));
        lseffect.add(new InfoEffect(R.drawable.cross19, false));
        lseffect.add(new InfoEffect(R.drawable.cross20, false));
    }

    @Override
    public void updateUI(String s) {
        txtupdate.setText("Đang xử lý " + s);
    }
}
