package com.studio.makergif.activitys;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.studio.makergif.R;
import com.studio.makergif.dialog.RateAppDialog;
import com.studio.makergif.utils.Ads;
import com.studio.makergif.utils.AnimationTranslate;
import com.studio.makergif.utils.Constant;
import com.studio.makergif.utils.Utils;
import com.zer.android.ZAndroidSDK;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout ln_videotogif, ln_imagetogif, ln_gif, ln_rate;
    private static final String[] PERMISSION =
            {Manifest.permission.READ_EXTERNAL_STORAGE
                    , Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private FFmpeg ffmpeg;
    private RateAppDialog rateAppDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        turnPermiss();
    }

    public void turnPermiss() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Utils.checkPermission(PERMISSION, MainActivity.this) == PackageManager.PERMISSION_GRANTED) {
                ZAndroidSDK.init(MainActivity.this);
                init();
            } else {
                MainActivity.this.requestPermissions(PERMISSION, 1);
            }
        } else {
            ZAndroidSDK.init(MainActivity.this);
            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ZAndroidSDK.init(MainActivity.this);
                init();
            } else {
                finish();

            }
        }

    }

    private void loadFFMpegBinary() {
        try {
            if (ffmpeg == null) {
                Log.d("DEBUG", "ffmpeg : era nulo");
                ffmpeg = FFmpeg.getInstance(this);
            }
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
                    Log.d("DEBUG", "ffmpeg : fail Loaded");
                }

                @Override
                public void onSuccess() {
                    Constant.fFmpeg = ffmpeg;
                    Log.d("DEBUG", "ffmpeg : correct Loaded");
                }
            });
        } catch (FFmpegNotSupportedException e) {
        } catch (Exception e) {
            Log.d("DEBUG", "EXception no controlada : " + e);
        }
    }

    public void init() {
        Ads.f(MainActivity.this);
        creatFile();
        ln_videotogif = (LinearLayout) findViewById(R.id.ln_videotogif);
        ln_imagetogif = (LinearLayout) findViewById(R.id.ln_imagetogif);
        ln_gif = (LinearLayout) findViewById(R.id.ln_gif);
        ln_rate = (LinearLayout) findViewById(R.id.ln_rate);
        ln_videotogif.setOnClickListener(this);
        ln_imagetogif.setOnClickListener(this);
        ln_gif.setOnClickListener(this);
        ln_rate.setOnClickListener(this);
        loadFFMpegBinary();
        rateAppDialog = new RateAppDialog(this, new RateAppDialog.OnButtonClicked() {
            @Override
            public void onRateClicked() {
                Utils.rateApp(MainActivity.this);
            }

            @Override
            public void onCancelClicked() {
                finish();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ln_videotogif: {
                Intent intent = new Intent(MainActivity.this, ListVideoActivity.class);
                startActivity(intent);
                AnimationTranslate.nextAnimation(MainActivity.this);
                break;
            }
            case R.id.ln_imagetogif: {
                Intent intent = new Intent(MainActivity.this, AlbumActivity.class);
                startActivity(intent);
                AnimationTranslate.nextAnimation(MainActivity.this);
                break;
            }
            case R.id.ln_gif: {
                Intent intent = new Intent(MainActivity.this, GifActivity.class);
                startActivity(intent);
                AnimationTranslate.nextAnimation(MainActivity.this);
                break;
            }
            case R.id.ln_rate: {
                Utils.rateApp_(getBaseContext());

                break;
            }
        }
    }

    public void creatFile() {
        try {
            copyCurves();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File f = new File(Constant.PATHFOLDER);
        if (!f.exists()) {
            f.mkdirs();
        }
        File f1 = new File(Constant.PATHMYGIF);
        if (!f1.exists()) {
            f1.mkdirs();
        }
        File f2 = new File(Constant.PATHTEMP);
        if (!f2.exists()) {
            f2.mkdirs();
        }
        File f3 = new File(Constant.PATHTEMPIMAGE);
        f3.mkdirs();
        File f4 = new File(Constant.PATHTEMPVIDEO);
        if (!f4.exists()) {
            f4.mkdirs();
        }
    }

    public void copyCurves() throws IOException {
        AssetManager man = getAssets();
        String[] files = man.list("curves");
        File f = new File(getCacheDir() + "/cross1.acv");
        if (f.exists()) {
            Log.d("DEBUG", " exists");
            return;
        } else {
            for (String s : files) {
                File file = new File(getCacheDir() + "/" + s);
                if (!file.exists()) {
                    InputStream is = man.open("curves/" + s);
                    FileUtils.copyInputStreamToFile(is, file);
                    Log.d("DEBUG", s + " created");
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        rateAppDialog.show();
    }
}
