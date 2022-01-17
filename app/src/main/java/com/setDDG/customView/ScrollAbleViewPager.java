package com.setDDG.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

public class ScrollAbleViewPager extends CustomViewPager {
    private static final String TAG = "CustomViewPager";
    private boolean scrollable = true;

    public ScrollAbleViewPager(@NonNull Context context) {
        this(context, null);
    }

    public ScrollAbleViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setMyScroller();
    }

    public Boolean getScrollable() {
        return scrollable;
    }

    public void setScrollable(boolean enable) {
        scrollable = enable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        try {
            if (scrollable) {
                return super.onInterceptTouchEvent(event);
            }
        } catch (IllegalArgumentException ex) {
//            DebugLog.e(TAG, ex);
        }
        return false;
    }

    private void setMyScroller() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this, new MyScroller(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        try {
//            super.onTouchEvent(event);
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

    public static class MyScroller extends Scroller {
        private MyScroller(Context context) {
            super(context, new DecelerateInterpolator());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
//            super.startScroll(startX, startY, dx, dy, 1000 /*1 secs*/);
            super.startScroll(startX, startY, dx, dy, 600);
        }
    }
}
