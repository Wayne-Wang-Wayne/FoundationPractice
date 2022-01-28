package com.setDDG.customView.imageView;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * 可以將圖片比例用成16:9
 */
public class ProportionImageView extends AppCompatImageView {

    public ProportionImageView(Context context) {
        super(context);
    }

    public ProportionImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProportionImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = 0;
        if (getDrawable() != null && width != 0) {
            height = width * 9 / 16;
        }
        setMeasuredDimension(width, height);
    }

}
