package com.webwalker.framework.beans;

/**
 * 汉字转拼音、首字母实体类
 * 
 * @author xujian
 * 
 */
public class PinYinItem {
	private String tag;
	private String chineseFont;
	private String fullSpell;
	private String firstLetter;
	private String index;

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getChineseFont() {
		return chineseFont;
	}

	public void setChineseFont(String chineseFont) {
		this.chineseFont = chineseFont;
	}

	public String getFullSpell() {
		return fullSpell;
	}

	public void setFullSpell(String fullSpell) {
		this.fullSpell = fullSpell;
	}

	public String getFirstLetter() {
		return firstLetter;
	}

	public void setFirstLetter(String firstLetter) {
		this.firstLetter = firstLetter;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}
}
