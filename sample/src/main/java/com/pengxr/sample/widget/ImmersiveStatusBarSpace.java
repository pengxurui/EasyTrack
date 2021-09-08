package com.pengxr.sample.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import java.lang.reflect.Field;

public class ImmersiveStatusBarSpace extends View {

    private int height;

    public ImmersiveStatusBarSpace(Context context) {
        this(context, (AttributeSet) null);
    }

    public ImmersiveStatusBarSpace(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImmersiveStatusBarSpace(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.height = Build.VERSION.SDK_INT >= 19 ? getStatusBarHeight(this.getContext()) : 0;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.setMeasuredDimension(this.getMeasuredWidth(), this.height);
    }

    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);
        } catch (Exception E) {
            E.printStackTrace();
        }
        return sbar;
    }
}
