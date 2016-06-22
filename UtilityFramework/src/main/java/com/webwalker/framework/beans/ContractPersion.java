package com.webwalker.framework.beans;

/**
 * @author xu.jian
 * 
 */
public class ContractPersion extends BaseContract {
	private int contactId;
	private String sortKey;
	private String lookUpKey;
	private int selected = 0;
	private String formattedNumber;
	private String pinyin;

	public int getContactId() {
		return contactId;
	}

	public void setContactId(int contactId) {
		this.contactId = contactId;
	}

	public String getSortKey() {
		return sortKey;
	}

	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}

	public String getLookUpKey() {
		return lookUpKey;
	}

	public void setLookUpKey(String lookUpKey) {
		this.lookUpKey = lookUpKey;
	}

	public int getSelected() {
		return selected;
	}

	public void setSelected(int selected) {
		this.selected = selected;
	}

	public String getFormattedNumber() {
		return formattedNumber;
	}

	public void setFormattedNumber(String formattedNumber) {
		this.formattedNumber = formattedNumber;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		ContractPersion p = (ContractPersion) o;
		if (p == null)
			return super.equals(o);

		if (this.getName().equals(p.getName())
				&& this.getPhone().equals(p.getPhone()))
			return true;
		return super.equals(o);
	}
}
