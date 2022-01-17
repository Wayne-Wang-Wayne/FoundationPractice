package com.practiceappfoundation.util

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.rockex6.practiceappfoundation.R

object StatusBarUtil {


    fun setStatusBar(window: Window, color: Int) {
        val activity = getActivity(window.context)
        activity?.let {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                window.statusBarColor = ContextCompat.getColor(it, color)
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val startColor = window.statusBarColor
                val endColor = ContextCompat.getColor(it, color)
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                ObjectAnimator.ofArgb(window, "statusBarColor", startColor, endColor)
                    .start()
            }
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            setStatusIcon(window, color)
        }
    }

    fun setStatusIcon(window: Window, color: Int) {
//        val activity = getActivity(window.context)
        if (color == R.color.black) {
            setStatusIconWhite(window)
        } else {
            setStatusIconBlack(window)
        }
    }

    private fun setStatusIconBlack(window: Window) {
        var flags = window.decorView.systemUiVisibility
        flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setStatusIconColor(window, flags)
    }

    private fun setStatusIconWhite(window: Window) {
        setStatusIconColor(window, 0)
    }

    private fun setStatusIconColor(window: Window, flags: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = flags
        }
    }


    private fun getActivity(context: Context?): Activity? {
        when (context) {
            null -> {
                return null
            }
            is ContextWrapper -> {
                return if (context is Activity) {
                    context
                } else {
                    getActivity(context.baseContext)
                }
            }
            else -> {
                getActivity((context as ContextWrapper).baseContext)
            }
        }
        return null
    }
}