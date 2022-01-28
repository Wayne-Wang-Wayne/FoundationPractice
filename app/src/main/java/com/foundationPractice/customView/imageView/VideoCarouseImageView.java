package com.setDDG.customView.imageView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.rockex6.practiceappfoundation.R;


/**
 *下面兩個角不需圓角的ImageView
 */
public class VideoCarouseImageView extends ProportionImageView {
    Path clipPath = new Path();
    RectF rect;
    final float[] radii = new float[8];

    public VideoCarouseImageView(Context context) {
        super(context);
    }

    public VideoCarouseImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoCarouseImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float radius = getResources().getDimensionPixelSize(R.dimen._10dp);
        rect = new RectF(0, 0, getWidth(), getHeight());
        radii[0] = radius;
        radii[1] = radius;
        radii[2] = radius;
        radii[3] = radius;
        clipPath.addRoundRect(rect, radii, Path.Direction.CW);
        canvas.clipPath(clipPath);
        super.onDraw(canvas);
    }
}
