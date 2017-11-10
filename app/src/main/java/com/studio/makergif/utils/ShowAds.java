package com.studio.makergif.utils;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by DaiPhongPC on 9/23/2017.
 */

public class ShowAds {
    public static void showAdsNative(final RelativeLayout adsView, Activity activity) {
        Ads.b(activity, adsView, new Ads.OnAdsListener() {

            @Override
            public void onError() {
                adsView.setVisibility(View.GONE);
            }

            @Override
            public void onAdLoaded() {
                adsView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdOpened() {
                adsView.setVisibility(View.VISIBLE);
            }
        });
    }
}
