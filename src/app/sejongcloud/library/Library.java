package app.sejongcloud.library;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import app.sejongcloud.R;

public class Library extends Activity {
	WebView mWebView;
	ProgressDialog dialog;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.library);

		dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setCancelable(false);
		dialog.show();

		mWebView = (WebView) findViewById(R.id.libraryWebView);
		mWebView.setVisibility(View.INVISIBLE);
		mWebView.setWebViewClient(new goLibraryClient()); // WebViewClient ÁöÁ¤
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
		mWebView.loadUrl("http://m.lib.sejong.ac.kr");
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private class goLibraryClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
}
