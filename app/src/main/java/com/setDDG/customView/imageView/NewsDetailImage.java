package com.setDDG.customView.imageView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * 新聞內頁使用的imageView
 */
public class NewsDetailImage extends NewsFullImageView {
    Path clipPath = new Path();
    RectF rect;
    private static final float radius = 18.0f;

    public NewsDetailImage(Context context) {
        super(context);
    }

    public NewsDetailImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewsDetailImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        rect = new RectF(0, 0, getWidth(), getHeight());
        clipPath.addRoundRect(rect, radius, radius, Path.Direction.CW);
        canvas.clipPath(clipPath);
        super.onDraw(canvas);
    }
}
