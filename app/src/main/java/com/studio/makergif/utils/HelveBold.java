package com.studio.makergif.utils;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by DaiPhongPC on 8/30/2017.
 */

public class HelveBold extends TextView {
    public HelveBold(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public HelveBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public HelveBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("UTM HelveBold.ttf", context);
        setTypeface(customFont);
    }
}
