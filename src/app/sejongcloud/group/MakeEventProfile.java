package app.sejongcloud.group;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import app.sejongcloud.R;
import app.sejongcloud.R.id;
import app.sejongcloud.R.layout;
import app.sejongcloud.main.Main;

public class MakeEventProfile extends Activity {
	String subject;
	String content;
	String name;
	String email;
	String password;
	String hp;
	int type;
	int period;
	int count;
	ProgressDialog dialog;
	EditText eName;
	EditText eEmail;
	EditText ePassword;
	EditText eHp;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_make_event_profile);

		eName = (EditText) findViewById(R.id.eventName);
		eEmail = (EditText) findViewById(R.id.eventEmail);
		ePassword = (EditText) findViewById(R.id.eventPassword);
		eHp = (EditText) findViewById(R.id.eventHp);

		subject = getIntent().getStringExtra("subject");
		content = getIntent().getStringExtra("content");
		type = getIntent().getIntExtra("type", 0);
		period = getIntent().getIntExtra("period", 0);
		count = getIntent().getIntExtra("count", 0);

		SharedPreferences pref = getSharedPreferences("profile", 0);
		name = pref.getString("name", "");
		email = pref.getString("email", "");
		password = pref.getString("password", "");
		hp = pref.getString("hp", "");

		eName.setText(name);
		eEmail.setText(email);
		ePassword.setText(password);
		eHp.setText(hp);

		Button nextBtn = (Button) findViewById(R.id.profileNextButton);
		nextBtn.setOnClickListener(mClickListener);

	}

	public void onPause() {
		super.onPause();

		SharedPreferences pref = getSharedPreferences("profile", 0);
		SharedPreferences.Editor edit = pref.edit();

		String pName = name;
		String pEmail = email;
		String pPassword = password;
		String pHp = hp;

		edit.putString("name", pName);
		edit.putString("email", pEmail);
		edit.putString("password", pPassword);
		edit.putString("hp", pHp);

		edit.commit();
	}

	Button.OnClickListener mClickListener = new Button.OnClickListener() {
		public void onClick(View v) {
			name = eName.getText().toString();
			email = eEmail.getText().toString();
			password = ePassword.getText().toString();
			hp = eHp.getText().toString();

			makeEvent();
		}
	};

	public void makeEvent() {
		dialog = ProgressDialog.show(MakeEventProfile.this, "", "로딩중.... ",
				true);
		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					URL url = new URL(
							"http://yhjun0229.cafe24.com/sejongstick/php/createEvent.php");
					HttpURLConnection httpURLCon = (HttpURLConnection) url
							.openConnection();
					httpURLCon.setDefaultUseCaches(false);
					httpURLCon.setDoInput(true);
					httpURLCon.setDoOutput(true);
					httpURLCon.setRequestMethod("POST");
					httpURLCon.setRequestProperty("content-type",
							"application/x-www-form-urlencoded");

					StringBuffer sb = new StringBuffer();
					sb.append("tag").append("=").append(type).append("&");
					sb.append("subject").append("=").append(subject)
							.append("&");
					sb.append("content").append("=").append(content)
							.append("&");
					sb.append("period").append("=").append(period).append("&");
					sb.append("count").append("=").append(count).append("&");
					sb.append("owner").append("=").append(name).append("&");
					sb.append("email").append("=").append(email).append("&");
					sb.append("password").append("=").append(password)
							.append("&");
					sb.append("hp").append("=").append(hp);

					// Log.d("sb string:",sb.toString());
					PrintWriter pw = new PrintWriter(new OutputStreamWriter(
							httpURLCon.getOutputStream(), "UTF-8"));
					pw.write(sb.toString());
					pw.flush();

					BufferedReader bf = new BufferedReader(
							new InputStreamReader(httpURLCon.getInputStream(),
									"UTF-8"));
					/*
					 * StringBuilder buff=new StringBuilder(); String line;
					 * while((line=bf.readLine())!=null){ buff.append(line); }
					 */
					// Log.d("buff",buff+"");

					httpURLCon.disconnect();
					bf.close();
					pw.close();

					afterHandler.sendEmptyMessage(0);
				} catch (Exception e) {
					Log.d("error", e.getMessage());
				}
			}
		});
		thread.start();
	}

	private Handler afterHandler = new Handler() {
		public void handleMessage(Message msg) {
			dialog.dismiss();
			Toast.makeText(MakeEventProfile.this, "정상적으로 모임을 만드셨습니다.",
					Toast.LENGTH_LONG).show();
			Intent i = new Intent(MakeEventProfile.this, Main.class)
					.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.putExtra("bol", 1);
			startActivity(i);
		}
	};
}
