package com.sejongcloud.app.notice;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sejongcloud.app.dialog.TransDialog;
import sejong.Article;
import sejong.Parser;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.sejongcloud.app.R;

public class Notice extends ListActivity{
	long backKeyClick = 0;
	long backKeyClickTime;
	String mUrl1 = "http://cm.sejong.ac.kr/servlet/kr.co.k2web.jwizard.contents.board.boardUser.servlet.userMainServlet?command=list&client_id=board&handle=";
	String mUrl2 = "&board_id=";
	String mUrl3 = "&client_id=board&site_id=board&curPage=";
	String mUrl4 = "&search=&column=&b_type=01&list_type=01&board_seq=&view_board_id=&chkBoxSeq=&chkBoxId=&command=list&warning_yn=N&category_id=0&category_depth=&notice=&sboard_id=";
	String url = null;
	int[] handleArray = { 57, 59, 131, 60, 181 };
	int handle;
	private ListView lv;
	ThreadNotice mThread;
	ArrayList<Article> result = null;
	ArrayList<Article> temp = null;
	NoticeAdapter noticeAdapter = null;
	int mPosition;
	int url_current = 1;
	int bol = 0;
	LinearLayout mProgressLayout;
	LinearLayout buttonLinearLayout;
	Button sortBtn;
	ImageButton ib;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notice);

		sortBtn = (Button) findViewById(R.id.sortButton);
		buttonLinearLayout = (LinearLayout) findViewById(R.id.sortButtons);
		SharedPreferences pref = getSharedPreferences("noticeBar", 0);
		bol = pref.getInt("bol", 1);

		if (bol == 1) { // 접기
			buttonLinearLayout.setVisibility(View.VISIBLE);
			sortBtn.setBackgroundResource(com.sejongcloud.app.R.drawable.sort_button_up);
		} else { // 정렬
			buttonLinearLayout.setVisibility(View.GONE);
			sortBtn.setBackgroundResource(com.sejongcloud.app.R.drawable.sort_button_default);
		}

		/* 정렬버튼 */
		sortBtn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				if (buttonLinearLayout.getVisibility() == View.GONE) {
					// 접기
					buttonLinearLayout.setVisibility(View.VISIBLE);
					sortBtn.setBackgroundResource(com.sejongcloud.app.R.drawable.sort_button_up);
				} else if (buttonLinearLayout.getVisibility() == View.VISIBLE) {
					// 정렬
					buttonLinearLayout.setVisibility(View.GONE);
					sortBtn.setBackgroundResource(com.sejongcloud.app.R.drawable.sort_button_default);
				}
			}
		});
		lv = getListView();
		LinearLayout mLinearLayout = (LinearLayout) View.inflate(this,
				R.layout.progress, null);
		lv.addFooterView(mLinearLayout);
		mProgressLayout = (LinearLayout) findViewById(R.id.progressLayout);
		mProgressLayout.setVisibility(View.GONE);

		handle = handleArray[0];
		url = mUrl1 + handle + mUrl2 + handle + mUrl3 + url_current + mUrl4;
		lv.setOnScrollListener(new OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			public void onScroll(AbsListView view, int firstVisibleItem,
								 int visibleItemCount, int totalItemCount) {
				if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
				}
			}
		});

		findViewById(R.id.noticeAll).setOnClickListener(mClickListener);
		findViewById(R.id.noticeNew).setOnClickListener(mClickListener);
		findViewById(R.id.noticeInter).setOnClickListener(mClickListener);
		findViewById(R.id.noticeCollege).setOnClickListener(mClickListener);
		findViewById(R.id.noticeGradute).setOnClickListener(mClickListener);
		findViewById(R.id.moreListButton).setOnClickListener(
				new Button.OnClickListener() {
					public void onClick(View v) {
						url_current++;
						url = mUrl1 + handle + mUrl2 + handle + mUrl3
								+ url_current + mUrl4;
						mProgressLayout.setVisibility(View.VISIBLE);
						final getMoreList mGetThread = new getMoreList();
						mGetThread.setDaemon(true);
						mGetThread.start();
					}
				});


		/* 처음 데이터를 가져오기 위한 스레드 */
		TransDialog.showLoading(this);

		if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
			new GetNotice().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
		else
			new GetNotice().execute(url);
	}

	public void onPause() {
		super.onPause();

		SharedPreferences pref = getSharedPreferences("noticeBar", 0);
		SharedPreferences.Editor edit = pref.edit();

		if (buttonLinearLayout.getVisibility() == View.GONE) {
			bol = 0;
		} else if (buttonLinearLayout.getVisibility() == View.VISIBLE) {
			bol = 1;
		}

		edit.putInt("bol", bol);
		edit.commit();
	}

	class getMoreList extends Thread { // code thread
		getMoreList() {
		}

		public void run() {
			try {
				temp = new GetNotice().getNotice(url);

				if(temp == null){
					TransDialog.hideLoading();
					return;
				}

			} catch (Exception e) {
				Log.d("getMoreList : ", e.getMessage());
			}
			getHandler.sendEmptyMessage(0);
		}
	}

	Handler getHandler = new Handler() {
		public void handleMessage(Message msg) { // UI
			try {
				if(temp.isEmpty() == true)
					return;

				String resultTop = result.get(result.size() - 1).getId();

				Log.w(" compare temp result : ", temp.get(0).getId()+"/"+temp.get(temp.size()-1).getId()+"/"+resultTop);
				Log.w(" temp size : ", Integer.toString(temp.size()));
				if (temp.get(0).getId().equals(resultTop) || temp.get(temp.size()-1).getId().equals(resultTop)){
					ib = (ImageButton) findViewById(R.id.moreListButton);
					ib.setVisibility(View.GONE);
				}else {
					for (int i = 0; i < temp.size(); i++) {
						noticeAdapter.add(temp.get(i));
					}
				}

				noticeAdapter.notifyDataSetChanged(); // ListView가 변경됬다는 것을 어댑터에 알려야함
				mProgressLayout.setVisibility(View.GONE);
			}catch (Exception e ){
				e.printStackTrace();
			}
		}
	};

	private class NoticeAdapter extends ArrayAdapter<Article> {
		public NoticeAdapter(Context context, int textViewResourceId,
							 ArrayList<Article> items) {
			super(context, textViewResourceId, items);
		}

		public void addNotice(ArrayList<Article> articles){
			for(int i=0;i<articles.size();i++){
				this.add(articles.get(i));
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;

			if (v == null) {
				v = LayoutInflater.from(getContext()).inflate(R.layout.notice_inner, parent, false);
//				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//				v = vi.inflate(R.layout.notice_inner, null);
			}else {
				Article mNotice = getItem(position);

				if (mNotice != null) {
					Log.d("Article mNotice :",mNotice.getTitle()+"/"+position);

					TextView noticeTitle = (TextView) v
							.findViewById(R.id.noticeTitle);
					TextView noticeSubTitle = (TextView) v
							.findViewById(R.id.noticeSubTitle);
					TextView noticeSubUser = (TextView) v
							.findViewById(R.id.noticeSubUser);

					if (noticeTitle != null) {
						noticeTitle.setText(mNotice.getTitle());
					}
					if (noticeSubTitle != null) {
						noticeSubTitle.setText(mNotice.getDate());
					}
					if (noticeSubUser != null) {
						noticeSubUser.setText(mNotice.getUser());
					}
				}
			}
			return v;
		}
	}

	Button.OnClickListener mClickListener = new Button.OnClickListener() {
		public void onClick(final View v) {
			url_current = 1;
			TransDialog.showLoading(Notice.this);

			ib = (ImageButton) findViewById(R.id.moreListButton);
			ib.setVisibility(View.VISIBLE);

			switch (v.getId()) {
				case R.id.noticeAll: // 전체
					handle = handleArray[0];
					url = mUrl1 + handle + mUrl2 + handle + mUrl3 + url_current
							+ mUrl4;
					break;
				case R.id.noticeNew: // 입학
					handle = handleArray[1];
					url = mUrl1 + handle + mUrl2 + handle + mUrl3 + url_current
							+ mUrl4;
					break;
				case R.id.noticeInter: // 국제
					handle = handleArray[2];
					url = mUrl1 + handle + mUrl2 + handle + mUrl3 + url_current
							+ mUrl4;
					break;
				case R.id.noticeCollege: // 학사
					handle = handleArray[3];
					url = mUrl1 + handle + mUrl2 + handle + mUrl3 + url_current
							+ mUrl4;
					break;
				case R.id.noticeGradute: // 졸업
					handle = handleArray[4];
					url = mUrl1 + handle + mUrl2 + handle + mUrl3 + url_current
							+ mUrl4;
					break;
			}
			mThread = new ThreadNotice(url);
			mThread.setDaemon(true);
			mThread.start();
		}
	};

	public void onListItemClick(ListView parent, View v, int position, long id) {
		mPosition = position;
		TransDialog.showLoading(this);

		new Thread() {
			public void run() {
				Intent i = new Intent(Notice.this, NoticeContent.class);
				i.putExtra("board_seq", result.get(mPosition).getId());
				i.putExtra("handle", result.get(mPosition).getHandle());
				i.putExtra("subject", result.get(mPosition).getTitle());
				i.putExtra("user", result.get(mPosition).getUser());
				i.putExtra("date", result.get(mPosition).getDate());
				startActivity(i);
			}
		}.start();
	}

	private class ThreadNotice extends Thread { // code thread
		String mUrl;

		public ThreadNotice(String url) {
			mUrl = url;
		}

		public void run() {
			super.run();

			try {
				result = new GetNotice().getNotice(url);

				if(result == null){
					System.out.print("ThreadNotice run error");
					TransDialog.hideLoading();
					return;
				}
			} catch (Exception e) {
				Log.e("Get Notice Exception", e.toString());
			}
			mAfter.sendEmptyMessage(0);
		}
	}

	Handler mAfter = new Handler() {
		public void handleMessage(Message msg) { // UI
			TransDialog.hideLoading();
			noticeAdapter = new NoticeAdapter(Notice.this,
					R.layout.notice_inner, result);
			setListAdapter(noticeAdapter);
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

				Toast t = Toast.makeText(Notice.this,
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

	private class GetNotice extends AsyncTask<String, Void, ArrayList<Article>> {
		@Override
		protected ArrayList<Article> doInBackground(String... urls){
			try {
				return this.getNotice(urls[0]);
			}catch (Exception e){
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<Article> res){
			TransDialog.hideLoading();

			if(res != null){
				result = new ArrayList<Article>();

				noticeAdapter = new NoticeAdapter(Notice.this,
						R.layout.notice_inner, result);
				lv.setAdapter(noticeAdapter);

				noticeAdapter.addNotice(res);
				noticeAdapter.notifyDataSetChanged();
			}
		}

		public ArrayList<Article> getNotice(String address) {
			Pattern mPattern = Pattern.compile("handle=(.*)&board_id");
			Matcher mTemp = mPattern.matcher(address); // 부가정보 따옴
			String handle = null;

			if (mTemp.find()) {
				handle = mTemp.group(1);
			}

			Parser parser = new Parser();
			ArrayList<Article> articles = new ArrayList<Article>();

			try { // notice
				articles = parser.previews(address, handle);
			} catch (Exception e) {
				Log.d("GetNotice Error : ", e.getMessage());
				return null;
			}

			return articles;
		}
	}
}