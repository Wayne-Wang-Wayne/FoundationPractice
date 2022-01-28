package com.foundationPractice.carouselPagerFunction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.foundationPractice.customView.CarouseViewPager;
import com.foundationPractice.util.Logger;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * 圖片輪播ADAPTER
 */
public class CarouselPagerAdapter extends CircleAdapter implements View.OnTouchListener {
    private static final String TAG = "CarouselPagerAdapter";
    private static final int Delay_Time = 3000;
    private ArrayList<PokemonPicModel> mFormatMarqueeModels;
    private boolean isPause = false;
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mViewPager.setCurrentItem(currentPosition + 1, true);
            mHandler.postDelayed(this, Delay_Time);
        }
    };

    public CarouselPagerAdapter(FragmentManager fm, ArrayList<PokemonPicModel> formatMarqueeModels, CarouseViewPager viewPager, TabLayout tabLayout) {
        super(fm);
        this.mFormatMarqueeModels = formatMarqueeModels;
        this.mViewPager = viewPager;
        this.mTabLayout = tabLayout;
        notifyDataSetChanged();
        mViewPager.setOnTouchListener(this);
        setMyScroller();
        start();
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return CarouselPagerItemFragment.Companion.newInstance(mFormatMarqueeModels, mFormatMarqueeModels.size() - 1);
        } else if (position > 0 && position <= mFormatMarqueeModels.size()) {
            return CarouselPagerItemFragment.Companion.newInstance(mFormatMarqueeModels, position - 1);
        } else {
            return CarouselPagerItemFragment.Companion.newInstance(mFormatMarqueeModels, 0);
        }
    }

    @Override
    public int getCount() {
        return mFormatMarqueeModels.size() + 2;
    }


    /**
     * 開始自動輪播
     */
    private void start() {
        Logger.d(TAG, "start");
        isPause = false;
        mHandler.postDelayed(mRunnable, Delay_Time);
    }

    /**
     * 停止自動輪播
     */
    public void stop() {
        Logger.d(TAG, "stop");
        isPause = true;
        mHandler.removeCallbacks(mRunnable);
    }


    /**
     * 移動輪播時，停止自動輪播
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //讓滑動時不受到parent view影響
        v.getParent().requestDisallowInterceptTouchEvent(true);
        if (event.getAction() == MotionEvent.ACTION_MOVE && !isPause) { //按下螢幕中間的文字
            Logger.d(TAG, String.valueOf(event.getAction()));
            stop();
        } else if (event.getAction() == MotionEvent.ACTION_UP && isPause) { //放開螢幕中間的文字
            start();
        }
        return false;
    }

    private void setMyScroller() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this, new MyScroller(mTabLayout.getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyScroller extends Scroller {
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