package app.sejongcloud;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import app.sejongcloud.R;

public class Tab2ContentWeb extends Activity {
	WebView mWebView;
	ProgressDialog dialog;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab2_content_web);

		dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setMessage("로딩 중입니다...");
		dialog.setCancelable(false);
		dialog.show();

		String url = getIntent().getStringExtra("url");
		mWebView = (WebView) findViewById(R.id.noticeWebView);
		mWebView.setVisibility(View.INVISIBLE);
		mWebView.setWebViewClient(new goNoticeClient()); // WebViewClient 지정
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadUrl(url);
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
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private class goNoticeClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
}
