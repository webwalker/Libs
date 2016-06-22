package com.webwalker.framework.ui;

import java.util.ArrayList;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

/**
 * 底部导航
 * 
 * @author xu.jian
 * 
 */
public class MyFragmentTabHost implements TabHost.OnTabChangeListener {
	private FragmentTabHost fragmentTabHost;
	private LayoutInflater layoutInflater;
	private Resources resources;
	private ArrayList<MyFragmentItem> tabs;
	private MyFragmentItem preItem;
	private int imgWidth, imgHeight;
	public int downStyle, upStyle;
	public int downColor, upColor;

	public MyFragmentTabHost(FragmentTabHost tabhost, FragmentActivity activity) {
		this(tabhost, activity, android.R.id.tabcontent);
	}

	public MyFragmentTabHost(FragmentTabHost tabhost,
			FragmentActivity activity, int containerLayoutId) {
		fragmentTabHost = tabhost;
		tabs = new ArrayList<MyFragmentItem>();
		layoutInflater = activity.getLayoutInflater();
		resources = activity.getResources();
		tabhost.setup(activity.getBaseContext(),
				activity.getSupportFragmentManager(), containerLayoutId);
		tabhost.setOnTabChangedListener(this);
	}

	public View newTabView(int layoutid, int backgroung, String title) {
		TextView tv = (TextView) layoutInflater.inflate(layoutid, null);
		tv.setText(title);
		Drawable draw = getBoundDrawable(backgroung);
		tv.setCompoundDrawables(null, draw, null, null);
		return tv;
	}

	private Drawable getBoundDrawable(int resId) {
		Drawable draw = resources.getDrawable(resId);
		if (imgWidth > 0)
			draw.setBounds(0, 0, imgWidth, imgHeight);
		return draw;
	}

	public void setBoundDrawable(TextView tv, int resId) {
		Drawable draw = resources.getDrawable(resId);
		if (imgWidth > 0)
			draw.setBounds(0, 0, imgWidth, imgHeight);
		tv.setCompoundDrawables(null, draw, null, null);
	}

	// setIndicator设置标签和图表
	public void addTab(MyFragmentItem item) {
		tabs.add(item);
		fragmentTabHost.addTab(fragmentTabHost.newTabSpec(item.tag)
				.setIndicator(item.view), item.fragment, null);
	}

	public MyFragmentItem getTabByTag(String tag) {
		for (MyFragmentItem item : tabs) {
			if (item.tag == tag) {
				return item;
			}
		}
		return null;
	}

	@Override
	public void onTabChanged(String tabId) {
		MyFragmentItem item = getTabByTag(tabId);
		if (item != null) {
			if (preItem != null) {
				TextView tv = (TextView) preItem.view;
				Drawable draw = getBoundDrawable(preItem.upImg);
				tv.setCompoundDrawables(null, draw, null, null);
				if (upStyle > 0)
					tv.setBackgroundResource(upStyle);
				if (upColor > 0)
					tv.setTextColor(getUpColor());
			}
			preItem = item;

			TextView tv = (TextView) item.view;
			Drawable draw = getBoundDrawable(item.downImg);
			tv.setCompoundDrawables(null, draw, null, null);
			if (downStyle > 0)
				tv.setBackgroundResource(downStyle);
			if (downColor > 0)
				tv.setTextColor(getDownColor());
		}
	}

	public static class MyFragmentItem {
		public View view;
		public String tag;
		public Class<?> fragment;
		public int downImg;
		public int upImg;

		public MyFragmentItem() {
		}

		public MyFragmentItem(View view, String tag, Class<?> fragment,
				int downImg, int upImg) {
			this.view = view;
			this.tag = tag;
			this.fragment = fragment;
			this.downImg = downImg;
			this.upImg = upImg;
		}
	}

	public int getImgWidth() {
		return imgWidth;
	}

	public void setImgWidth(int imgWidth) {
		this.imgWidth = imgWidth;
	}

	public int getImgHeight() {
		return imgHeight;
	}

	public void setImgHeight(int imgHeight) {
		this.imgHeight = imgHeight;
	}

	public int getDownStyle() {
		return downStyle;
	}

	public void setDownStyle(int downStyle) {
		this.downStyle = downStyle;
	}

	public int getUpStyle() {
		return upStyle;
	}

	public void setUpStyle(int upStyle) {
		this.upStyle = upStyle;
	}

	public int getDownColor() {
		return resources.getColor(downColor);
	}

	/**
	 * eg:Color.parseColor("#FFEEDD") | R.color.line_deep
	 * 
	 * @param downColor
	 */
	public void setDownColor(int downColor) {
		this.downColor = downColor;
	}

	public int getUpColor() {
		return resources.getColor(upColor);
	}

	public void setUpColor(int upColor) {
		this.upColor = upColor;
	}

}