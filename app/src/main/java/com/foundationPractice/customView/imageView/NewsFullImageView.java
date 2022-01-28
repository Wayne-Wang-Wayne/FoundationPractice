package com.setDDG.customView.imageView;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * 可以完整顯示圖片
 */
public class NewsFullImageView extends AppCompatImageView {
    public NewsFullImageView(Context context) {
        super(context);
    }

    public NewsFullImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewsFullImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = 0;
        if (getDrawable() != null) {
            height = width * getDrawable().getIntrinsicHeight() / getDrawable().getIntrinsicWidth();
        }
        setMeasuredDimension(width, height);
    }

}
