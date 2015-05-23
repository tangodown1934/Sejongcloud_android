package com.sejongcloud.app.tip;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.sejongcloud.app.R;

public class Tip extends Activity {
	WebView mWebView;
	ProgressDialog dialog;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tip);

		dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setCancelable(false);
		dialog.show();

		mWebView = (WebView) findViewById(R.id.TipWebView);
		mWebView.setVisibility(View.INVISIBLE);
		mWebView.setWebViewClient(new webClient()); // WebViewClient 지정
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
		mWebView.loadUrl("http://ec2-54-64-124-136.ap-northeast-1.compute.amazonaws.com/library");
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private class webClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
}
