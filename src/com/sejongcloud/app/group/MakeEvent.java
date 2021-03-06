package com.sejongcloud.app.group;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.sejongcloud.app.R;

public class MakeEvent extends Activity {
	String subject;
	String content;
	int type;
	int period;
	int count;
	RadioButton periodBtn;
	RadioGroup periodGroup;
	RadioButton countBtn;
	RadioGroup countGroup;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_make_event);

		type = getIntent().getIntExtra("type", 0);
		periodGroup = (RadioGroup) findViewById(R.id.makePeriod);
		periodBtn = (RadioButton) findViewById(periodGroup
				.getCheckedRadioButtonId());
		countGroup = (RadioGroup) findViewById(R.id.makeCount);
		countBtn = (RadioButton) findViewById(countGroup
				.getCheckedRadioButtonId());
		Button nextBtn = (Button) findViewById(R.id.eventNextButton);
		nextBtn.setOnClickListener(mClickListener);
	}

	Button.OnClickListener mClickListener = new Button.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.eventNextButton:
				subject = ((EditText) findViewById(R.id.eventSubject))
						.getText().toString();
				content = ((EditText) findViewById(R.id.eventContent))
						.getText().toString();

				if (periodBtn.getId() == R.id.period1) {
					period = 14;
				} else if (periodBtn.getId() == R.id.period2) {
					period = 30;
				} else if (periodBtn.getId() == R.id.period3) {
					period = 60;
				}
				if (countBtn.getId() == R.id.count1) {
					count = 5;
				} else if (countBtn.getId() == R.id.count2) {
					count = 15;
				} else if (countBtn.getId() == R.id.count3) {
					count = 30;
				}
				break;
			}
			Intent i = new Intent(MakeEvent.this,
					MakeEventProfile.class);
			i.putExtra("subject", subject);
			i.putExtra("content", content);
			i.putExtra("period", period);
			i.putExtra("count", count);
			i.putExtra("type", type);
			startActivity(i);
		}
	};
}
