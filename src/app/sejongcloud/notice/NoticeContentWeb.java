package app.sejongcloud.notice;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import app.sejongcloud.R;
import app.sejongcloud.dialog.TransDialog;

public class NoticeContentWeb extends Activity {
	WebView mWebView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notice_content_web);

		TransDialog.showLoading(this);

		String url = getIntent().getStringExtra("url");
		mWebView = (WebView) findViewById(R.id.noticeWebView);
		mWebView.setVisibility(View.INVISIBLE);
		mWebView.setWebViewClient(new goNoticeClient()); // WebViewClient ÁöÁ¤
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadUrl(url);
		mWebView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				if (progress == 100) {
					mWebView.setVisibility(View.VISIBLE);
					TransDialog.hideLoading();
				} else {
					TransDialog.showLoading(NoticeContentWeb.this);
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
