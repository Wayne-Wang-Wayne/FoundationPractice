package com.practiceappfoundation.util

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.rockex6.practiceappfoundation.R
import kotlin.math.roundToLong

fun AppCompatImageView.loadImage(activity: Activity, url: String) {
    Glide.with(activity)
        .load(url)
        .format(DecodeFormat.PREFER_RGB_565)
        .placeholder(R.mipmap.loading_photo)
        .into(this)
}

fun AppCompatImageView.loadImage(fragment: Fragment, url: String) {
    Glide.with(fragment)
        .load(url)
        .format(DecodeFormat.PREFER_RGB_565)
        .placeholder(R.mipmap.loading_photo)
        .into(this)
}

fun AppCompatImageView.loadImage(context: Context, url: String) {
    Glide.with(context)
        .load(url)
        .format(DecodeFormat.PREFER_RGB_565)
        .placeholder(R.mipmap.loading_photo)
        .into(this)
}

fun AppCompatImageView.loadImageForRoundedCorner(context: Context, url: String) {
    Glide.with(context)
        .load(url)
        .format(DecodeFormat.PREFER_RGB_565)
        .placeholder(R.mipmap.loading_photo)
        .transform(CenterCrop(),RoundedCorners(20))
        .into(this)
}

fun AppCompatImageView.loadMemberIcon(context: Context, url: String) {
    Glide.with(context)
        .load(url)
        .format(DecodeFormat.PREFER_RGB_565)
        .placeholder(R.mipmap.loading_photo)
//        .error(R.drawable.icon_default_member_icon)
        .into(this)
}

fun AppCompatImageView.loadImageNoHolder(context: Context, url: String) {
    Glide.with(context)
        .asBitmap()
        .load(url)
        .format(DecodeFormat.PREFER_RGB_565)
        .into(this)
}

fun AppCompatImageView.loadImageNoHolder(fragment: Fragment, url: String) {
    Glide.with(context)
        .asBitmap()
        .load(url)
        .format(DecodeFormat.PREFER_RGB_565)
        .into(this)
}


fun viewScaleHeight(activity: Activity): Int {
    //16:9的高
    val pictureWidth: Double = activity.resources.displayMetrics.widthPixels / 16.0
    return (pictureWidth.roundToLong() * 9).toInt()
}

fun viewScaleWidth(activity: Activity): Int {
    //16:9的寬
    return activity.resources.displayMetrics.widthPixels
}

fun viewScaleCustomerHeight(width: Int): Int {
    //客製化16:9高
    val pictureWidth: Double = width / 16.0
    return (pictureWidth.roundToLong() * 9).toInt()
}

fun screenViewDP(context: Context, dp: Int) : Int{
    return context.resources.getDimensionPixelSize(dp)
}

