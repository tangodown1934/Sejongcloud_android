package com.sejongcloud.app.community;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sejongcloud.app.dialog.TransDialog;
import com.sejongcloud.app.R;
import com.sejongcloud.app.utils.JSONfunctions;

public class community extends ListActivity {
	class CommentResult {
		String comment;
		String date;
		String no;
	}

	long backKeyClick;
	long backKeyClickTime;
	static ArrayList<CommentResult> mylist;
	CommentAdapter commentAdapter;
	View v;
	ListView lv;
	LinearLayout linear;
	String comment;
	String commentCount;
	int getCount;
	LinearLayout mProgressLayout;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.community);

		commentCount = "";
		backKeyClick = 0;
		getCount = 15;

		Button comment = (Button) findViewById(R.id.commentButton);
		Button developerTipBtn = (Button) findViewById(R.id.developerTipButton);
		developerTipBtn.setOnClickListener(mClickListener);
		comment.setOnClickListener(mClickListener);
		mylist = new ArrayList<CommentResult>();
		LayoutInflater inflater = getLayoutInflater();
		v = inflater.inflate(R.layout.community_developer, null);

		LinearLayout mLinearLayout = (LinearLayout) View.inflate(this,
				R.layout.progress, null);
		lv = getListView();
		lv.addHeaderView(v);
		lv.addFooterView(mLinearLayout);
		mProgressLayout = (LinearLayout) findViewById(R.id.progressLayout);
		mProgressLayout.setVisibility(View.GONE);
		lv.setTextFilterEnabled(true);
		onFirstThread();

		findViewById(R.id.moreListButton).setOnClickListener(
				new Button.OnClickListener() {
					public void onClick(View v) {
						mProgressLayout.setVisibility(View.VISIBLE);
						getCount += 15;
						getMoreList();
						// mGetThread.setDaemon(true);
						// mGetThread.start();
					}
				});

	}

	private class CommentAdapter extends ArrayAdapter<CommentResult> {
		private ArrayList<CommentResult> items;

		public CommentAdapter(Context context, int textViewResourceId,
				ArrayList<CommentResult> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.community_inner, null);
			}
			CommentResult mNotice = items.get(position);
			if (mNotice != null) {
				TextView tt = (TextView) v.findViewById(R.id.comment);
				TextView bt = (TextView) v.findViewById(R.id.commentDate);
				if (tt != null) {
					tt.setText(mNotice.comment);
				}
				if (bt != null) {
					bt.setText(mNotice.date);
				}
			}
			return v;
		}
	}

	OnClickListener mClickListener = new Button.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.commentButton:
				linear = (LinearLayout) View.inflate(community.this,
						R.layout.community_comment, null);
				new AlertDialog.Builder(community.this)
						.setTitle("의견")
						// .setIcon(R.drawable.icon)
						.setView(linear)
						.setPositiveButton("남기기",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										final EditText eComment = (EditText) linear
												.findViewById(R.id.commentText);
										comment = eComment.getText().toString();
										if (comment.length() == 0) {
											Toast.makeText(community.this,
													"실패. 내용을 입력하세요",
													Toast.LENGTH_LONG).show();
										} else {
											pushComment();
										}
									}
								})
						.setNegativeButton("취소",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
									}
								}).show();
				break;
			case R.id.developerTipButton:
				AlertDialog alert = new AlertDialog.Builder(community.this)
						// .setIcon( R.drawable.icon )
						.setTitle("공지")
						.setMessage(R.string.app_infomation)
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
		}
	};

	public void pushComment() {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					URL url = new URL(
							"http://yhjun0229.cafe24.com/sejongstick/app/php/comment.php");
					HttpURLConnection httpURLCon = (HttpURLConnection) url
							.openConnection();
					httpURLCon.setDefaultUseCaches(false);
					httpURLCon.setDoInput(true);
					httpURLCon.setDoOutput(true);
					httpURLCon.setRequestMethod("POST");
					httpURLCon.setRequestProperty("content-type",
							"application/x-www-form-urlencoded");

					StringBuffer sb = new StringBuffer();
					sb.append("comment").append("=").append(comment);

					PrintWriter pw = new PrintWriter(new OutputStreamWriter(
							httpURLCon.getOutputStream(), "UTF-8"));
					pw.write(sb.toString());
					pw.flush();

                    BufferedReader bf = new BufferedReader(
                            new InputStreamReader(httpURLCon.getInputStream(),
                                    "UTF-8"));

					StringBuilder buff = new StringBuilder();
					String line;
					while ((line = bf.readLine()) != null) {
						buff.append(line);
					}

					httpURLCon.disconnect();
					bf.close();
					pw.close();

					pushHandler.sendEmptyMessage(0);
				} catch (Exception e) {
				}
			}
		});
		thread.start();
	}

	private Handler pushHandler = new Handler() {
		public void handleMessage(Message msg) {
			Toast.makeText(community.this, "의견 감사합니다", Toast.LENGTH_LONG)
					.show();
			mylist = new ArrayList<CommentResult>(); // arraylist 초기화 시키고 다시
														// 어댑터에 적용하기 위해
			onFirstThread(); // 긁어오기 다시 (for refresh)
		}
	};

	public void onFirstThread() {
		TransDialog.showLoading(this);
		Thread thread = new Thread(new Runnable() {
			public void run() {
				JSONArray json = JSONfunctions
						.getJSONfromURL("http://yhjun0229.cafe24.com/sejongstick/app/php/commentList.php?count=15");
				try {
					JSONArray test = json;
					JSONObject temp = test.getJSONObject(0);
					commentCount = temp.getString("size");
					for (int i = 0; i < test.length(); i++) {
						CommentResult result = new CommentResult();
						JSONObject data = test.getJSONObject(i);
						result.no = data.getString("no");
						result.comment = data.getString("comment");
						result.date = data.getString("date");
						mylist.add(result);
					}
					commentAdapter = new CommentAdapter(community.this,
							R.layout.community_inner, mylist);
					// 시간걸리는 처리
					onFirstHandler.sendEmptyMessage(0);
				} catch (Exception e) {
					Log.d("", e.getMessage());
				}
			}
		});
		thread.start();
	}

	private Handler onFirstHandler = new Handler() {
		public void handleMessage(Message msg) {
			TransDialog.hideLoading();
			setListAdapter(commentAdapter);
			((TextView) v.findViewById(R.id.commentCount)).setText("의견 "
					+ commentCount + "개");
			// View갱신
		}
	};

	public void getMoreList() {
		TransDialog.showLoading(this);
		Thread thread = new Thread(new Runnable() {
			public void run() {
				JSONArray json = JSONfunctions
						.getJSONfromURL("http://yhjun0229.cafe24.com/sejongstick/app/php/commentList.php?count="
								+ getCount);
				// Toast.makeText(Main.this, json.toString(),
				// Toast.LENGTH_LONG).show();
				try {
					JSONArray test = json;
					for (int i = 0; i < test.length(); i++) {
						CommentResult result = new CommentResult();
						JSONObject data = test.getJSONObject(i);
						result.no = data.getString("no");
						result.comment = data.getString("comment");
						result.date = data.getString("date");
						mylist.add(result);
					}
					getHandler.sendEmptyMessage(0);
				} catch (Exception e) {
					Log.e("error", e.getMessage());
				}
			}
		});
		thread.start();
	}

	private Handler getHandler = new Handler() {
		public void handleMessage(Message msg) {
			commentAdapter.notifyDataSetChanged(); // ListView가 변경됬다는 것을 어댑터에
													// 알려야함
			TransDialog.hideLoading();
			mProgressLayout.setVisibility(View.GONE);
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

				Toast t = Toast.makeText(community.this,
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