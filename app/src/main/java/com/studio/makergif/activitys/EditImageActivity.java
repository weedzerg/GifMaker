package com.studio.makergif.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.studio.makergif.R;
import com.studio.makergif.adapters.AdapterEffect;
import com.studio.makergif.dialog.QualtyDialog;
import com.studio.makergif.dialog.XacThucAppDialog;
import com.studio.makergif.gpuimage.GPUImage;
import com.studio.makergif.gpuimage.GPUImageToneCurveFilter;
import com.studio.makergif.interfaces.ResultComand;
import com.studio.makergif.objects.InfoBitmap;
import com.studio.makergif.objects.InfoEffect;
import com.studio.makergif.utils.AnimatedGIFWriter;
import com.studio.makergif.utils.AnimationTranslate;
import com.studio.makergif.utils.Constant;
import com.studio.makergif.utils.EditVideo;
import com.studio.makergif.utils.ShowAds;
import com.studio.makergif.utils.Utils;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class EditImageActivity extends AppCompatActivity implements AdapterEffect.OnClickImage, ResultComand {
    private ArrayList<InfoEffect> lseffect;
    private RecyclerView rcview;
    private AdapterEffect adapter;
    private ImageView img;
    private LinearLayout ln_effect, ln_process;
    private ImageView iv_back;
    private TextView title, tv_apply, tv_frame, tv_save;
    private Bitmap thuml;
    private String pathcurves, pathout;
    private ArrayList<String> lsFileImage;
    private ArrayList<InfoBitmap> lsBitmap;
    private FFmpeg ffmpeg;
    private EditVideo editVideo;
    private boolean check = false;
    private QualtyDialog qualtyDialog;
    private Runnable r;
    private DiscreteSeekBar delay;
    private XacThucAppDialog diag;
    private RelativeLayout rlads;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);
        title = (TextView) findViewById(R.id.tv_title);
        title.setText(getString(R.string.edit_image));
        lseffect = new ArrayList<>();
        ffmpeg = Constant.fFmpeg;
        editVideo = new EditVideo(ffmpeg, this);
        loadEffect();
        lsFileImage = Constant.lspathimg;
        lsBitmap = new ArrayList<>();
        ln_process = (LinearLayout) findViewById(R.id.ln_pro);
        ln_process.setVisibility(View.VISIBLE);
        new asynLoad().execute();

    }

    public void init() {
        diag = new XacThucAppDialog(EditImageActivity.this, new XacThucAppDialog.OnButtonClicked() {
            @Override
            public void onOkClicked() {
                try {
                    FileUtils.deleteDirectory(new File(Constant.PATHTEMPIMAGE));
                    File f = new File(Constant.PATHTEMPIMAGE);
                    f.mkdirs();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finish();
                AnimationTranslate.previewAnimation(EditImageActivity.this);
            }

            @Override
            public void onCancelClicked() {

            }
        });

        qualtyDialog = new QualtyDialog(EditImageActivity.this, new QualtyDialog.OnButtonClicked() {
            @Override
            public void onOkClicked(boolean check) {
                ln_process.setVisibility(View.VISIBLE);
                h.removeCallbacksAndMessages(null);
                if (check) {
                    //chat luong cao
                    new asynWriteGif().execute(lsBitmap);
                } else {
                    //chat luong thap

                    pathout = Constant.PATHMYGIF + "/" + Utils.convertNameGIF();
                    String size = "500x500";
                    editVideo.excuteImagetoGif(Constant.PATHTEMPIMAGE + "/image%3d.png", pathout, size, delay.getProgress());
                }
            }
        });
        rlads = (RelativeLayout) findViewById(R.id.layout_ads);
        delay = (DiscreteSeekBar) findViewById(R.id.delay);
        img = (ImageView) findViewById(R.id.img_preview);
        rcview = (RecyclerView) findViewById(R.id.rc_effect);
        rcview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new AdapterEffect(lseffect, getLayoutInflater(), this);
        adapter.setHasStableIds(true);
        rcview.setAdapter(adapter);
        ShowAds.showAdsNative(rlads, EditImageActivity.this);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_frame = (TextView) findViewById(R.id.tv_frame);
        tv_apply = (TextView) findViewById(R.id.tv_apply);
        tv_save = (TextView) findViewById(R.id.tv_save);
        tv_save.setVisibility(View.VISIBLE);
        tv_apply.setVisibility(View.GONE);
        tv_save.setOnClickListener(on_click);
        iv_back.setOnClickListener(on_click);
        tv_apply.setOnClickListener(on_click);

        ln_effect = (LinearLayout) findViewById(R.id.ln_effect);
        ln_effect.setOnClickListener(on_click);
        img.setOnClickListener(on_click);
        showhideView(check, 1000);
        delay.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
                return value;
            }
        });
        delay.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
                h.removeCallbacksAndMessages(null);
            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
                Log.d("DEBUG", seekBar.getProgress() + "");
                try {
                    slideImage((1000 / seekBar.getProgress()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tv_frame.setText(seekBar.getProgress() + " frame/s");
            }
        });
    }

    public void showhideView(boolean check, int longtime) {
        if (check) {
            h.removeCallbacksAndMessages(null);
            tv_frame.setVisibility(View.INVISIBLE);
            rcview.setVisibility(View.VISIBLE);
            tv_apply.setVisibility(View.VISIBLE);
            delay.setVisibility(View.INVISIBLE);
            tv_save.setVisibility(View.GONE);
            img.setImageBitmap(thuml);
        } else {
            slideImage(longtime);
            tv_frame.setVisibility(View.VISIBLE);
            delay.setVisibility(View.VISIBLE);
            tv_save.setVisibility(View.VISIBLE);
            rcview.setVisibility(View.GONE);
            tv_apply.setVisibility(View.GONE);
        }
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
                    if (!pathcurves.equals("")) {
                        ln_process.setVisibility(View.VISIBLE);
                        new AsyncTask<Void, Void, Void>() {

                            @Override
                            protected Void doInBackground(Void... voids) {
                                TranlateCuverImage();
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);
                                ln_process.setVisibility(View.GONE);
                                check = false;
                                showhideView(check, 1000 / delay.getProgress());
                            }
                        }.execute();
                    } else {
                        Toast.makeText(EditImageActivity.this, getString(R.string.noeffect), Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case R.id.ln_effect: {
                    check = !check;
                    thuml = Utils.getBitmapFromLocalPath(Constant.PATHTEMPIMAGE + "/" + "image000.png", 1);
                    showhideView(check, 1000 / delay.getProgress());
                    break;
                }
            }
        }
    };

    @Override
    public void onClickImage(int position) {
        pathcurves = getCacheDir() + "/cross " + position + ".acv";
        Bitmap bmp = thuml;
        img.setImageBitmap(ConvertCurves(bmp));
        for (InfoEffect f : lseffect) {
            f.setCheck(false);
        }
        lseffect.get(position).setCheck(true);
        adapter.setLsefect(lseffect);
        adapter.notifyDataSetChanged();

    }

    public Bitmap ConvertCurves(Bitmap b) {
        InputStream is = null;
        try {
            is = FileUtils.openInputStream(new File(pathcurves));
            GPUImageToneCurveFilter filter = new GPUImageToneCurveFilter();
            try {
                filter.setFromCurveFileInputStream(is);
                is.close();
            } catch (IOException e) {
                Log.e("MainActivity", "Error");
            }
// Use GPUImage processed image
            GPUImage gpuImage = new GPUImage(getBaseContext());
            gpuImage.setImage(b);
            gpuImage.setFilter(filter);
            b = gpuImage.getBitmapWithFilterApplied();
// Later in the ImageView image display processing
            return b;
        } catch (IOException e) {
            e.printStackTrace();
            return b;
        }
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

    public void getAllImage(ArrayList<String> lspath) {
        lsBitmap.clear();
        int i = 0;
        for (String s : lspath) {
            Bitmap b = Utils.getBitmapFromLocalPath(s, 1);
            b = Utils.makeScaled(500, b, s);
            Bitmap dstBitmap = Bitmap.createBitmap(
                    500, // Width
                    500, // Height
                    Bitmap.Config.ARGB_8888); // Config
            Canvas c = new Canvas(dstBitmap);
            c.drawColor(Color.BLACK);
            int cx = (500 - b.getWidth()) / 2;
            int cy = (500 - b.getHeight()) / 2;
            c.drawBitmap(b, cx, cy, null);
            FileOutputStream out = null;
            String namefile = "";
            String index = "";
            try {
                if (i < 10) {
                    index = "image00" + i + ".png";
                } else if (i < 100) {
                    index = "image0" + i + ".png";
                } else if (i < 1000) {
                    index = "image" + i + ".png";
                }
                namefile = Constant.PATHTEMPIMAGE + "/" + index;
                out = new FileOutputStream(namefile);
                dstBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                lsBitmap.add(new InfoBitmap(namefile, dstBitmap));// bmp is your Bitmap instance
                // PNG is a lossless format, the compression factor (100) is ignored
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            i++;
        }
    }

    public void TranlateCuverImage() {
        ArrayList<InfoBitmap> ls = new ArrayList<>();
        FileOutputStream out = null;
        for (InfoBitmap b : lsBitmap) {
            try {
                out = new FileOutputStream(b.getPathfile());
                Bitmap bmp = ConvertCurves(b.getBmp());
                bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
                b.setBmp(bmp);
                ls.add(b);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        lsBitmap.clear();
        lsBitmap = ls;
    }

    private Handler h;
    private int i = -1;

    public void slideImage(final int longtime) {
        h = new Handler();
        r = new Runnable() {
            @Override
            public void run() {
                h.postDelayed(r, longtime);
                shownext();

            }
        };
        h.postDelayed(r, 0);
    }

    public void shownext() {
        if (i == lsBitmap.size() - 1) {
            i = 0;
        } else {
            i++;
        }
        Log.d("DEBUG", i + "size: " + lsBitmap.size());
        img.setImageBitmap(lsBitmap.get(i).getBmp());
    }

    @Override
    public void resultComand(String resutl) {
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(new File(pathout))));
        ln_process.setVisibility(View.GONE);
        Intent intent = new Intent(EditImageActivity.this, SlideGifActivity.class);
        intent.putExtra(Constant.DATAINTENT, pathout);
        intent.putExtra(Constant.DATAINTENT1, 1);
        startActivity(intent);

    }

    class asynWriteGif extends AsyncTask<ArrayList<InfoBitmap>, Void, String> {
        @Override
        protected String doInBackground(ArrayList<InfoBitmap>[] arrayLists) {
            try {
                pathout = Constant.PATHMYGIF + "/" + Utils.convertNameGIF();
                OutputStream os = new FileOutputStream(pathout);
                AnimatedGIFWriter writer = new AnimatedGIFWriter(true);
                writer.prepareForWrite(os, -1, -1);
                for (InfoBitmap b : arrayLists[0]) {
                    writer.writeFrame(os, b.getBmp(), 1000 / delay.getProgress());
                    Log.d("WriteFrame", "true");
                }
                writer.finishWrite(os);
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.fromFile(new File(pathout))));
                return "ok";
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ln_process.setVisibility(View.GONE);
            if (s != null) {
                //startintent
                Intent intent = new Intent(EditImageActivity.this, SlideGifActivity.class);
                intent.putExtra(Constant.DATAINTENT, pathout);
                intent.putExtra(Constant.DATAINTENT1, 1);
                startActivity(intent);
            }
        }
    }

    class asynLoad extends AsyncTask<Void, Void, ArrayList<InfoBitmap>> {


        @Override
        protected ArrayList<InfoBitmap> doInBackground(Void... voids) {
            getAllImage(lsFileImage);
            return lsBitmap;
        }

        @Override
        protected void onPostExecute(ArrayList<InfoBitmap> bitmaps) {
            super.onPostExecute(bitmaps);
            if (bitmaps.size() > 0) {
                ln_process.setVisibility(View.GONE);
                init();
            }
        }
    }

    @Override
    public void onBackPressed() {
        diag.show();
    }
}
