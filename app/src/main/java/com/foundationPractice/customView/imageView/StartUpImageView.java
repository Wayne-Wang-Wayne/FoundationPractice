package com.foundationPractice.customView.imageView;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class StartUpImageView extends AppCompatImageView {
    public StartUpImageView(Context context) {
        super(context);
    }

    public StartUpImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StartUpImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        try {
            super.onDraw(canvas);
        } catch (Exception e) {
            System.out
                    .println("NewsFullImageView -> onDraw() Canvas: trying to use a recycled bitmap");
        }
    }
}
