package com.webwalker.framework.utils;

import android.text.Html;
import android.text.Spanned;

/**
 * @author xu.jian
 * 
 */
public class HtmlUtil {
	public static Spanned getHtmlAmount(String amount) {
		Spanned ret = Html.fromHtml("<font color=red>" + amount + "</font>");
		return ret;
	}
}
