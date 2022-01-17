package com.practiceappfoundation.customView

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.tabs.TabLayout
import com.practiceappfoundation.util.PhoneSizeUtil
import java.lang.reflect.Field

class CustomTabLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    TabLayout(context, attrs, defStyleAttr) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    init {
        initTabWidth(context)
    }

    private fun initTabWidth(context: Context) {
        val width = PhoneSizeUtil.getInstance(context)?.getPhoneWidth()
            ?.div(18)
        val field: Field
        try {
            field = TabLayout::class.java.getDeclaredField("mScrollableTabMinWidth")
            field.isAccessible = true
            field.set(this, width)
        } catch (e: Exception) {

        }
    }
}