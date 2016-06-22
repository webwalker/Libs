package com.webwalker.framework.utils;

import com.webwalker.framework.beans.MessageEntry;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

/**
 * @author xu.jian
 * 
 */
public class DialogUtil {
	private static Context context;
	private static Activity act;
	private static ProgressDialog progressDialog;
	public static final int NO_ICON = -1; // 无图标

	public static void setContext(Context c) {
		context = c;
	}

	public static void setActivity(Activity a) {
		act = a;
	}

	/**
	 * 创建消息对话框
	 * 
	 * @param context
	 *            上下文 必填
	 * @param iconId
	 *            图标，如：R.drawable.icon 或 DialogTool.NO_ICON 必填
	 * @param title
	 *            标题 必填
	 * @param message
	 *            显示内容 必填
	 * @param btnName
	 *            按钮名称 必填
	 * @param listener
	 *            监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
	 * @return
	 */
	public static Dialog createMessageDialog(Context context, String title,
			String message, String btnName, OnClickListener listener, int iconId) {
		Dialog dialog = null;
		// R.style.dialog
		Builder builder = new Builder(context);

		if (iconId != NO_ICON) {
			// 设置对话框图标
			builder.setIcon(iconId);
		}
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置对话框标题
		builder.setTitle(title);
		// 设置对话框消息
		builder.setMessage(message);
		// 设置按钮
		builder.setPositiveButton(btnName, listener);
		// 创建一个消息对话框
		dialog = builder.create();

		return dialog;
	}

	/**
	 * 创建警示（确认、取消）对话框
	 * 
	 * @param context
	 *            上下文 必填
	 * @param iconId
	 *            图标，如：R.drawable.icon 或 DialogTool.NO_ICON 必填
	 * @param title
	 *            标题 必填
	 * @param message
	 *            显示内容 必填
	 * @param positiveBtnName
	 *            确定按钮名称 必填
	 * @param negativeBtnName
	 *            取消按钮名称 必填
	 * @param positiveBtnListener
	 *            监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
	 * @param negativeBtnListener
	 *            监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
	 * @return
	 */
	public static Dialog createConfirmDialog(Context context, String title,
			String message, String positiveBtnName, String negativeBtnName,
			OnClickListener positiveBtnListener,
			OnClickListener negativeBtnListener, int iconId) {
		Dialog dialog = null;
		Builder builder = new Builder(context);

		if (iconId != NO_ICON) {
			// 设置对话框图标
			builder.setIcon(iconId);
		}
		// 设置对话框标题
		builder.setTitle(title);
		// 设置对话框消息
		builder.setMessage(message);
		// 设置确定按钮
		builder.setPositiveButton(positiveBtnName, positiveBtnListener);
		// 设置取消按钮
		builder.setNegativeButton(negativeBtnName, negativeBtnListener);
		// 创建一个消息对话框
		dialog = builder.create();

		return dialog;
	}

	/**
	 * 创建单选对话框
	 * 
	 * @param context
	 *            上下文 必填
	 * @param iconId
	 *            图标，如：R.drawable.icon 或 DialogTool.NO_ICON 必填
	 * @param title
	 *            标题 必填
	 * @param itemsString
	 *            选择项 必填
	 * @param positiveBtnName
	 *            确定按钮名称 必填
	 * @param negativeBtnName
	 *            取消按钮名称 必填
	 * @param positiveBtnListener
	 *            监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
	 * @param negativeBtnListener
	 *            监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
	 * @param itemClickListener
	 *            监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
	 * @return
	 */
	public static Dialog createSingleChoiceDialog(Context context,
			String title, String[] itemsString, String positiveBtnName,
			String negativeBtnName, OnClickListener positiveBtnListener,
			OnClickListener negativeBtnListener,
			OnClickListener itemClickListener, int iconId) {
		Dialog dialog = null;
		Builder builder = new Builder(context);

		if (iconId != NO_ICON) {
			// 设置对话框图标
			builder.setIcon(iconId);
		}
		// 设置对话框标题
		builder.setTitle(title);
		// 设置单选选项, 参数0: 默认第一个单选按钮被选中
		builder.setSingleChoiceItems(itemsString, 0, itemClickListener);
		// 设置确定按钮
		builder.setPositiveButton(positiveBtnName, positiveBtnListener);
		// 设置确定按钮
		builder.setNegativeButton(negativeBtnName, negativeBtnListener);
		// 创建一个消息对话框
		dialog = builder.create();

		return dialog;
	}

	/**
	 * 创建复选对话框
	 * 
	 * @param context
	 *            上下文 必填
	 * @param iconId
	 *            图标，如：R.drawable.icon 或 DialogTool.NO_ICON 必填
	 * @param title
	 *            标题 必填
	 * @param itemsString
	 *            选择项 必填
	 * @param positiveBtnName
	 *            确定按钮名称 必填
	 * @param negativeBtnName
	 *            取消按钮名称 必填
	 * @param positiveBtnListener
	 *            监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
	 * @param negativeBtnListener
	 *            监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
	 * @param itemClickListener
	 *            监听器，需实现android.content.DialogInterface.
	 *            OnMultiChoiceClickListener;接口 必填
	 * @return
	 */
	public static Dialog createMultiChoiceDialog(Context context, String title,
			String[] itemsString, String positiveBtnName,
			String negativeBtnName, OnClickListener positiveBtnListener,
			OnClickListener negativeBtnListener,
			OnMultiChoiceClickListener itemClickListener, int iconId) {
		Dialog dialog = null;
		Builder builder = new Builder(context);

		if (iconId != NO_ICON) {
			// 设置对话框图标
			builder.setIcon(iconId);
		}
		// 设置对话框标题
		builder.setTitle(title);
		// 设置选项
		builder.setMultiChoiceItems(itemsString, null, itemClickListener);
		// 设置确定按钮
		builder.setPositiveButton(positiveBtnName, positiveBtnListener);
		// 设置确定按钮
		builder.setNegativeButton(negativeBtnName, negativeBtnListener);
		// 创建一个消息对话框
		dialog = builder.create();

		return dialog;
	}

	/**
	 * 创建列表对话框
	 * 
	 * @param context
	 *            上下文 必填
	 * @param iconId
	 *            图标，如：R.drawable.icon 或 DialogTool.NO_ICON 必填
	 * @param title
	 *            标题 必填
	 * @param itemsString
	 *            列表项 必填
	 * @param negativeBtnName
	 *            取消按钮名称 必填
	 * @param negativeBtnListener
	 *            监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
	 * @return
	 */
	public static Dialog createListDialog(Context context, String title,
			String[] itemsString, String negativeBtnName,
			OnClickListener negativeBtnListener,
			OnClickListener itemClickListener, int iconId) {
		Dialog dialog = null;
		Builder builder = new Builder(context);

		if (iconId != NO_ICON) {
			// 设置对话框图标
			builder.setIcon(iconId);
		}
		// 设置对话框标题
		builder.setTitle(title);
		// 设置列表选项
		builder.setItems(itemsString, itemClickListener);
		// 设置确定按钮
		builder.setNegativeButton(negativeBtnName, negativeBtnListener);
		// 创建一个消息对话框
		dialog = builder.create();

		return dialog;
	}

	/**
	 * 创建自定义（含确认、取消）对话框
	 * 
	 * @param context
	 *            上下文 必填
	 * @param iconId
	 *            图标，如：R.drawable.icon 或 DialogTool.NO_ICON 必填
	 * @param title
	 *            标题 必填
	 * @param positiveBtnName
	 *            确定按钮名称 必填
	 * @param negativeBtnName
	 *            取消按钮名称 必填
	 * @param positiveBtnListener
	 *            监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
	 * @param negativeBtnListener
	 *            监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
	 * @param view
	 *            对话框中自定义视图 必填
	 * @return
	 */
	public static Dialog createRandomDialog(Context context, String title,
			String positiveBtnName, String negativeBtnName,
			OnClickListener positiveBtnListener,
			OnClickListener negativeBtnListener, View view, int iconId) {
		Dialog dialog = null;
		Builder builder = new Builder(context);

		if (iconId != NO_ICON) {
			// 设置对话框图标
			builder.setIcon(iconId);
		}
		// 设置对话框标题
		builder.setTitle(title);
		builder.setView(view);
		// 设置确定按钮
		builder.setPositiveButton(positiveBtnName, positiveBtnListener);
		// 设置确定按钮
		builder.setNegativeButton(negativeBtnName, negativeBtnListener);
		// 创建一个消息对话框
		dialog = builder.create();

		return dialog;
	}

	/**
	 * 创建普通对话框
	 * 
	 * @param ctx
	 *            上下文 必填
	 * @param iconId
	 *            图标，如：R.drawable.icon 必填
	 * @param title
	 *            标题 必填
	 * @param message
	 *            显示内容 必填
	 * @param btnName
	 *            按钮名称 必填
	 * @param listener
	 *            监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
	 * @return
	 */
	public static Dialog createNormalDialog(Context ctx, int iconId,
			String title, String message, String btnName,
			OnClickListener listener) {
		Dialog dialog = null;
		Builder builder = new Builder(
				ctx);
		// 设置对话框的图标
		builder.setIcon(iconId);
		// 设置对话框的标题
		builder.setTitle(title);
		// 设置对话框的显示内容
		builder.setMessage(message);
		// 添加按钮，android.content.DialogInterface.OnClickListener.OnClickListener
		builder.setPositiveButton(btnName, listener);
		// 创建一个普通对话框
		dialog = builder.create();
		return dialog;
	}

	/**
	 * 创建列表对话框
	 * 
	 * @param ctx
	 *            上下文 必填
	 * @param iconId
	 *            图标，如：R.drawable.icon 必填
	 * @param title
	 *            标题 必填
	 * @param itemsId
	 *            字符串数组资源id 必填
	 * @param listener
	 *            监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
	 * @return
	 */
	public static Dialog createListDialog(Context ctx, int iconId,
			String title, int itemsId, OnClickListener listener) {
		Dialog dialog = null;
		Builder builder = new Builder(
				ctx);
		// 设置对话框的图标
		builder.setIcon(iconId);
		// 设置对话框的标题
		builder.setTitle(title);
		// 添加按钮，android.content.DialogInterface.OnClickListener.OnClickListener
		builder.setItems(itemsId, listener);
		// 创建一个列表对话框
		dialog = builder.create();
		return dialog;
	}

	/**
	 * 创建单选按钮对话框
	 * 
	 * @param ctx
	 *            上下文 必填
	 * @param iconId
	 *            图标，如：R.drawable.icon 必填
	 * @param title
	 *            标题 必填
	 * @param itemsId
	 *            字符串数组资源id 必填
	 * @param listener
	 *            单选按钮项监听器，需实现android.content.DialogInterface.OnClickListener接口
	 *            必填
	 * @param btnName
	 *            按钮名称 必填
	 * @param listener2
	 *            按钮监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
	 * @return
	 */
	public static Dialog createRadioDialog(Context ctx, int iconId,
			String title, int itemsId, OnClickListener listener,
			String btnName, OnClickListener listener2) {
		Dialog dialog = null;
		Builder builder = new Builder(
				ctx);
		// 设置对话框的图标
		builder.setIcon(iconId);
		// 设置对话框的标题
		builder.setTitle(title);
		// 0: 默认第一个单选按钮被选中
		builder.setSingleChoiceItems(itemsId, 0, listener);
		// 添加一个按钮
		builder.setPositiveButton(btnName, listener2);
		// 创建一个单选按钮对话框
		dialog = builder.create();
		return dialog;
	}

	/**
	 * 创建复选对话框
	 * 
	 * @param ctx
	 *            上下文 必填
	 * @param iconId
	 *            图标，如：R.drawable.icon 必填
	 * @param title
	 *            标题 必填
	 * @param itemsId
	 *            字符串数组资源id 必填
	 * @param flags
	 *            初始复选情况 必填
	 * @param listener
	 *            单选按钮项监听器，需实现android.content.DialogInterface.
	 *            OnMultiChoiceClickListener接口 必填
	 * @param btnName
	 *            按钮名称 必填
	 * @param listener2
	 *            按钮监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
	 * @return
	 */
	public static Dialog createCheckBoxDialog(
			Context ctx,
			int iconId,
			String title,
			int itemsId,
			boolean[] flags,
			OnMultiChoiceClickListener listener,
			String btnName, OnClickListener listener2) {
		Dialog dialog = null;
		Builder builder = new Builder(
				ctx);
		// 设置对话框的图标
		builder.setIcon(iconId);
		// 设置对话框的标题
		builder.setTitle(title);
		builder.setMultiChoiceItems(itemsId, flags, listener);
		// 添加一个按钮
		builder.setPositiveButton(btnName, listener2);
		// 创建一个复选对话框
		dialog = builder.create();
		return dialog;
	}

	public void ShowDialog(int title, int message, int ok, int cancel,
			final OnClickListener listener) {
		ShowDialog(context.getResources().getString(title), context
				.getResources().getString(message), ok, cancel, listener);
	}

	public void ShowDialog(String title, String message, int ok, int cancel) {
		ShowDialog(title, message, ok, cancel, null);
	}

	public void ShowDialog(int title, String message, int ok, int cancel,
			final OnClickListener listener) {
		ShowDialog(context.getResources().getString(title), message, ok,
				cancel, listener);
	}

	public void ShowDialog(String title, String message, int ok, int cancel,
			final OnClickListener listener) {
		Builder builder = new Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		if (listener != null) {
			builder.setPositiveButton(ok, listener);
			builder.setNegativeButton(cancel, listener);
		}
		Dialog noticeDialog = builder.create();
		noticeDialog.show();
	}

	public void showNoticeDialog(String title, String message, String ok) {
		Builder builder = new Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(ok, new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		Dialog noticeDialog = builder.create();
		noticeDialog.show();
	}

	public static void showAlert(Context context, MessageEntry entry,
			OnClickListener listener, OnCancelListener cancelListener) {
		formatMessage(context, entry);
		ShowDialog(context, entry.getTitle(), entry.getMessage(),
				entry.getOkText(), entry.getCancelText(), entry.isCancelable(),
				null, listener, null, cancelListener);
	}

	public static void confirmDialog(Context context, MessageEntry entry) {
		showCustomDialog(context, entry, null);
	}

	public static void showCustomDialog(Context context, MessageEntry entry,
			OnClickListener listener) {
		// load layout
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(entry.getDialogLayoutId(), null);
		// show
		ShowDialog(context, entry.getTitle(), entry.getMessage(),
				entry.getOkText(), entry.getCancelText(), entry.isCancelable(),
				layout, listener, null, null);
	}

	public static void showCustomView(Context context, MessageEntry entry,
			OnClickListener listener) {
		ShowDialog(context, entry.getTitle(), entry.getMessage(),
				entry.getOkText(), entry.getCancelText(), entry.isCancelable(),
				entry.getV(), listener, null, null);
	}

	public static void ShowDialog(Context context, String title,
			String message, String ok, String cancel, boolean cancelable,
			View v, final OnClickListener listener,
			final OnClickListener negativeListener,
			final OnCancelListener cancelListener) {
		Builder builder = new Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		if (v != null)
			builder.setView(v);
		builder.setCancelable(cancelable); // 响应按back键的事件
		if (listener != null)
			builder.setPositiveButton(ok, listener);
		if (negativeListener != null)
			builder.setNegativeButton(cancel, negativeListener);
		if (cancelListener != null)
			builder.setOnCancelListener(cancelListener);
		Dialog noticeDialog = builder.create();
		noticeDialog.show();
	}

	public void ShowExit(final Activity act, Context context, MessageEntry entry) {
		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				switch (arg1) {
				case DialogInterface.BUTTON_POSITIVE:// YES
					act.finish();
					int nPid = android.os.Process.myPid();
					android.os.Process.killProcess(nPid);
					break;
				case DialogInterface.BUTTON_NEGATIVE:// NO
					arg0.cancel();
					break;
				}
			}
		};

		ShowDialog(context, entry.getTitle(), entry.getMessage(),
				entry.getOkText(), entry.getCancelText(), entry.isCancelable(),
				null, listener, null, null);
	}

	/**
	 * 退出应用程序
	 */
	public void ShowExitDialog(final Activity act, int titleId, int msgId,
			int ok, int cancel) {
		Builder alertbBuilder = new Builder(context);
		alertbBuilder
				.setTitle(titleId)
				.setMessage(msgId)
				.setPositiveButton(ok, new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						act.finish();
						int nPid = android.os.Process.myPid();
						android.os.Process.killProcess(nPid);
					}
				})
				.setNegativeButton(cancel,
						new OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						}).create();
		alertbBuilder.show();
	}

	private static void formatMessage(Context context, MessageEntry entry) {
		if (entry.getTitleId() != 0) {
			entry.setTitle(context.getResources().getString(entry.getTitleId()));
		}
		if (entry.getMessageId() != 0) {
			entry.setMessage(context.getResources().getString(
					entry.getMessageId()));
		}

		if (entry.getOkTextId() != 0) {
			entry.setOkText(context.getResources().getString(
					entry.getOkTextId()));
		}
		if (entry.getCancelTextId() != 0) {
			entry.setCancelText((context.getResources().getString(entry
					.getCancelTextId())));
		}
	}

	public static void progressDialogShow(Context context, String message) {
		progressDialogShow(context, message, false);
	}

	public static void progressDialogShow(Context context, String message,
			boolean cancelAble) {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(context);
			progressDialog.setIndeterminate(true);
			progressDialog.setCancelable(cancelAble);
		}
		progressDialog.setMessage(message);
	}

	public static void progressDialogHide(Context context, String message) {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}
}
