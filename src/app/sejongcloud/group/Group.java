package app.sejongcloud.group;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import app.sejongcloud.R;
import app.sejongcloud.dialog.TransDialog;
import app.sejongcloud.utils.JSONfunctions;

public class Group extends ListActivity {
	/** Called when the activity is first created. */
	long backKeyClick = 0;
	long backKeyClickTime;
	int pos;
	int count = 0;
	ArrayList<HashMap<String, String>> mylist;
	ListAdapter adapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group);

		Button refresh = (Button) findViewById(R.id.refreshButton);
		Button makeEvent = (Button) findViewById(R.id.makeEvent);
		refresh.setOnClickListener(mClickListener);
		makeEvent.setOnClickListener(mClickListener);

		mylist = new ArrayList<HashMap<String, String>>();
		onThread(); // 스레드 시작

		final ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				pos = position;
				TransDialog.showLoading(Group.this);
				new Thread() {
					public void run() {
						@SuppressWarnings("unchecked")
						HashMap<String, String> o = (HashMap<String, String>) lv
								.getItemAtPosition(pos);
						// Toast.makeText(Tab1.this, "ID '" + o.get("id") +
						// "' was clicked. \n Follower : "+o.get("follower"),
						// Toast.LENGTH_SHORT).show();
						Intent i = new Intent(Group.this, GroupContent.class);
						i.putExtra("subject", o.get("subject"));
						i.putExtra("index", Integer.parseInt(o.get("index")));
						i.putExtra("content", o.get("content"));
						i.putExtra("owner", Integer.parseInt(o.get("owner")));
						i.putExtra("follower", o.get("follower"));
						i.putExtra("count", Integer.parseInt(o.get("count")));
						i.putExtra("follower_count",
								Integer.parseInt(o.get("follower_count")));
						startActivity(i);
						TransDialog.hideLoading();
					}
				}.start();
			}
		});
	}

	Button.OnClickListener mClickListener = new Button.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.refreshButton:
				mylist = new ArrayList<HashMap<String, String>>(); // 초기화
				onThread();
				break;
			case R.id.makeEvent:
				Intent i = new Intent(Group.this, MakeEventType.class);
				startActivity(i);
				break;
			}
		}
	};

	public void onThread() {
		TransDialog.showLoading(this);
		Thread thread = new Thread(new Runnable() {
			public void run() {
				JSONArray json = JSONfunctions
						.getJSONfromURL("http://yhjun0229.cafe24.com/sejongstick/php/eventList.php?bol=-1");
				// Toast.makeText(Main.this, json.toString(),
				// Toast.LENGTH_LONG).show();
				try {
					JSONArray test = json;
					int bol;
					// Log.d("test length",""+test.length());

					for (int i = 0; i < test.length(); i++) {
						// Log.d("i",""+i);
						HashMap<String, String> map = new HashMap<String, String>();
						JSONObject data = test.getJSONObject(test.length() - i
								- 1);
						// Log.d("data",""+data);
						count = 0;
						bol = 0;

						String s = data.getString("followerIndex");
						for (int j = 0; j < s.length(); j++) {
							if (s.charAt(j) == '|') {
								bol = 1;
								count++;
							}
						}
						if (s.length() != 0 && bol == 0) {
							count = 1;
						} else if (bol == 1) {
							count++;
						}
						map.put("id", String.valueOf(i));
						map.put("subject", data.getString("subject"));
						map.put("content", data.getString("content"));
						map.put("owner", data.getString("owner"));
						map.put("follower", data.getString("follower"));
						map.put("count", data.getString("count"));
						map.put("index", data.getString("no"));
						map.put("follower_count", count + "");
						map.put("event_count",
								count + "/" + data.getString("count") + "명");
						map.put("event_period", data.getString("period")
								+ "일 남음");

						mylist.add(map);
					}
					adapter = new SimpleAdapter(Group.this, mylist,
							R.layout.group_inner, new String[] { "subject",
									"event_count", "event_period" }, new int[] {
									R.id.eventTitle, R.id.eventCount,
									R.id.eventPeriod });

					onHandler.sendEmptyMessage(0);
				} catch (Exception e) {
					Log.d("", e.getMessage());
				}
			}
		});
		thread.start();
	}

	private Handler onHandler = new Handler() {
		public void handleMessage(Message msg) {
			setListAdapter(adapter);
			TransDialog.hideLoading();
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if ((keyCode == KeyEvent.KEYCODE_BACK)) {

			long currentTime = System.currentTimeMillis();
			final int duration = 2000;

			backKeyClick++;

			if (backKeyClick == 1) {
				backKeyClickTime = System.currentTimeMillis();

				Toast t = Toast.makeText(Group.this,
						"'뒤로' 버튼을  한번 더 누르면 종료됩니다", Toast.LENGTH_SHORT);
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
