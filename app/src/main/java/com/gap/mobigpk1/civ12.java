package com.gap.mobigpk1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.yodo1.mas.banner.Yodo1MasBannerAdListener;
import com.yodo1.mas.banner.Yodo1MasBannerAdView;
import com.yodo1.mas.error.Yodo1MasError;

public class civ12 extends AppCompatActivity {
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blank);


        Dialog progressDialog = new Dialog(civ12.this);
        progressDialog.setContentView(R.layout.danger);
        progressDialog.setCancelable(false);

        Yodo1MasBannerAdView bannerAdView=progressDialog.findViewById(R.id.yodo1_mas_banner);
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

        Button btn = progressDialog.findViewById(R.id.Back);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        Button btn2 = progressDialog.findViewById(R.id.Review);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.gap.mobigpk1&hl=en")));
            }
        });

        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        progressDialog.show();
 /*
        Button button1 = findViewById(R.id.btn1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(civ12.this,AppMath1bActivity.class);
                startActivity(intent);
            }
        });
        Button button2 = findViewById(R.id.btn2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(civ12.this,AppMechActivity.class);
                startActivity(intent);
            }
        });
        Button button3 = findViewById(R.id.btn3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(civ12.this,bmee.class);
                startActivity(intent);
            }
        });
        Button button4 = findViewById(R.id.btn4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(civ12.this,BITActivity.class);
                startActivity(intent);
            }
        });

  */
    }
}
