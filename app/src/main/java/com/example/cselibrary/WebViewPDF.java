package com.example.cselibrary;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Objects;

public class WebViewPDF extends AppCompatActivity {
    private WebView webView;
    private String type;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_web_view_pdf);

        Objects.requireNonNull(getSupportActionBar()).hide();
        webView = findViewById(R.id.webview);


        Intent intent = getIntent();
        String pdfurl = intent.getStringExtra("pdfurl");

        type = intent.getStringExtra("type");

        final ProgressDialog dialog = new ProgressDialog(this);
        if (type.equals("url")) {
            dialog.setTitle("Loading");
            dialog.setMessage("Loading Profile...");
            dialog.setCancelable(false);
        }
        if (type.equals("pdf")) {
            dialog.setTitle("Loading");
            dialog.setMessage("Loading Document...");
            dialog.setCancelable(false);
        }
        MyWebView myWebView = new MyWebView(dialog);


        webView.setWebViewClient(myWebView);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                if (type.equals("url"))
                    dialog.setMessage("Loading : " + newProgress + " %\nPlease wait a moment........");

                if(type.equals("pdf"))
                    dialog.setMessage("Rendering : " + newProgress + " %\nPlease wait a moment........");

            }

        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.loadUrl(pdfurl);

    }

    class MyWebView extends WebViewClient {
        ProgressDialog dialog;

        public MyWebView(ProgressDialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            view.loadUrl("file:///android_asset/index.html");
            dialog.dismiss();

            //custom Toast
            LayoutInflater li = getLayoutInflater();
            View v = li.inflate(R.layout.toast_error, ((ViewGroup) findViewById(R.id.errorlayout)));
            ((TextView) v.findViewById(R.id.texterror)).setText("Failed to Load...");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.setView(v);
            toast.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            dialog.dismiss();

            //custom Toast
            LayoutInflater li = getLayoutInflater();
            View v = li.inflate(R.layout.toast_success, ((ViewGroup) findViewById(R.id.successlayout)));
            ((TextView) v.findViewById(R.id.textsuccess)).setText("Loaded Successfully...");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.setView(v);
            toast.show();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            dialog.show();
        }
    }
}
