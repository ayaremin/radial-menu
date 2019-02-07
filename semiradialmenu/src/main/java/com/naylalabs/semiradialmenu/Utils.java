package com.naylalabs.semiradialmenu;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class Utils {

    public static Utils utils;
    private static Context mContext;
    private View view;

    public Utils(Context context) {
        this.mContext = context;
    }

    public static Utils getInstance(Context context) {
        if (utils == null) {
            utils = new Utils(context);
        }
        return utils;
    }

    public int dpTopixel(int dp) {
        float dip = (float) dp;
        Resources r = mContext.getResources();
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dip,
                r.getDisplayMetrics()
        );
        return (int) px;
    }

    public int px2dp(int px) {
        float pix = (float) px;
        Resources r = mContext.getResources();
        float pixel = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_PX,
                pix,
                r.getDisplayMetrics()
        );
        return (int) pixel;
    }

    public int getScreenWidth() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        return display.getWidth();
    }

    public int getScreenHeight() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        return display.getHeight();
    }

    public int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        mContext.getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true);
        final DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        int myPaddingStart = (int) typedValue.getDimension(metrics);
        return myPaddingStart;
    }

    public void setCenterView (View view) {
        this.view = view;
    }

    public View getCenterView () {
        return view;
    }
}
