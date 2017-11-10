package com.studio.makergif.activitys;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
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
import com.studio.makergif.interfaces.ResultComand;
import com.studio.makergif.utils.AnimationTranslate;
import com.studio.makergif.utils.Constant;
import com.studio.makergif.utils.EditVideo;
import com.studio.makergif.utils.ShowAds;
import com.studio.makergif.utils.TrimVideo;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.io.File;
import java.io.IOException;

public class TrimVideoActivity extends AppCompatActivity implements ResultComand {
    private VideoView video;
    private String pathvideo, pathout;
    private ImageView iv_back, img_review;
    private TextView tv_start, tv_end, tv_second_trim, title, tv_save;
    private int duration;
    private RangeSeekBar rangeSeekBar;
    private Runnable r;
    private LinearLayout ln_trim_video, ln_process;
    private RelativeLayout rlads;
    private FFmpeg ffmpeg;
    private EditVideo editVideo;
    private long start, end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trim_video);
        pathvideo = getIntent().getStringExtra(Constant.DATAINTENT);
        ffmpeg = Constant.fFmpeg;
        editVideo = new EditVideo(ffmpeg, this);
        init();
    }

    public void init() {
        rlads = (RelativeLayout) findViewById(R.id.layout_ads);
        ShowAds.showAdsNative(rlads, TrimVideoActivity.this);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        img_review = (ImageView) findViewById(R.id.img_preview);
        tv_save = (TextView) findViewById(R.id.tv_save);
        tv_save.setVisibility(View.GONE);
        iv_back.setOnClickListener(on_click);
        title = (TextView) findViewById(R.id.tv_title);
        title.setText(getString(R.string.cut_video));
        ln_process = (LinearLayout) findViewById(R.id.ln_pro);
        ln_process.setVisibility(View.GONE);
        ln_trim_video = (LinearLayout) findViewById(R.id.ln_trim);
        video = (VideoView) findViewById(R.id.video);
        tv_start = (TextView) findViewById(R.id.tv_start);
        tv_end = (TextView) findViewById(R.id.tv_end);
        tv_second_trim = (TextView) findViewById(R.id.tv_second_cut);
        tv_second_trim.setVisibility(View.GONE);
        rangeSeekBar = (RangeSeekBar) findViewById(R.id.sb_seek);
        ln_trim_video.setOnClickListener(on_click);
        setupvideo();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        AnimationTranslate.previewAnimation(TrimVideoActivity.this);
    }

    private View.OnClickListener on_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_back: {
                    finish();
                    AnimationTranslate.previewAnimation(TrimVideoActivity.this);

                    break;
                }

                case R.id.ln_trim: {
                    ln_process.setVisibility(View.VISIBLE);
                    video.pause();
                    new AsynTrimVideo().execute();
                    break;
                }
            }
        }
    };

    private String getTime(int seconds) {
        int hr = seconds / 3600;
        int rem = seconds % 3600;
        int mn = rem / 60;
        int sec = rem % 60;
        return String.format("%02d", hr) + ":" + String.format("%02d", mn) + ":" + String.format("%02d", sec);
    }

    public void setupvideo() {
        video.setVideoPath(pathvideo);
        rangeSeekBar.setEnabled(false);
        video.start();
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                // TODO Auto-generated method stub
                duration = mp.getDuration() / 1000;
                tv_start.setText("00:00:00");
                tv_end.setText(getTime(mp.getDuration() / 1000));
                mp.setLooping(true);
                rangeSeekBar.setRangeValues(0, duration);
                rangeSeekBar.setSelectedMinValue(0);
                rangeSeekBar.setSelectedMaxValue(duration);
                rangeSeekBar.setEnabled(true);

                rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
                    @Override
                    public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Number minValue, Number maxValue) {
                        video.seekTo((int) minValue * 1000);
                        tv_second_trim.setVisibility(View.VISIBLE);
                        int start = (int) bar.getSelectedMinValue();
                        int end = (int) bar.getSelectedMaxValue();
                        tv_second_trim.setText(getTime(end - start));
                        Log.d("DEBUG", (end - start) + "");
                        tv_start.setText(getTime((int) bar.getSelectedMinValue()));

                        tv_end.setText(getTime((int) bar.getSelectedMaxValue()));

                    }

                });

                final Handler handler = new Handler();
                handler.postDelayed(r = new Runnable() {
                    @Override
                    public void run() {

                        if (video.getCurrentPosition() >= rangeSeekBar.getSelectedMaxValue().intValue() * 1000)
                            video.seekTo(rangeSeekBar.getSelectedMinValue().intValue() * 1000);
                        handler.postDelayed(r, 1000);
                    }
                }, 1000);

            }
        });
    }

    @Override
    public void resultComand(String resutl) {
        ln_process.setVisibility(View.GONE);
        if (resutl.equals("ok")) {
            pathout = Constant.PATHTEMPEDITVIDEO;
            Intent intent = new Intent(TrimVideoActivity.this, EditVideoActivity.class);
            intent.putExtra(Constant.DATAINTENT, pathout);
            startActivity(intent);
            AnimationTranslate.nextAnimation(TrimVideoActivity.this);
        }else {
            Toast.makeText(this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
        }
    }

    public class AsynTrimVideo extends AsyncTask<Void, Void, Uri> {
        @Override
        protected Uri doInBackground(Void... voids) {
            try {
                start = rangeSeekBar.getSelectedMinValue().intValue() * 1000;
                end = rangeSeekBar.getSelectedMaxValue().intValue() * 1000;
                File in = new File(pathvideo);
                File out = new File(Constant.PATHTEMPEDITVIDEO);
                return TrimVideo.startTrim(in, out, start, end);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(Uri uri) {
            super.onPostExecute(uri);
            if (uri != null) {
                pathout= uri.toString();
                MediaPlayer mp = MediaPlayer.create(getBaseContext(), uri);
                int duration = mp.getDuration();
                Log.d("duration", duration + "");
                mp.release();
                if (duration == 0) {
                    editVideo.executeCutVideoCommand((int) start, (int) end, pathvideo, Constant.PATHTEMPEDITVIDEO);
                } else {
                    ln_process.setVisibility(View.GONE);
                    Intent intent = new Intent(TrimVideoActivity.this, EditVideoActivity.class);
                    intent.putExtra(Constant.DATAINTENT, pathout);
                    startActivity(intent);
                    AnimationTranslate.nextAnimation(TrimVideoActivity.this);
                }

            }
            this.cancel(true);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimationTranslate.previewAnimation(TrimVideoActivity.this);
    }

}
