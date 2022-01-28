package com.foundationPractice.customView.imageView;

import android.content.Context;
import android.util.AttributeSet;

import com.foundationPractice.util.PhoneSizeUtil;


public class ListImage extends ProportionImageView {
    public ListImage(Context context) {
        super(context);
    }

    public ListImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = (int) (PhoneSizeUtil.Companion.getInstance(getContext()).getPhoneWidth() / 2.2);
        int height = 0;
        if (getDrawable() != null && width != 0) {
            height = width * 9 / 16;
        }
        setMeasuredDimension(width, height);
    }

}
