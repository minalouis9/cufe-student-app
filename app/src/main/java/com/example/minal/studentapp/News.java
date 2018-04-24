package com.example.minal.studentapp;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.toolbox.HttpClientStack;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;


public class News extends AppCompatActivity {
    WebView wv;
    String url="http://eng.cu.edu.eg/ar/credit-hour-system/";
    String fullString;




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
        setContentView(R.layout.activity_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        wv=(WebView)findViewById(R.id.Wv);

        wv.getSettings().setJavaScriptEnabled(true);
        wv.setFocusable(true);
        wv.setFocusableInTouchMode(true);
        wv.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        wv.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wv.getSettings().setDatabaseEnabled(true);
        wv.getSettings().setAppCacheEnabled(true);
        wv.getSettings().setDomStorageEnabled(true);
        wv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.loadUrl(url);
        wv.setWebViewClient(new WebViewClient());
        wv.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


        try {
            URL url = new URL("http://eng.cu.edu.eg/ar/credit-hour-system/");
            enableStrictMode();

            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;
            while ( (line = br.readLine()) != null)
                fullString += line;

            br.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void enableStrictMode()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }


}
