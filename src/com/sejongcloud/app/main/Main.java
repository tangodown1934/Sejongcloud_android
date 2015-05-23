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
import com.sejongcloud.app.food.Food;
import com.sejongcloud.app.library.Library;
import com.sejongcloud.app.notice.Notice;
import com.sejongcloud.app.tip.Tip;

@SuppressWarnings("deprecation")
public class Main extends TabActivity implements TabHost.OnTabChangeListener{
	private TabHost tabHost=null;

	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		tabHost = getTabHost();
		tabHost.setup();
		tabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);

		tabHost.setOnTabChangedListener(this);

		setupTab(new TextView(this), "공지",
				new Intent(this, Notice.class)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
				getResources().getDrawable(R.drawable.tab_notice));
		setupTab(new TextView(this), "식단", new Intent(this, Food.class),
				getResources().getDrawable(R.drawable.tab_food));
		setupTab(new TextView(this), "도서관", new Intent(this, Library.class),
				getResources().getDrawable(R.drawable.tab_library));
		setupTab(new TextView(this), "도서관팁", new Intent(this, Tip.class),
				getResources().getDrawable(R.drawable.tab_tip));
		setupTab(new TextView(this), "소개",
				new Intent(this, community.class)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
				getResources().getDrawable(R.drawable.tab_feedback));

		tabHost.setCurrentTab(0);
//		int bol = getIntent().getIntExtra("bol", 0);
//		if (bol == 1) {
//			tabHost.setCurrentTab(1);
//		} else {
//			tabHost.setCurrentTab(0);
//		}
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
	@Override
	public void onTabChanged(String tabId) {
		// Tab 색 변경
		int menu_off[] ={R.drawable.tab_notice,R.drawable.tab_food,R.drawable.tab_library,R.drawable.tab_tip,R.drawable.tab_feedback};
		int menu_on[] ={R.drawable.tab_notice_selected,R.drawable.tab_food_selected,R.drawable.tab_library_selected,R.drawable.tab_tip_selected,R.drawable.tab_feedback_selected};

		for(int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			ImageView iv = (ImageView)tabHost.getTabWidget().getChildAt(i).findViewById(R.id.tabsImage);
			if(iv!=null)
				iv.setImageDrawable(getResources().getDrawable(menu_off[i]));
		}

		ImageView ip = (ImageView)tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).findViewById(R.id.tabsImage);
		if(ip!=null)
			ip.setImageDrawable(getResources().getDrawable(menu_on[tabHost.getCurrentTab()]));
	}
}