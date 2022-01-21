package com.setDDG.carouselPagerFunction;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

/**
 * 無限輪播
 */
public abstract class CircleAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener, TabLayout.OnTabSelectedListener {
    protected ViewPager mViewPager;
    protected TabLayout mTabLayout;
    protected int currentPosition = 0;

    public CircleAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public abstract Fragment getItem(int position);

    @Override
    public abstract int getCount();

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentPosition = position;
        try {
            mTabLayout.getTabAt(position - 1).select();
        } catch (NullPointerException e) {
//            e.printStackTrace();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // ViewPager.SCROLL_STATE_IDLE 如果在此狀態下移動，會出現跳動
        if (state != ViewPager.SCROLL_STATE_IDLE) return;

        // 當位置是0時，將viewpager設置為最後一張
        if (currentPosition == 0) {
            mViewPager.setCurrentItem(getCount() - 2, false);
        } else if (currentPosition == getCount() - 1) {
            // 當位置是最後一個時，將viewpager設置為第一張
            mViewPager.setCurrentItem(1, false);
        }
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition() + 1);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
