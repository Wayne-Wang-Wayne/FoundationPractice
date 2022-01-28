package com.foundationPractice.customView

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.rockex6.practiceappfoundation.R
import com.foundationPractice.util.IntentUtil
import com.foundationPractice.util.PhoneSizeUtil

class CustomToolbar : Toolbar {

    private var shareUrl = ""
     var toolbarTitle: TextView? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs,
        defStyleAttr)

    init {
        navigationIcon = ContextCompat.getDrawable(context, R.drawable.ic_back)
        setNavigationOnClickListener {
            getActivity(context)?.onBackPressed()
        }
        setBackgroundColor(ContextCompat.getColor(context, R.color.black))
        inflateMenu(R.menu.menu_content_toolbar)
        setOnMenuItemClickListener {
            when (it.itemId) {
//                R.id.font_select -> {
//                    IntentUtil.intentToAnyPage(context, FontSizeSettingActivity::class.java)
//                    true
//                }
                R.id.share -> {
                    if (shareUrl.isNotEmpty()) IntentUtil.share(context, shareUrl)
                    true
                }
                else -> false
            }
        }
        toolbarTitle = TextView(context)
        toolbarTitle?.textSize =
            context.resources.getDimensionPixelSize(R.dimen._28dp) / PhoneSizeUtil.getInstance(
                context)!!.displayMetrics.density
        toolbarTitle?.setTextColor(ContextCompat.getColor(context, R.color.white))
        toolbarTitle?.typeface = Typeface.DEFAULT_BOLD
        toolbarTitle?.gravity = Gravity.CENTER
        toolbarTitle?.maxLines = 1
        toolbarTitle?.ellipsize = TextUtils.TruncateAt.END
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.CENTER
        params.marginEnd =
            (context.resources.getDimensionPixelSize(R.dimen._150dp) / PhoneSizeUtil.getInstance(
                context)!!.displayMetrics.density).toInt()
        toolbarTitle?.layoutParams = params
        toolbarTitle?.isSingleLine = true

        addView(toolbarTitle)
    }

    fun setShareUrl(url: String) {
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.CENTER
        toolbarTitle?.layoutParams = params
        shareUrl = url
    }

    fun setTitle(title: String) {
        toolbarTitle?.text = title
    }

    fun setTitleColor(color: Int) {
        toolbarTitle?.setTextColor(ContextCompat.getColor(context, color))
    }

    fun setIcon(drawable: Int) {
        navigationIcon = ContextCompat.getDrawable(context, drawable)
    }

    fun setToolbarColor(color: Int) {
        setBackgroundColor(ContextCompat.getColor(context, color))
        navigationIcon = ContextCompat.getDrawable(context, R.drawable.ic_back)
    }

    private fun getActivity(context: Context?): Activity? {
        if (context == null) {
            return null
        } else if (context is ContextWrapper) {
            return if (context is Activity) {
                context
            } else {
                getActivity(context.baseContext)
            }
        }
        return null
    }
}
