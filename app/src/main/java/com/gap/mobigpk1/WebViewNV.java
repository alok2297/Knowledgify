package com.gap.mobigpk1;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class WebViewNV extends AppCompatActivity {

    private ProgressBar progressBar;
    private Toolbar titleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);

        String url=getIntent().getStringExtra("url");
        String title=getIntent().getStringExtra("title");
        getSupportActionBar().hide();

        WebView webview=findViewById(R.id.webview);
        titleBar=findViewById(R.id.titleBar);

        titleBar.setTitle(title);
        Log.e("this is good",url);

        if(url.contains("shorts")){
            webview.setClickable(false);
            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            },60000);
        }

        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl(url);
        WebSettings webSettings=webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        
        webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webview.getSettings().setAppCacheEnabled(true);
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        webSettings.setSaveFormData(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDomStorageEnabled(true);
        Toast.makeText(this, "Loading...", Toast.LENGTH_LONG).show();
    }
}