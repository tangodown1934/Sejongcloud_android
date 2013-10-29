package app.sejongcloud.group;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import app.sejongcloud.R;

public class MakeEventType extends Activity {
	Button studyTypeBtn;
	Button generalTypeBtn;
	int type;
	Intent i;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_make_event_type);

		studyTypeBtn = (Button) findViewById(R.id.studyTypeButton);
		generalTypeBtn = (Button) findViewById(R.id.generalTypeButton);
		studyTypeBtn.setOnClickListener(mClickListener);
		generalTypeBtn.setOnClickListener(mClickListener);
	}

	Button.OnClickListener mClickListener = new Button.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.studyTypeButton:
				type = 0;
				break;
			case R.id.generalTypeButton:
				type = 5;
				break;
			}
			if (type == 5) {
				i = new Intent(MakeEventType.this, MakeEvent.class);
				i.putExtra("type", type);
			} else if (type == 0) {
				i = new Intent(MakeEventType.this,
						MakeEventTypeStudy.class);
			}
			startActivity(i);
		}
	};
}
