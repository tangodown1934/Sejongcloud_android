package app.sejongcloud.notice;

import java.io.IOException;

import sejong.Parser;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import app.sejongcloud.R;
import app.sejongcloud.R.id;
import app.sejongcloud.R.layout;

public class NoticeContent extends Activity {
	String url;
	Parser parser;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notice_content);

		parser = new Parser();

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
		String result = null;

		try {
			result = parser.content(url);
		} catch (IOException e) {
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
		// Toast.makeText(this, "url="+url+"result="+result,
		// Toast.LENGTH_LONG).show();
	}
}
