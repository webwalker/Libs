package com.webwalker.framework.beans;

import java.io.Serializable;
import java.util.List;

/**
 * @author xu.jian
 * 
 */
public class ListViewItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ListViewItem() {

	}

	// 在ListView中的索引
	private int listIndex = 0;
	private List<Object> ext;
	private boolean isChecked;
	private String id = "0";

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	public void setListIndex(int index) {
		this.listIndex = index;
	}

	public int getListIndex() {
		return listIndex;
	}

	public void setExt(List<Object> ext) {
		this.ext = ext;
	}

	public List<Object> getExt() {
		return ext;
	}

	public Object getExt(int index) {
		return ext.get(index);
	}

	public void setIsChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public boolean getIsChecked() {
		return isChecked;
	}
}