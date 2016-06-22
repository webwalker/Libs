package com.webwalker.framework.utils;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

/**
 * @author xu.jian
 *
 */
public class UriUtil {
    private final static String TAG = "UriUtil";
    private static Context context = null;
    public static int DEFAULT_ENTER_ANIM;
    public static int DEFAULT_EXIT_ANIM;
    public static Intent intent;

    public static void setContext(Context c) {
        context = c;
    }

    public static void startActivity(Activity act, String className) {
        act.startActivity(new Intent(className));
    }

    public static void startActAndFinish(Activity act, Class c) {
        act.startActivity(new Intent(act.getBaseContext(), c));
        act.finish();
    }

    public static void startActivityNoHistory(Context context, Class c) {
        Intent it = new Intent(context, c);
        it.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        context.startActivity(new Intent(context, c));
    }

    public static void startActivityClearTop(Context context, Class c) {
        Intent it = new Intent(context, c);
        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(new Intent(context, c));
    }

    public static void startActivityClearTask(Context context, Class c) {
        Intent it = new Intent(context, c);
        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(new Intent(context, c));
    }

    public static void startMsgActivity(Context context, Class c, String msg) {
        Intent it = new Intent(context, c);
        it.putExtra("msg", msg);
        context.startActivity(it);
    }

    public static void startMsgActivityNewTask(Context context, Class c,
                                               String msg) {
        Intent it = new Intent(context, c);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        it.putExtra("msg", msg);
        context.startActivity(it);
    }

    public static void startActivity(Context context, Class<? extends Activity> toklass) {
        Intent intent = new Intent(context, toklass);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Class<? extends Activity> toklass, Bundle extras) {
        Intent intent = new Intent(context, toklass);
        intent.putExtras(extras);
        context.startActivity(intent);
    }

    public static void startActivityForResult(Activity fromActivity, Class<? extends Activity> toklass,
                                              int requestCode) {
        Intent intent = new Intent(fromActivity, toklass);
        fromActivity.startActivityForResult(intent, requestCode);
    }

    public static void startActivityForResult(Activity fromActivity, Class<? extends Activity> toklass,
                                              int requestCode, Bundle extras) {
        Intent intent = new Intent(fromActivity, toklass);
        intent.putExtras(extras);
        fromActivity.startActivityForResult(intent, requestCode);
    }

    public static void intentDIY(Activity activity, Class<?> classes) {
        IntentDIY(activity, classes, null, DEFAULT_ENTER_ANIM,
                DEFAULT_EXIT_ANIM);
    }

    public static void IntentDIY(Activity activity, Class<?> classes,
                                 Map<String, String> paramMap, int enterAnim, int exitAnim) {
        intent = new Intent(activity, classes);
        organizeAndStart(activity, classes, paramMap);
        if (enterAnim != 0 && exitAnim != 0) {
            activity.overridePendingTransition(enterAnim, exitAnim);
        }
    }

    public static void intentSysDefault(Activity activity, Class<?> classes,
                                        Map<String, String> paramMap) {
        organizeAndStart(activity, classes, paramMap);
    }

    private static void organizeAndStart(Activity activity, Class<?> classes,
                                         Map<String, String> paramMap) {
        intent = new Intent(activity, classes);
        if (null != paramMap) {
            Set<String> set = paramMap.keySet();
            for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
                String key = iterator.next();
                intent.putExtra(key, paramMap.get(key));
            }
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }

    public static Serializable getSerializableValue(Activity act, String key) {
        try {
            return act.getIntent().getSerializableExtra(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getStringValue(Activity act, String key) {
        try {
            return act.getIntent().getStringExtra(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int getIntValue(Activity act, String key) {
        try {
            return act.getIntent().getIntExtra(key, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void redirect(String packageName, String className) {
        Loggers.d("redirecting..." + packageName + "," + className);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        ComponentName componentName = new ComponentName(packageName, className);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setComponent(componentName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        context.startActivity(intent);
    }

    public static int uriForward(Context context, String uri) {
        Loggers.d("uri:" + uri);
        final int FORWARD_ERROR_CODE = -1;
        final int FORWARD_SUCCESS_CODE = 1;

        String actionName = null;
        String param = null;
        String activityId = null;

        if (null != uri && !"".equals(uri)) {

            String[] ss = uri.split(":");
            if (ss.length < 1) {
                Loggers.w("the uri is null or empty");
                return FORWARD_ERROR_CODE;
            }

            if (ss.length > 2) {
                activityId = ss[2];
            }
            if (ss.length > 1) {
                param = ss[1];
            }
            if (ss.length > 0) {
                actionName = ss[0];
            }

            if (null != actionName && !"".equals(actionName)) {
                try {
                    Loggers.d("the uri detail is: actionName=" + actionName
                            + ",param=" + param + ",activityId=" + activityId);
                    Intent intent = new Intent();
                    intent.setAction(actionName);
                    intent.putExtra("param", param);
                    intent.putExtra("activityId", activityId);
                    context.startActivity(intent);

                    return FORWARD_SUCCESS_CODE;
                } catch (Exception e) {
                    Loggers.w("start activity fail , actionName is ="
                            + actionName);
                    return FORWARD_ERROR_CODE;
                }
            } else {
                Loggers.w("the uri actionName is null or empty");
                return FORWARD_ERROR_CODE;
            }
        } else {
            Loggers.w("the uri is null or empty");
            return FORWARD_ERROR_CODE;
        }

    }

    public static void callPhone(Context ctx, String number) {
        callPhone(ctx, number, null);
    }

    @SuppressWarnings("MissingPermission")
    public static void callPhone(Context ctx, String number, String data) {
        Loggers.d(TAG, "call number:" + number);
        Uri uri = Uri.parse("tel:" + number);
        Intent intent = new Intent(Intent.ACTION_CALL, uri);// ACTION_DIAL 让用户选择
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (!TextUtils.isEmpty(data))
            intent.putExtra("data", data);
        ctx.startActivity(intent);
    }

    public static void sendMail(Context c, String to, String title, String text) {
        Intent data = new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse("mailto:" + to));
        // data.putExtra(Intent.EXTRA_EMAIL, new String[] { "Jason-Xu@msn.com",
        // "14876534@qq.com" });
        // data.putExtra(Intent.EXTRA_CC, new String[] { "" });
        // data.putExtra(Intent.EXTRA_BCC, new String[] { "" });
        data.putExtra(Intent.EXTRA_SUBJECT, title);
        data.putExtra(Intent.EXTRA_TEXT, text);
        c.startActivity(data);
        // c.startActivity(Intent.createChooser(data, "选择邮件服务器"));
    }

    public static void url(Context context, String url) {
        Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(
                    "com.UCMobile", 0);
            if (pi != null) {
                // UC浏览器
                AppUtil.startAppByPackageName(context, "com.UCMobile", it);
                return;
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        it.setClassName("com.android.browser",
                "com.android.browser.BrowserActivity");
        context.startActivity(it);
    }
}
