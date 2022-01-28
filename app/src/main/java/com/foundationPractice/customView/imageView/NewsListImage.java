package com.setDDG.customView.imageView;

import android.content.Context;
import android.util.AttributeSet;

import com.setDDG.util.PhoneSizeUtil;


public class NewsListImage extends NewsItemImageView {
    public NewsListImage(Context context) {
        this(context, null);
    }

    public NewsListImage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewsListImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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
