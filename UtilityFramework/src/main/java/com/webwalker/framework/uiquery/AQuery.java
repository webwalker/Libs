/*
 * Copyright 2011 - AndroidQuery.com (tinyeeliu@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.webwalker.framework.uiquery;

import com.webwalker.framework.utils.StringUtil;
import com.webwalker.framework.utils.UIUtils;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * The main class of AQuery. All methods are actually inherited from
 * AbstractAQuery.
 * 
 */
public class AQuery extends AbstractAQuery<AQuery> {
	public AQuery(Fragment fg) {
		super(fg);
	}

	public AQuery(Activity act) {
		super(act);
	}

	public AQuery(View view) {
		super(view);
	}

	public AQuery(Context context) {
		super(context);
	}

	public AQuery(Activity act, View root) {
		super(act, root);
	}

	// ///////////////////////////////////////////////////////////////////
	// ----------------------扩展---------------------------//
	public AQuery format(String... value) {

		if (view instanceof TextView) {
			TextView tv = (TextView) view;
			tv.setText(StringUtil.format(tv.getText().toString(), value));
		}

		return self();
	}

	public AQuery clicked(long disableTimes, OnClickListener listener) {
		if (UIUtils.isFastDoubleClick(disableTimes))
			return self();

		if (view != null) {
			view.setOnClickListener(listener);
		}

		return self();
	}

	public AQuery setTextSize(int dimen) {
		if (view instanceof TextView) {
			TextView tv = (TextView) view;
			tv.setTextSize(view.getResources().getDimensionPixelSize(dimen));
		}
		return self();
	}

	public AQuery setTextSizeSP(int dimen) {
		if (view instanceof TextView) {
			TextView tv = (TextView) view;
			tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, view.getResources()
					.getDimensionPixelSize(dimen));
		}
		return self();
	}

	public boolean isVisible() {
		if (view.getVisibility() == View.VISIBLE)
			return true;
		return false;
	}

	public boolean isFocus() {
		return view.isFocused();
	}

	public void focus() {
		if (view.isFocusable()) {
			view.requestFocus();
		}
	}
}
