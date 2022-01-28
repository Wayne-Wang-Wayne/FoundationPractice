package com.setDDG.util

import android.content.Context
import android.net.Uri
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.text.util.Linkify
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.rockex6.practiceappfoundation.R
import java.util.*
import java.util.regex.Pattern

object UrlUtil {
    fun getPicUrl(path: String, imgID: Int): String {
        return if (!TextUtils.isEmpty(path)) {
            "$path$imgID-XXL.jpg"
        } else "$imgID-XXL.jpg"
    }


    fun getUrlId(url: String): String {
        val lowerUrl = url.toLowerCase(Locale.ROOT)
        val category = getUrlCategory(lowerUrl)
        val uri = Uri.parse(lowerUrl)
//            if (category?.contains(IntentUtil.KEY_PATH_NEWS_URL)!! || category?.contains(
//                    IntentUtil.KEY_PATH_PHOTO_URL)
//            ) {
        return getId(url)
//            }
//            return ""
    }

    private fun getId(url: String): String {
        val pattern = Pattern.compile("[0-9]+")
        val matcher = pattern.matcher(url)
        if (matcher.find()) {
            return matcher.group()
        }
        return ""
    }

    fun getUrlOpenMode(url: String): Int {
        val uri = Uri.parse(url.toLowerCase(Locale.ROOT))
        if (!TextUtils.isEmpty(
                uri.getQueryParameter(IntentUtil.KEY_OPEN_MODE.toLowerCase(Locale.ROOT)))
        ) {
            return uri.getQueryParameter(IntentUtil.KEY_OPEN_MODE.toLowerCase(Locale.ROOT))!!
                .toInt()
        }
        return 0
    }

    fun getUrlCategory(url: String): String? {
        return Uri.parse(url.toLowerCase(Locale.ROOT)).path
    }

    fun setHyperLint(textView: TextView, text: CharSequence) {
        val spannable = SpannableString(text)
        val urlPattern =
            "((http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?+-=\\\\.&]*|" + "(www\\.|WWW\\.)+[\\w\\d:#@%/;$()~_?+-=\\\\.&]*)"
        val p = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE)
        Linkify.addLinks(spannable, p, "http://")

        val urlPattern2 = "((https):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?+-=\\\\.&]*)"
        val p2 = Pattern.compile(urlPattern2, Pattern.CASE_INSENSITIVE)
        Linkify.addLinks(spannable, p2, "https://")

        val end = text.length
        val urlSpan = spannable.getSpans(0, end, URLSpan::class.java)
        if (urlSpan.isEmpty()) {
            return
        }
        val spannableStringBuilder = SpannableStringBuilder(text)
        Linkify.addLinks(spannableStringBuilder, Linkify.EMAIL_ADDRESSES)
        urlSpan.forEach {
            if (it.url.indexOf("http://") == 0 || it.url.indexOf("https://") == 0) {
                val customUrlSpan = CustomUrlSpan(textView.context, it.url)
                spannableStringBuilder.setSpan(customUrlSpan, spannable.getSpanStart(it),
                    spannable.getSpanEnd(it), Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            }
        }
        textView.text = spannableStringBuilder
        textView.movementMethod = LinkMovementMethod.getInstance()
    }

    class CustomUrlSpan(private val context: Context, private val url: String) : ClickableSpan() {

        override fun onClick(widget: View) {
            IntentUtil.startUrl(context, url)
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.color = ContextCompat.getColor(context, R.color.color6ea1ff)
        }
    }

    fun getYoutubeID(url: String): String? {
        val q = Pattern.compile(
            "(?:http|https|)(?:://|)(?:www.|)(?:youtu\\.be/|youtube\\.com(?:/embed/|/v/|/watch\\?v=|/ytscreeningroom\\?v=|/feeds/api/videos/|/user\\\\S*[^\\w\\-\\s]|\\S*[^\\w\\-\\s]))([\\w\\-_]{11})[a-z0-9;:@#?&%=+/_$.-]*")
        val matcher = q.matcher(url)
        return if (matcher.find()) {
            matcher.group(1)
        } else url
    }

    fun setProjectAssignPage(url: String): String {
        return "$url&${IntentUtil.KEY_OPEN_MODE}=2"
    }
}
