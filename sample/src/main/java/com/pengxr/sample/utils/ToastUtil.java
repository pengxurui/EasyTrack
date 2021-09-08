package com.pengxr.sample.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by pengxr on 5/9/2021
 */
public class ToastUtil {

    public static void toast(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }
}