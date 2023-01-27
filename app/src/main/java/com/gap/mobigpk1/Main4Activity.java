package com.gap.mobigpk1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.yodo1.mas.banner.Yodo1MasBannerAdListener;
import com.yodo1.mas.banner.Yodo1MasBannerAdView;
import com.yodo1.mas.error.Yodo1MasError;

public class Main4Activity extends AppCompatActivity {
    private Button button1,button2,button3;
    private AdView mAdView;
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        title = findViewById(R.id.title);
        getSupportActionBar().hide();
        String Title = getIntent().getStringExtra("title");
        title.setText(Title);

        Yodo1MasBannerAdView bannerAdView = findViewById(R.id.yodo1_mas_banner);
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

        button1=findViewById(R.id.btn1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                civ1 s=new civ1();
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.lay1,s);
                transaction.commit();
            }
        });
//        button2=findViewById(R.id.btn2);
//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                civ2 magic=new civ2();
//                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.lay1,magic);
//                transaction.commit();
//            }
//        });
//        button3=findViewById(R.id.btn3);
//        button3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                civ3 m=new civ3();
//                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.lay1,m);
//                transaction.commit();
//            }
//        });
    }
}
