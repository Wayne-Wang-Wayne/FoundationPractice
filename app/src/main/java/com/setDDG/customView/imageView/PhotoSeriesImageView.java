package com.setDDG.customView.imageView;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;

public class PhotoSeriesImageView extends NewsItemImageView {
    private Matrix mMatrix;
    private boolean mHasFrame;


    public PhotoSeriesImageView(Context context) {
        this(context, null);
    }

    public PhotoSeriesImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressWarnings("UnusedDeclaration")
    public PhotoSeriesImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mHasFrame = false;
        mMatrix = new Matrix();
    }


    @Override
    protected boolean setFrame(int l, int t, int r, int b) {
        boolean changed = super.setFrame(l, t, r, b);
        if (changed) {
            mHasFrame = true;
            setupScaleMatrix(r - l, b - t);
        }
        return changed;
    }


    private void setupScaleMatrix(int width, int height) {
        if (!mHasFrame) {
            return;
        }
        final Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        Matrix matrix = mMatrix;
        final int intrinsicWidth = drawable.getIntrinsicWidth();
        final int intrinsicHeight = drawable.getIntrinsicHeight();

        float factorWidth = width / (float) intrinsicWidth;
        float factorHeight = height / (float) intrinsicHeight;
        float factor = Math.max(factorHeight, factorWidth);

        // there magic happen and can be adjusted to current
        // needs
        matrix.setTranslate(-intrinsicWidth / 2.0f, 0);
        matrix.postScale(factor, factor, 0, 0);
        matrix.postTranslate(width / 2.0f, 0);
        setImageMatrix(matrix);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        setupScaleMatrix(getWidth(), getHeight());
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        setupScaleMatrix(getWidth(), getHeight());
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        setupScaleMatrix(getWidth(), getHeight());
    }
}
