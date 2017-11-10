package com.studio.makergif.activitys;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.studio.makergif.R;
import com.studio.makergif.utils.AnimationTranslate;
import com.studio.makergif.utils.Constant;
import com.studio.makergif.utils.ShowAds;

import java.io.File;
import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;

public class SlideGifActivity extends AppCompatActivity {
    private ImageView g, g1;
    private LinearLayout ln_fb, ln_is;
    private String pathgif;
    private byte[] b;
    private String typeimage = "image/*";
    private ImageView iv_back, iv_share;
    private TextView tv_title;
    private RelativeLayout rlads;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_gif);
        pathgif = getIntent().getStringExtra(Constant.DATAINTENT);
        type = getIntent().getIntExtra(Constant.DATAINTENT1, 0);
//        pathgif = Constant.PATHTEMPVIDEO+"/"+"DQpgw.gif";
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void init() {
        rlads = (RelativeLayout) findViewById(R.id.layout_ads);
        ShowAds.showAdsNative(rlads, SlideGifActivity.this);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_share = (ImageView) findViewById(R.id.iv_share);
        iv_share.setVisibility(View.VISIBLE);
        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creatShare(typeimage, pathgif);
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                AnimationTranslate.previewAnimation(SlideGifActivity.this);
            }
        });
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.slide_gif));
        g = (ImageView) findViewById(R.id.gif_img);
        g1 = (ImageView) findViewById(R.id.iv_gif_max);
        ln_fb = (LinearLayout) findViewById(R.id.ln_fb);
        ln_is = (LinearLayout) findViewById(R.id.ln_is);
        ln_fb.setOnClickListener(on_click);
        ln_is.setOnClickListener(on_click);
        if (type == 1) {
            g1.setVisibility(View.VISIBLE);
            g.setVisibility(View.GONE);
        } else {
            g.setVisibility(View.VISIBLE);
            g1.setVisibility(View.GONE);
        }
        try {
            File gifFile = new File(pathgif);
            GifDrawable gifFromFile = new GifDrawable(gifFile);
            if (type == 1) {
                g1.setBackground(gifFromFile);

            } else {
                g.setBackground(gifFromFile);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Glide.with(getBaseContext()).asGif().load(pathgif).into(g);
    }

    private View.OnClickListener on_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ln_fb: {
                    if (isPackageInstalled("com.facebook.katana")) {
                        creatFacebook(typeimage, pathgif);
                    } else {
                        Toast.makeText(SlideGifActivity.this, getString(R.string.fb_notavainle), Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case R.id.ln_is: {
                    if (isPackageInstalled("com.instagram.android")) {
                        creatInInstagram(typeimage, pathgif);
                    } else {
                        Toast.makeText(SlideGifActivity.this, getString(R.string.is_notavaible), Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        }
    };

    private boolean isPackageInstalled(String packagename) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void creatInInstagram(String type, String mediaPath) {
        // Create the new Intent using the 'Send' action.
        Intent share = new Intent(Intent.ACTION_SEND);

        // Set the MIME type
        share.setType(type);
        share.setPackage("com.instagram.android");

        // Create the URI from the media
        File media = new File(mediaPath);
        Uri uri = Uri.fromFile(media);

        // Add the URI to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, uri);

        // Broadcast the Intent.
        startActivity(Intent.createChooser(share, "Share to"));

    }

    public void creatShare(String type, String mediaPath) {
        // Create the new Intent using the 'Send' action.
        Intent share = new Intent(Intent.ACTION_SEND);

        // Set the MIME type
        share.setType(type);

        // Create the URI from the media
        File media = new File(mediaPath);
        Uri uri = Uri.fromFile(media);

        // Add the URI to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, uri);

        // Broadcast the Intent.
        startActivity(Intent.createChooser(share, "Share to"));

    }

    public void creatFacebook(String type, String mediaPath) {
        // Create the new Intent using the 'Send' action.
        Intent share = new Intent(Intent.ACTION_SEND);

        // Set the MIME type
        share.setType(type);
        share.setPackage("com.facebook.katana");

        // Create the URI from the media
        File media = new File(mediaPath);
        Uri uri = Uri.fromFile(media);

        // Add the URI to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, uri);

        // Broadcast the Intent.
        startActivity(Intent.createChooser(share, "Share to"));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimationTranslate.previewAnimation(SlideGifActivity.this);
    }
}
