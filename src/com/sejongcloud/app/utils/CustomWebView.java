package com.sejongcloud.app.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.sejongcloud.app.R;

public class CustomWebView extends Activity {
	WebView mWebView;
	ProgressDialog dialog;
	String url;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tip_web);

		url = getIntent().getStringExtra("url");
		dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setCancelable(false);
		dialog.show();

		mWebView = (WebView) findViewById(R.id.foodWebView);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.setVisibility(View.INVISIBLE);
		mWebView.setWebViewClient(new goLibraryClient()); // WebViewClient 지정
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				dialog.setProgress(progress);

				if (progress == 100) {
					mWebView.setVisibility(View.VISIBLE);
					dialog.dismiss();
				} else {
					dialog.show();
				}
			}
		});
		mWebView.loadUrl(url);
	}

	private class goLibraryClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
}
