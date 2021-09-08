package com.pengxr.sample.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * Square picture with Height adaptive.
 */
public class HeightAutoFitSquareImageView extends AppCompatImageView {

    public HeightAutoFitSquareImageView(Context context) {
        super(context);
    }

    public HeightAutoFitSquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeightAutoFitSquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredW = getMeasuredWidth();
        setMeasuredDimension(measuredW, measuredW);
    }
}
