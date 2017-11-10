package com.studio.makergif.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.studio.makergif.R;


/**
 * Created by binhnk on 7/25/2017.
 */

public class QualtyDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private OnButtonClicked onButtonClicked;
    private LinearLayout lnhight, lnlow;
    private ImageView iv_hight, iv_low;
    private TextView ok;
    private boolean check = false;

    public QualtyDialog(@NonNull Context context, OnButtonClicked onButtonClicked) {
        super(context);
        this.context = context;
        this.onButtonClicked = onButtonClicked;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.layout_quaitly);
        ok = (TextView) findViewById(R.id.check_ok);
        lnhight = (LinearLayout) findViewById(R.id.ln_hight);
        lnlow = (LinearLayout) findViewById(R.id.ln_low);
        iv_hight = (ImageView) findViewById(R.id.iv_hight);
        iv_low = (ImageView) findViewById(R.id.iv_low);
        ok.setOnClickListener(this);
        lnhight.setOnClickListener(this);
        lnlow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check_ok:
                dismiss();
                onButtonClicked.onOkClicked(check);
                break;
            case R.id.ln_hight:
                check = true;
                update(check);
                break;
            case R.id.ln_low:
                check = false;
                update(check);
                break;
            default:
                dismiss();
                break;
        }
    }

    public interface OnButtonClicked {
        void onOkClicked(boolean check);

    }

    public void update(boolean check) {
        if (check) {
            iv_hight.setImageResource(R.drawable.ic_select_click);
            iv_low.setImageResource(R.drawable.ic_select);
        } else {
            iv_hight.setImageResource(R.drawable.ic_select);
            iv_low.setImageResource(R.drawable.ic_select_click);
        }
    }
}
