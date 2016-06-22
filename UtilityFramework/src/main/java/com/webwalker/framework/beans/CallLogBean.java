package com.webwalker.framework.beans;

import java.util.Date;

/**
 * 通话记录
 * 
 * @author Administrator
 * 
 */
public class CallLogBean extends BaseContract {
	private int id;
	private Date date; // 日期
	private int type; // 来电:1，拨出:2,未接:3
	private int count; // 通话次数

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
