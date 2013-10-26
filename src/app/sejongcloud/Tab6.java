package app.sejongcloud;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import app.sejongcloud.R;

public class Tab6 extends Activity {
	Intent i;
	long backKeyClick;
	long backKeyClickTime;
	int bol;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab6);

		backKeyClick = 0;
		Button food0Btn = (Button) findViewById(R.id.food0Button);
		Button food1Btn = (Button) findViewById(R.id.food1Button);
		Button libraryBtn = (Button) findViewById(R.id.libraryButton);
		Button cafe0Btn = (Button) findViewById(R.id.cafe0Button);
		Button cafe1Btn = (Button) findViewById(R.id.cafe1Button);
		Button tipBtn = (Button) findViewById(R.id.foodTipButton);
		food0Btn.setOnClickListener(mClickListener);
		food1Btn.setOnClickListener(mClickListener);
		libraryBtn.setOnClickListener(mClickListener);
		cafe0Btn.setOnClickListener(mClickListener);
		cafe1Btn.setOnClickListener(mClickListener);
		tipBtn.setOnClickListener(mClickListener);
	}

	Button.OnClickListener mClickListener = new Button.OnClickListener() {
		public void onClick(View v) {
			bol = 1;
			switch (v.getId()) {
			case R.id.food0Button:
				i = new Intent(Tab6.this, Tab6Web.class);
				i.putExtra("url",
						"http://sjcoop.org/cheditor/attach/food01.jpg");
				break;
			case R.id.food1Button:
				i = new Intent(Tab6.this, Tab6Web.class);
				i.putExtra("url", "http://m.sejong.ac.kr/front/cafeteria.do");
				break;
			case R.id.libraryButton:
				i = new Intent(Tab6.this, Tab6Web.class);
				i.putExtra("url", "http://210.107.226.14/seat/domian5.asp");
				break;
			case R.id.cafe0Button:
				i = new Intent(Tab6.this, Tab6Web.class);
				i.putExtra("url", "http://yhjun0229.cafe24.com/cafe/cafe1.html");
				break;
			case R.id.cafe1Button:
				i = new Intent(Tab6.this, Tab6Web.class);
				i.putExtra("url", "http://yhjun0229.cafe24.com/cafe/cafe2.html");
				break;
			case R.id.foodTipButton:
				bol = 0;
				AlertDialog alert = new AlertDialog.Builder(Tab6.this)
						// .setIcon( R.drawable.icon )
						.setTitle("운영정보")
						.setMessage(
								"- 평일 : 오전 08:30 ~ 19:00\n- 토요일 : 09:00 ~ 14:00\n- 일요일 / 공휴일은 휴무")
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								}).show();
				break;
			}
			if (bol == 1) {
				startActivity(i);
			}
		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if ((keyCode == KeyEvent.KEYCODE_BACK)) {

			long currentTime = System.currentTimeMillis();
			final int duration = 2000;

			backKeyClick++;

			if (backKeyClick == 1) {
				backKeyClickTime = System.currentTimeMillis();

				Toast t = Toast.makeText(Tab6.this, "'뒤로' 버튼을  한번 더 누르면 종료됩니다",
						Toast.LENGTH_SHORT);
				t.setDuration(duration);
				t.show();

				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							Thread.sleep(duration);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						backKeyClick = 0;
					}
				}).start();
			} else if (backKeyClick == 2) {

				if (currentTime - backKeyClickTime <= duration) {
					return super.onKeyDown(keyCode, event);
				}
				backKeyClick = 0;
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
