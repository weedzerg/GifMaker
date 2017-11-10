package com.studio.makergif.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.studio.makergif.R;


/**
 * Created by binhnk on 7/25/2017.
 */

public class XacThucAppDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private OnButtonClicked onButtonClicked;

    private TextView tvok, tvcancel;

    public XacThucAppDialog(@NonNull Context context, OnButtonClicked onButtonClicked) {
        super(context);
        this.context = context;
        this.onButtonClicked = onButtonClicked;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_xacthuc);

        tvok = (TextView) findViewById(R.id.tv_ok);
        tvcancel = (TextView) findViewById(R.id.tvcancel);

        tvok.setOnClickListener(this);
        tvcancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvcancel:
                dismiss();
                onButtonClicked.onCancelClicked();
                break;

            case R.id.tv_ok:
                dismiss();
                onButtonClicked.onOkClicked();
                break;

            default:
                dismiss();
                break;
        }
    }

    public interface OnButtonClicked {
        void onOkClicked();

        void onCancelClicked();
    }
}
