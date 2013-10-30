package com.sejongcloud.app.notice;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import sejong.Parser;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sejongcloud.app.R;
import com.sejongcloud.app.dialog.TransDialog;

public class NoticeContent extends Activity {
	String url = null;
	String result = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notice_content);
		
		String board_seq = getIntent().getStringExtra("board_seq");
		String handle = getIntent().getStringExtra("handle");
		String subject = getIntent().getStringExtra("subject");
		String user = getIntent().getStringExtra("user");
		String date = getIntent().getStringExtra("date");

		url = "http://cm.sejong.ac.kr/servlet/kr.co.k2web.jwizard.contents.board.boardUser.servlet.userMainServlet?client_id=board&handle="
				+ handle
				+ "&command=view&curPage=1&board_seq="
				+ board_seq
				+ "&b_type=01";

		try {
			result = new ParseContentTask().execute(url).get();

			if (result != null) {
				TransDialog.hideLoading();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		((Button) findViewById(R.id.goUrlButton))
				.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View v) {
						Intent i = new Intent(NoticeContent.this,
								NoticeContentWeb.class);
						i.putExtra("url", url);
						startActivity(i);
					}
				});

		((TextView) findViewById(R.id.txt)).setText(Html.fromHtml(result));
		((TextView) findViewById(R.id.contentSubject)).setText(subject);
		((TextView) findViewById(R.id.contentUser)).setText(user);
		((TextView) findViewById(R.id.contentDate)).setText(date);
	}

	private class ParseContentTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... mUrl) {
			try {
				Parser parser = new Parser();
				result = parser.content(mUrl[0]);

			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			return result;
		}
	}
}
