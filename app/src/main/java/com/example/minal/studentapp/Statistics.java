package com.example.minal.studentapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Statistics extends AppCompatActivity {

    WebView wv;


    @Override
    public void onBackPressed()
    {
        if(wv.canGoBack())
            wv.goBack();
        else
            super.onBackPressed();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Satatistics");
        wv=(WebView)findViewById(R.id.WV);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setFocusable(true);
        wv.setFocusableInTouchMode(true);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        wv.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wv.getSettings().setDatabaseEnabled(true);
        wv.getSettings().setAppCacheEnabled(true);
        wv.getSettings().setDomStorageEnabled(true);
        wv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wv.loadUrl("https://std.eng.cu.edu.eg/chsresultstatistics.aspx?s=1");
        wv.setWebViewClient(new WebViewClient());
    }



}
