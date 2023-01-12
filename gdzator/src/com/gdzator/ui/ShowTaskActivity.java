package com.gdzator.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.gdzator.GdzatorApplication;
import com.gdzator.R;
import com.gdzator.content.model.Task;
import com.gdzator.helpers.AdsHelper;
import com.gdzator.ui.fragments.ShowTaskFragment;
import com.gdzator.ui.fragments.TasksFragment;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_show_task)
public class ShowTaskActivity extends Activity {
    public final static String LOG_TAG = ShowTaskFragment.class.getName();

    @ViewById
    WebView webView;

    @ViewById
    TextView title;

    private Task task;

    private InterstitialAd interstitial;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            task = intent.getParcelableExtra(TasksFragment.ARG_TASK);
        }
    }

    @AfterViews
    void afterViews() {
        if (!AdsHelper.checkOffAds(this)) {
            interstitial = new InterstitialAd(this);
            interstitial.setAdUnitId(GdzatorApplication.BANNER_UNIT_ID2);
            AdRequest adRequest = new AdRequest.Builder().build();
            interstitial.loadAd(adRequest);
            interstitial.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    if (interstitial != null && interstitial.isLoaded())
                        try {
                            interstitial.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
            });
        }
        title.setText(task.number);
        webView.setBackgroundColor(Color.WHITE);
        webView.setInitialScale(1);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.loadUrl(task.url);
    }

    @Click
    void back() {
        finish();
    }
}
