package com.app.globalacademy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.http.SslError;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.browser.customtabs.CustomTabsIntent;

public class MyWebViewClient extends WebViewClient {

    private Activity activity = null;
    private ProgressBar progressBar;
    private View mCustomView;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;
    protected FrameLayout mFullscreenContainer;
    private int mOriginalOrientation;
    private int mOriginalSystemUiVisibility;

    public MyWebViewClient(Activity activity, ProgressBar progressBar) {
        this.activity = activity;
        this.progressBar = progressBar;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {

      if (url.contains("thetelemaster.com")){
          CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
          builder.enableUrlBarHiding();
          CustomTabsIntent customTabsIntent = builder.build();
          customTabsIntent.intent.setPackage("com.android.chrome");
          customTabsIntent.launchUrl(activity, Uri.parse(url));

          return true;
      }
       return false;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        progressBar.setVisibility(View.GONE);
        Log.d("URL",url);

    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        Log.d("ERROR", error.toString()+" ");
    }

    @Override
    public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError er) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
        builder.setMessage("Error : Invalid SSL Certificate");
        builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.proceed();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.cancel();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
    }

}
