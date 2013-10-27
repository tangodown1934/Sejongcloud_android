package app.sejongcloud;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Tab6 extends Activity {
	WebView mWebView;
	ProgressDialog dialog;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab6);

		dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setMessage("�ε� ���Դϴ�...");
		dialog.setCancelable(false);
		dialog.show();

		mWebView = (WebView) findViewById(R.id.TipWebView);
		mWebView.setVisibility(View.INVISIBLE);
		mWebView.setWebViewClient(new webClient()); // WebViewClient ����
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
		mWebView.loadUrl("http://sejongcloud.com");
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