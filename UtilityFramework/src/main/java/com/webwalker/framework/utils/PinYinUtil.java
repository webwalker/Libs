package com.webwalker.framework.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.webwalker.framework.beans.PinYinItem;
import com.webwalker.framework.system.contact.HanziToPinyin;
import com.webwalker.framework.system.contact.UnicodeGBK2Alpha;
import com.webwalker.framework.system.contact.HanziToPinyin.Token;
import android.text.TextUtils;

/**
 * 利用系统自带的拼音转换字母
 * 
 * http://blog.csdn.net/wwj_748/article/details/18215681
 * 
 * @author xu.jian
 * 
 */
public class PinYinUtil {
	public static String getPinYin(String input) {
		ArrayList<Token> tokens = HanziToPinyin.getInstance().get(input);
		StringBuilder sb = new StringBuilder();
		if (tokens != null && tokens.size() > 0) {
			for (Token token : tokens) {
				if (Token.PINYIN == token.type) {
					sb.append(token.target);
				} else {
					sb.append(token.source);
				}
			}
		}
		return sb.toString().toLowerCase();
	}

	/**
	 * 按号码-拼音搜索联系人
	 * 
	 * @param str
	 */
	public static ArrayList<PinYinItem> search(final String str,
			final ArrayList<PinYinItem> allContacts) {
		ArrayList<PinYinItem> contactList = new ArrayList<PinYinItem>();
		// 如果搜索条件以0 1 +开头则按号码搜索
		if (str.startsWith("0") || str.startsWith("1") || str.startsWith("+")) {
			for (PinYinItem contact : allContacts) {
				if (contact.getChineseFont() != null
						&& contact.getChineseFont() != null) {
					if (contact.getChineseFont().contains(str)
							|| contact.getChineseFont().contains(str)) {
						// contact.setGroup(str);
						contactList.add(contact);
					}
				}
			}
			return contactList;
		}

		// final ChineseSpelling finder = ChineseSpelling.getInstance();
		// finder.setResource(str);
		// final String result = finder.getSpelling();
		// 先将输入的字符串转换为拼音
		// final String result = PinYinUtil.getFullSpell(str);
		final String result = getPinYin(str);
		for (PinYinItem contact : allContacts) {
			if (contains(contact, result)) {
				contactList.add(contact);
			}
		}

		return contactList;
	}

	/**
	 * 根据拼音搜索
	 * 
	 * @param str
	 *            正则表达式
	 * @param pyName
	 *            拼音
	 * @param isIncludsive
	 *            搜索条件是否大于6个字符
	 * @return
	 */
	public static boolean contains(PinYinItem contact, String search) {
		if (TextUtils.isEmpty(contact.getChineseFont())
				|| TextUtils.isEmpty(search)) {
			return false;
		}

		boolean flag = false;

		// 简拼匹配,如果输入在字符串长度大于6就不按首字母匹配了
		if (search.length() < 6) {
			// String firstLetters = FirstLetterUtil.getFirstLetter(contact
			// .getName());
			// 获得首字母字符串
			String firstLetters = UnicodeGBK2Alpha
					.getSimpleCharsOfString(contact.getChineseFont());
			// String firstLetters =
			// PinYinUtil.getFirstSpell(contact.getName());
			// 不区分大小写
			Pattern firstLetterMatcher = Pattern.compile("^" + search,
					Pattern.CASE_INSENSITIVE);
			flag = firstLetterMatcher.matcher(firstLetters).find();
		}

		if (!flag) { // 如果简拼已经找到了，就不使用全拼了
			// 全拼匹配
			// ChineseSpelling finder = ChineseSpelling.getInstance();
			// finder.setResource(contact.getName());
			// 不区分大小写
			Pattern pattern2 = Pattern
					.compile(search, Pattern.CASE_INSENSITIVE);
			Matcher matcher2 = pattern2.matcher(getPinYin(contact
					.getChineseFont()));
			flag = matcher2.find();
		}

		return flag;
	}
}
