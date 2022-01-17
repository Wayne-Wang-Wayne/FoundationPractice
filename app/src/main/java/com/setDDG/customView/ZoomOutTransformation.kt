package com.setDDG.customView

import android.view.View
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs

class ZoomOutTransformation : ViewPager.PageTransformer {

    companion object {
        val MIN_SCALE = 0.95f
    }

    override fun transformPage(page: View, position: Float) {
        page.setLayerType(View.LAYER_TYPE_NONE, null)
        if (position <= 1) {
            page.scaleX = MIN_SCALE.coerceAtLeast(1 - abs(position))
            page.scaleY = MIN_SCALE.coerceAtLeast(1 - abs(position))
        }
    }
}