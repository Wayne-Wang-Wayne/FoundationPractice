package com.practiceappfoundation.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.practiceappfoundation.util.PhoneSizeUtil;

public class CarouseViewPager extends ScrollAbleViewPager {
    public CarouseViewPager(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CarouseViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        PhoneSizeUtil phoneSizeUtil = PhoneSizeUtil.Companion.getInstance(context);
        if (phoneSizeUtil != null) {
            int margin = phoneSizeUtil.getPhoneWidth() / 108;
            setPageMargin(-margin);
            setOffscreenPageLimit(2);
        }
    }

    //讓viewpager 自適應內容高度
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec,
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height) //採用最大的view的高度。
                height = h;
        }

        heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height,
                View.MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
