package com.pengxr.sample.utils;

import android.content.Context;

public class DensityUtil {

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}