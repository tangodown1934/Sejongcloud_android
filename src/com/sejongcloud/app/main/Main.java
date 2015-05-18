package com.sejongcloud.app.main;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import com.sejongcloud.app.R;
import com.sejongcloud.app.community.community;
import com.sejongcloud.app.group.Group;
import com.sejongcloud.app.library.Library;
import com.sejongcloud.app.notice.Notice;
import com.sejongcloud.app.tip.Tip;

@SuppressWarnings("deprecation")
public class Main extends TabActivity {
	private TabHost tabHost;

	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		tabHost = getTabHost();
		tabHost.setup();
		tabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);

		setupTab(new TextView(this), "공지",
				new Intent(this, Notice.class)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
				getResources().getDrawable(R.drawable.tab2));
		setupTab(new TextView(this), "모임",
				new Intent(this, Group.class)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
				getResources().getDrawable(R.drawable.tab1));
		setupTab(new TextView(this), "도서관", new Intent(this, Library.class),
				getResources().getDrawable(R.drawable.tab5));
		setupTab(new TextView(this), "팁", new Intent(this, Tip.class),
				getResources().getDrawable(R.drawable.tab6));
		setupTab(new TextView(this), "소개",
				new Intent(this, community.class)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
				getResources().getDrawable(R.drawable.tab3));
		int bol = getIntent().getIntExtra("bol", 0);
		if (bol == 1) {
			tabHost.setCurrentTab(1);
		} else {
			tabHost.setCurrentTab(0);
		}

	}

	private void setupTab(final View view, final String tag, Intent intent,
			Drawable drawable) {
		View tabview = createTabView(tabHost.getContext(), drawable);
		TabSpec setContent = tabHost.newTabSpec(tag).setIndicator(tabview)
				.setContent(intent);
		tabHost.addTab(setContent);
	}

	private static View createTabView(final Context context, Drawable drawable) {
		View view = LayoutInflater.from(context)
				.inflate(R.layout.tabs_bg, null);
		ImageView iv = (ImageView) view.findViewById(R.id.tabsImage);
		iv.setImageDrawable(drawable);

		return view;
	}
}