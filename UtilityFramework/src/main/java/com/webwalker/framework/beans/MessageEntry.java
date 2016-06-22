package com.webwalker.framework.beans;

import android.view.View;
import android.view.ViewGroup;

/**
 * @author xu.jian
 * 
 */
public class MessageEntry {
	private boolean cancelable = true;
	private String title;
	private String message;
	private String okText;
	private String cancelText;
	private int titleId;
	private int messageId;
	private int okTextId;
	private int cancelTextId;
	private int dialogLayoutId;
	private int rootViewId;
	private ViewGroup rootView;
	private View v;

	public MessageEntry(String message, String okText, int okTextId,
			int layoutId, int viewId) {
		this.message = message;
		this.okText = okText;
		this.okTextId = okTextId;
		this.dialogLayoutId = layoutId;
		this.rootViewId = viewId;
	}

	public MessageEntry(String message, String okText, int okTextId,
			int dialogLayoutId) {
		this.message = message;
		this.okText = okText;
		this.okTextId = okTextId;
		this.dialogLayoutId = dialogLayoutId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getOkText() {
		return okText;
	}

	public void setOkText(String okText) {
		this.okText = okText;
	}

	public String getCancelText() {
		return cancelText;
	}

	public void setCancelText(String cancelText) {
		this.cancelText = cancelText;
	}

	public int getTitleId() {
		return titleId;
	}

	public void setTitleId(int titleId) {
		this.titleId = titleId;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public int getOkTextId() {
		return okTextId;
	}

	public void setOkTextId(int okTextId) {
		this.okTextId = okTextId;
	}

	public int getCancelTextId() {
		return cancelTextId;
	}

	public void setCancelTextId(int cancelTextId) {
		this.cancelTextId = cancelTextId;
	}

	public boolean isCancelable() {
		return cancelable;
	}

	public void setCancelable(boolean cancelable) {
		this.cancelable = cancelable;
	}

	public int getDialogLayoutId() {
		return dialogLayoutId;
	}

	public void setDialogLayoutId(int dialogLayoutId) {
		this.dialogLayoutId = dialogLayoutId;
	}

	public int getRootViewId() {
		return rootViewId;
	}

	public void setRootViewId(int rootViewId) {
		this.rootViewId = rootViewId;
	}

	public View getV() {
		return v;
	}

	public void setV(View v) {
		this.v = v;
	}

	public ViewGroup getRootView() {
		return rootView;
	}

	public void setRootView(ViewGroup rootView) {
		this.rootView = rootView;
	}
}
