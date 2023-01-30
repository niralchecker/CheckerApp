package com.mor.sa.android.activities;


import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class webPreview extends AppCompatActivity {
//    WebView webPreview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_preview);

//        webPreview = (WebView) findViewById(R.id.webPreview);
//        webPreview.getSettings().setJavaScriptEnabled(true);
//        webPreview.getSettings().setLoadWithOverviewMode(true);
//        webPreview.getSettings().setUseWideViewPort(true);
//        webPreview.setWebViewClient(new WebViewClient(){
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                view.loadUrl("https://www.google.com/");
//                return true;
//            }
//        });
//        String data = "<html><body><h1>Hello Checker</h1></body></html>";
////        webPreview.loadData(data, "text/html", "UTF-8");
//        webPreview.loadUrl("https://www.google.com/");
    }
}
