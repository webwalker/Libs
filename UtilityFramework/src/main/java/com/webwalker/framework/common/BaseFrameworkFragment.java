package com.webwalker.framework.common;

import com.webwalker.framework.uiquery.AQuery;
import com.webwalker.framework.utils.Loggers;
import com.webwalker.framework.widget.LoadingDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View.OnClickListener;

/**
 * @author xu.jian
 * 
 */
public abstract class BaseFrameworkFragment extends Fragment implements
		OnClickListener {
	private LoadingDialog loading;
	protected AQuery aq = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		aq = new AQuery(this.getView());
	}

	/**
	 * Fragment虽然有onResume和onPause的，但是这两个方法是Activity的方法，调用时机也是与Activity相同，
	 * 和ViewPager搭配使用这个方法就很鸡肋了，根本不是你想要的效果，通过重写以下方法。
	 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			// 相当于Fragment的onResume
		} else {
			// 相当于Fragment的onPause
		}
	}

	public void init() {
		Loggers.d("current fragment:" + this.getClass().getName());
	}

	protected void showLoading() {
		loading = new LoadingDialog(getActivity());
		loading.showLoading();
	}

	protected void hideLoading() {
		if (loading != null)
			loading.hideLoading();
	}
}
