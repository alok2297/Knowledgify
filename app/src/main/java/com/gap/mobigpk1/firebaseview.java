package com.gap.mobigpk1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yodo1.mas.banner.Yodo1MasBannerAdListener;
import com.yodo1.mas.banner.Yodo1MasBannerAdView;
import com.yodo1.mas.error.Yodo1MasError;

public class firebaseview extends AppCompatActivity {
    private Yodo1MasBannerAdView bannerAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String email=getIntent().getStringExtra("title");
        getSupportActionBar().hide();
        setContentView(R.layout.activity_firebaseview);

        bannerAdView = findViewById(R.id.yodo1_mas_banner);
        bannerAdView.setAdListener(new Yodo1MasBannerAdListener() {
            @Override
            public void onBannerAdLoaded(Yodo1MasBannerAdView bannerAdView) {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onBannerAdFailedToLoad(Yodo1MasBannerAdView bannerAdView, @NonNull Yodo1MasError error) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onBannerAdOpened(Yodo1MasBannerAdView bannerAdView) {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onBannerAdFailedToOpen(Yodo1MasBannerAdView bannerAdView, @NonNull Yodo1MasError error) {
                // Code to be executed when an ad open fails.
            }

            @Override
            public void onBannerAdClosed(Yodo1MasBannerAdView bannerAdView) {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
        bannerAdView.loadAd();
        WebView webview=findViewById(R.id.webview);
        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl(email);
        WebSettings webSettings=webview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webview.getSettings().setAppCacheEnabled(true);
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        webSettings.setSaveFormData(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDomStorageEnabled(true);
    }
}