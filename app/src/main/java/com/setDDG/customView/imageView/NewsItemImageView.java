package com.setDDG.customView.imageView;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.rockex6.practiceappfoundation.R;


/**
 * 四角都圓角的imageView
 */
public class NewsItemImageView extends ProportionImageView {
    Path clipPath = new Path();
    RectF rect;

    public NewsItemImageView(Context context) {
        this(context, null);
    }

    public NewsItemImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewsItemImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        rect = new RectF(0, 0, getWidth(), getHeight());
        float radius = getResources().getDimensionPixelSize(R.dimen._10dp);
        clipPath.addRoundRect(rect, radius, radius, Path.Direction.CW);
        canvas.clipPath(clipPath);
        super.onDraw(canvas);
    }
}

