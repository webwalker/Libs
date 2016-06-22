package com.webwalker.framework.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UIUtils {
    private static String TAG = "UI_UTILS";
    private static CharacterStyle fontSpan = new ForegroundColorSpan(Color.BLUE);
    private static boolean exitFlag = false;

    public static void exit(Context context, int msgId, Handler handler) {
        if (exitFlag) {
            handler.removeCallbacksAndMessages(null);
            System.exit(0);
        } else {
            exitFlag = true;
            MessageUtil.showToast(context, msgId, Gravity.BOTTOM);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    exitFlag = false;
                }
            }, 3000);
        }
    }

    public static void finishActivity(Activity at, boolean bForce) {
        Loggers.d(TAG, "enter finishActivity. bForce=" + bForce);
        if (null == at) {
            return;
        }

        if (bForce) {
            at.finish();
            Loggers.d(TAG, "leave finishActivity. finished");
            return;
        }
        Loggers.d(TAG, "leave finishActivity");
    }

    public static int getScale(WindowManager manager) {
        int scale = 100;
        Point view = getDisplaySize(manager);
        Point page = getPageSize();

        scale = 100 * view.x / page.x;
        Loggers.d(TAG, "getScale : " + scale);
        return scale;
    }

    static public Point getPageSize() {
        return new Point(1280, 720);
    }

    public static Point getDisplaySize(WindowManager manager) {
        DisplayMetrics dm = new DisplayMetrics();
        Display dp = manager.getDefaultDisplay();
        dp.getMetrics(dm);
        Loggers.d(TAG, "getDisplaySize : widthPixels = " + dm.widthPixels
                + ", heightPixels = " + dm.heightPixels + ", density = "
                + dm.density);
        Loggers.d(TAG, "densityDpi=" + dm.densityDpi + ", xdpi=" + dm.xdpi
                + ", density=" + dm.density);

        return new Point(dm.widthPixels, dm.heightPixels);
    }

    public static float getDpi(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param fontScale （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 适配不同分辨率，好用
     * <p/>
     * 不同分辨率下，通过dimen来调节，不需要也没办法以参考标准进行动态计算
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int pix2dp(Context context, int pxValue) {
        final float density = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue * density);
    }

    public static View getView(Context context, int layouId) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(layouId, null);
        return v;
    }

    public static int getColor(String color) {
        int textColor = 0;
        try {
            textColor = Color.parseColor(color);
        } catch (Exception e) {
            e.printStackTrace();
            textColor = Color.parseColor("#FFFFFF");
        }
        return textColor;
    }

    public static void setTextSize(Context context, TextView tv, int dimen) {
        tv.setTextSize(context.getResources().getDimensionPixelSize(dimen));
        // tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, context.getResources()
        // .getDimensionPixelSize(dimen));
    }

    public static void showVersionText(ViewGroup vg, Context context,
                                       WindowManager manager) {
        try {
            TextView tv = new TextView(context);
            tv.setText("V " + AppUtil.getVersionName(context));
            tv.setTextSize(26);
            tv.setTextColor(Color.WHITE);
            vg.addView(tv);

            Point p = getDisplaySize(manager);
            // tv.setX(p.x - 120);
            // tv.setY(p.y - 50);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setVisibility(View view, int visibility) {
        view.setVisibility(visibility);
        if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view;
            for (int i = 0; i < vg.getChildCount(); i++) {
                setVisibility(vg.getChildAt(i), visibility);
            }
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static final ViewBinder checkboxViewBinder = new ViewBinder() {

        @Override
        public boolean setViewValue(View view, Object data,
                                    String textRepresentation) {
            if (view instanceof CheckedTextView) {
                CheckedTextView ctv = (CheckedTextView) view;
                ctv.setText(textRepresentation);
                return true;
            } else {
                return false;
            }
        }
    };

    public static SpinnerAdapter createSimpleSpinnerAdapter(Context belongTo,
                                                            List<Map<String, ?>> list, String labelKey) {
        if (list == null) {
            list = new ArrayList<Map<String, ?>>();
        }
        SimpleAdapter sa = new SimpleAdapter(belongTo, list,
                android.R.layout.simple_spinner_item,
                new String[]{labelKey}, new int[]{android.R.id.text1});
        sa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sa.setViewBinder(checkboxViewBinder);
        return sa;
    }

    /**
     * 当当前字符达到多少时,自动跳转到下一个View
     *
     * @param next
     * @param length
     * @return
     */
    public static TextWatcher getTextWatcher(final View next, final int length) {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s != null && s.toString().length() == length) {
                    next.setFocusable(true);
                    next.setFocusableInTouchMode(true);
                    next.requestFocus();
                }
            }
        };
        return watcher;
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    public static int computeInitialSampleSize(BitmapFactory.Options options,
                                               int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    public static int dip2pixel(Context context, float n) {
        int value = (int) TypedValue.applyDimension(1, n, context
                .getResources().getDisplayMetrics());
        return value;
    }

    public static float pixel2dip(Context context, float n) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = n / (metrics.densityDpi / 160.0F);
        return dp;
    }

    public static void transparent(View view, boolean transparent) {
        float alpha = 1.0F;
        if (transparent)
            alpha = 0.5F;

        setAlpha(view, alpha);
    }

    private static void setAlpha(View view, float alphaValue) {
        if (alphaValue == 1.0F) {
            view.clearAnimation();
        } else {
            AlphaAnimation alpha = new AlphaAnimation(alphaValue, alphaValue);
            alpha.setDuration(0L);
            alpha.setFillAfter(true);
            view.startAnimation(alpha);
        }
    }

    public void resetEditText(ViewGroup view) {
        // 全局遍历 需要为每个控件设定ID
        for (int i = 0; i < view.getChildCount(); i++) {
            View v1 = view.getChildAt(i);
            if (v1 instanceof EditText) {
                EditText e = (EditText) view.findViewById(v1.getId());
                e.setText("");
            }
        }
    }

    public static void bindTagEvent(ViewGroup vg, OnClickListener listener) {
        for (int i = 0; i < vg.getChildCount(); i++) {
            View v = vg.getChildAt(i);
            if (v.getTag() != null)
                v.setOnClickListener(listener);
            if (v instanceof ViewGroup)
                bindTagEvent((ViewGroup) v, listener);
        }
    }

    /**
     * highlight programs title
     *
     * @param tvTitle
     */
    public static SpannableStringBuilder getHighLightText(String originText,
                                                          String fullIndex, String searchkeys) {
        try {
            if (originText == null)
                return null;
            Pattern pattern = Pattern.compile(searchkeys,
                    Pattern.CASE_INSENSITIVE);
            Matcher m = pattern.matcher(fullIndex); // 从全索引中匹配要搜索的内容
            SpannableStringBuilder spannable = new SpannableStringBuilder(
                    originText);
            if (m.find()) {
                spannable.setSpan(fontSpan, m.start(), m.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                return spannable;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 是否显示引导界面
     *
     * @param source Splash Activity
     * @param target 目标Activity
     * @return
     */
    public static boolean isShowSplashUI(Activity source, Class target) {
        // 是否显示引导界面
        int lastVersionCode = SharedUtil.getInstance(source).getInt(
                "versionCode");
        int currentVersionCode = AppUtil.getVersionCode(source);
        if (lastVersionCode != currentVersionCode) {
            SharedUtil.getInstance(source).set("versionCode",
                    currentVersionCode);
            return true;
        } else {
            UriUtil.startActivityNoHistory(source, target);
            source.finish();
            return false;
        }
    }

    private static long lastClickTime;

    public synchronized static boolean isFastDoubleClick() {
        return isFastDoubleClick(800);
    }

    public synchronized static boolean isFastDoubleClick(long times) {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < times) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

}
