package com.setDDG.util

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import org.matomo.sdk.BuildConfig
import org.matomo.sdk.Matomo
import org.matomo.sdk.Tracker
import org.matomo.sdk.TrackerBuilder
import org.matomo.sdk.extra.TrackHelper

object MatomoTracker {
    private val TAG = "=${javaClass.simpleName}="
    private val isEnable = true

    //matomo
    private val SERVER_URL = "https://setana.setn.com/analytics/piwik.php"
    private val ID = 11
    private var MatomoTracker: Tracker? = null

    //參數
    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.VALUE_PARAMETER)
    annotation class SCREEN {
        companion object {
            const val WELCOME_PAGE = "歡迎頁"
            const val HOMEPAGE_CATEGORY = "首頁列表_%s"
            const val NEWS_CONTENT = "新聞內頁_%s_%s"
            const val LIVE = "直播列表_%s"
            const val LIVE_CONTENT = "直播內頁_%s"
            const val PROJECT_ALL = "專題列表_全部"
            const val PROJECT = "個別專題列表_%s"
            const val VIDEO_LIST = "影音列表"
            const val PICTURE_LIST = "圖籍列表"
            const val SEARCH = "搜尋頁"
            const val SETTING = "設定頁"
            const val VIDEO_PLAY_PAGE = "影音播放頁"
        }
    }

    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.VALUE_PARAMETER)
    annotation class Category {
        companion object {
            const val WELCOME_PAGE = "歡迎頁"
            const val HOMEPAGE_CATEGORY = "首頁列表_%s"
            const val HOMEPAGE_BLOCK = "首頁_%s"
            const val HOMEPAGE_CAROUSAL = "首頁_輪播"
            const val PUSH = "推播"
            const val LIVE_CONTENT = "直播內頁"
            const val PROJECT = "個別專題列表"
            const val VIDEO = "影音"
            const val PICTURE = "圖籍"
            const val BANNER = "特開Banner"
            const val LIVE_AREA = "LIVE_AREA"
            const val WATER_MARK = "浮水印"
            const val NEWS_CONTENT = "新聞內頁"
            const val SEARCH = "搜尋"
            const val SEARCH_RESULT = "搜尋結果頁"
            const val SETTING = "設定頁"
            const val FONT = "字體"
            const val VIDEO_PLAY_PAGE = "影音播放頁"
            const val FCM_TOKEN_APP = "FCMTokenApp"
        }
    }

    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.VALUE_PARAMETER)
    annotation class Action {
        companion object {
            const val CLICK = "點擊"
            const val SLIDE = "滑動"
            const val Platform = "android"
            const val SEARCH = "搜尋"
            const val PLAY_PAUSE = "play/pause"
        }
    }

    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.VALUE_PARAMETER)
    annotation class Label {
        companion object {
            const val FONT = "字體_%s"
        }
    }

    fun trackEvent(context: Context, category: String, action: String, label: String) {
        trackEvent(context, category, action, label, 0L, "")
    }

    fun trackEvent(
        context: Context,
        category: String,
        action: String,
        label: String,
        value: Long?,
        url: String) {

        if (isEnable) {
            Logger.d(TAG, "EVENT category : $category")
            Logger.d(TAG, "EVENT action : $action")
            if (label.isNotEmpty()) Logger.d(TAG, "EVENT label : $label")
            if (value != 0L) Logger.d(TAG, "EVENT value : $value")
            if (url.isNotEmpty()) Logger.d(TAG, "EVENT url : $url")
            Thread {
                trackFireBaseEvent(context, category, action, label, value)
                trackMatomoEvent(context, category, action, label, url)
            }.start()
        }
    }

    fun trackScreen(context: Context, @SCREEN title: String, url: String) {
        getMatomoTracker(context)?.let {
            Logger.d(TAG, "SCREEN title : $title")
            if (url.isNotEmpty()) Logger.d(TAG, "SCREEN url : $url")

            val trackHelper = TrackHelper.track()
//            TempData.mMemberProfile?.let {
//                trackHelper.visitVariables(2, "m_id", it.GetObject.UserID)
//            }
            trackHelper.visitVariables(3, "App Version", BuildConfig.VERSION_NAME)
                .screen(url)
                .title(title)
                .with(it)
        }
    }

    fun trackScreen(context: Context, @SCREEN title: String) {
        trackScreen(context, title, "")
    }

//    fun setUserId(context: Context) {
//        val mKeycloakToken: KeycloakTokenModel? by SharedPreferencesUtil(context,
//            SharedPreferencesUtil._SP_TOKEN, SharedPreferencesUtil.PREF_KeycloakToken,
//            KeycloakTokenModel(), KeycloakTokenModel::class.java)
//        mKeycloakToken?.let {
//            getMatomoTracker(context)?.let { tracker ->
//                try {
//                    val array = it.access_token.split(".")
//                    val data = String(Base64.decode(array[1], Base64.URL_SAFE))
//                    val subId: String = JSONObject(data)["sub"].toString()
//                    tracker.setUserId(subId)
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
//        }
//    }

    private fun trackFireBaseEvent(
        context: Context, category: String, action: String, label: String, value: Long?) {
        val param = Bundle()
        param.putString("action", action)
        if (label.isNotEmpty()) {
            param.putString("label", label)
        }
        if (value != null && value != 0L) {
            param.putLong("value", value)
        }
        FirebaseAnalytics.getInstance(context)
            .logEvent(category, param)
    }

    private fun trackMatomoEvent(
        context: Context, category: String, action: String, label: String, url: String) {
        getMatomoTracker(context)?.let {
            TrackHelper.track()
                .event(category, action)
                .name(label)
                .path(url)
                .with(it)
        }
    }

    private fun getMatomoTracker(context: Context): Tracker? {
        if (MatomoTracker == null) {
            MatomoTracker = TrackerBuilder.createDefault(SERVER_URL, ID)
                .build(Matomo.getInstance(context))
        }
        return MatomoTracker
    }
}