package com.sejongcloud.app.group;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sejongcloud.app.R;

public class MakeEventTypeStudy extends Activity {
	Button btn1;
	Button btn2;
	Button btn3;
	Button btn4;
	Button btn5;
	int type;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_make_event_type_study);

		btn1 = (Button) findViewById(R.id.studyForeign);
		btn2 = (Button) findViewById(R.id.studyLicense);
		btn3 = (Button) findViewById(R.id.studyHuman);
		btn4 = (Button) findViewById(R.id.studyManagement);
		btn5 = (Button) findViewById(R.id.studyEngineer);
		btn1.setOnClickListener(mClickListener);
		btn2.setOnClickListener(mClickListener);
		btn3.setOnClickListener(mClickListener);
		btn4.setOnClickListener(mClickListener);
		btn5.setOnClickListener(mClickListener);
	}

	Button.OnClickListener mClickListener = new Button.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.studyForeign:
				type = 0;
				break;
			case R.id.studyLicense:
				type = 1;
				break;
			case R.id.studyHuman:
				type = 2;
				break;
			case R.id.studyManagement:
				type = 3;
				break;
			case R.id.studyEngineer:
				type = 4;
				break;
			}
			Intent i = new Intent(MakeEventTypeStudy.this,
					MakeEvent.class);
			i.putExtra("type", type);
			startActivity(i);
		}
	};
}
