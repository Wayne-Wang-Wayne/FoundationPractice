package com.setDDG.util

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsIntent
import androidx.multidex.BuildConfig
import com.rockex6.practiceappfoundation.R
import com.webview.WebViewActivity
import java.util.*

object IntentUtil {
    private val TAG = javaClass.simpleName
    private var lastClickTime: Long = 0
    const val KEY_OPEN_MODE = "OpenMode"
    const val KEY_PATH_NEWS_URL = "news"
    const val KEY_PATH_PROJECT_URL = "project"
    const val KEY_PATH_PHOTO_URL = "photo"
    const val KEY_PATH_LIVE = "live"
    fun intentToAnyPage(context: Context, cls: Class<*>) {
        intentToAnyPage(context, null, cls)
    }

    fun intentToPageResult(activity: Activity, result: Int, cls: Class<*>) {
        run {
            if (!isFastDoubleClick()) {
                val intent = Intent()
                intent.setClass(activity, cls)
                activity.startActivityForResult(intent, result)
            }
        }
    }

    fun intentToAnyPage(context: Context, bundle: Bundle?, cls: Class<*>, uri: Uri?) {
        run {
            if (!isFastDoubleClick()) {
                val intent = Intent()
                if (bundle != null) {
                    Logger.d(TAG, "bundle : $bundle")
                    intent.putExtras(bundle)
                }
                if (uri != null) {
                    intent.data = uri
                }
                intent.setClass(context, cls)
                context.startActivity(intent)
            }
        }
    }

    fun intentToAnyPage(context: Context, bundle: Bundle?, cls: Class<*>) {
        intentToAnyPage(context, bundle, cls, null)
    }

    fun intentToVideoPlayerPageActivity(activity: Activity, bundle: Bundle?, cls: Class<*>) {
        run {
            if (!isFastDoubleClick()) {
                val intent = Intent()
                if (bundle != null) {
                    Logger.d(TAG, "bundle : $bundle")
                    intent.putExtras(bundle)
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.setClass(activity, cls)
                activity.startActivity(intent)
                activity.overridePendingTransition(0, 0)
                activity.finish()
            }
        }
    }

    fun intentToWebActivity(activity: Activity, bundle: Bundle?, cls: Class<*>) {
        val intent = Intent()
        if (bundle != null) {
            Logger.d(TAG, "bundle : $bundle")
            intent.putExtras(bundle)
        }
        intent.setClass(activity, cls)
        activity.startActivity(intent)
        activity.overridePendingTransition(0, 0)
    }

//    fun pushIntent(context: Context, intent: Intent) {
//        val bundle = intent.extras!!
//        val openMode = bundle.getString(FirebaseMessage.KEY_OPEN_TYPE, "")
//        if (openMode == FirebaseMessage.BROWSER) {
//            intentToChrome(context, intent.data.toString())
//            return
//        }
//        startUrl(context, intent.data.toString())
//    }

    fun startUrl(context: Context, url: String) {
        startUrl(context, url, null)
    }

    fun startUrl(context: Context, url: String, newsIDs: ArrayList<Int>?) {
        Logger.d(TAG, "URL : $url")
        if (url.isNotEmpty()) {
            val category = UrlUtil.getUrlCategory(url)
                ?.toLowerCase(Locale.ROOT)
            Logger.d(TAG, "category : $category")
            val openMode = UrlUtil.getUrlOpenMode(url)
            Logger.d(TAG, "openMode : $openMode")
            val id = UrlUtil.getUrlId(url)
                .toLowerCase(Locale.ROOT)
            Logger.d(TAG, "id : $id")
            //瀏覽器
            val bundle = Bundle()
            if (openMode != OpenModeEnum.OPEN_IN_APP) {
                if (openMode == OpenModeEnum.OPEN_BROWSER) {
                    intentToChrome(context, "", url)
                } else {
                    bundle.putString(WebViewActivity.WEB_URL, url)
                    bundle.putString(WebViewActivity.WEB_TYPE, WebViewActivity.WEB_VIEW)
                    intentToAnyPage(context, bundle, WebViewActivity::class.java)
                }
                return
            }

            if (id.isNotEmpty()) {
                when {
//                    category?.contains(KEY_PATH_PROJECT_URL)!! -> {
//                        bundle.putInt(ProjectNewsListActivity.KEY_PROJECT_ID, id.toInt())
//                        intentToAnyPage(context, bundle, ProjectNewsListActivity::class.java)
//                    }
//
//                    category.contains(KEY_PATH_NEWS_URL) -> {
//                        if (newsIDs != null && newsIDs.size != 0) {
//                            bundle.putIntegerArrayList(NewsContentActivity.KEY_NEWS_ID_ARRAY,
//                                newsIDs)
//                        }
//                        bundle.putInt(NewsContentActivity.KEY_NEWS_ID, id.toInt())
//                        intentToAnyPage(context, bundle, NewsContentActivity::class.java)
//                    }
//
//                    category.contains(KEY_PATH_PHOTO_URL) -> {
//                        bundle.putInt(PictureContentActivity.KEY_PHOTO_ID, id.toInt())
//                        intentToAnyPage(context, bundle, PictureContentActivity::class.java)
//                    }
//
//                    category.contains(KEY_PATH_LIVE) -> {
//                        bundle.putInt(LiveContentActivity.KEY_SLVID, id.toInt())
//                        intentToAnyPage(context, bundle, LiveContentActivity::class.java)
//                    }

                    else -> {
                        intentToChrome(context, "", url)
                    }
                }
            } else {
                bundle.putString(WebViewActivity.WEB_URL, url)
                bundle.putString(WebViewActivity.WEB_TYPE,
                    if (openMode == OpenModeEnum.OPEN_WEBVIEW_IN_APP) WebViewActivity.WEB_VIEW else WebViewActivity.WEB_CHROME)
                intentToAnyPage(context, bundle, WebViewActivity::class.java)
            }
        }
    }

    fun share(context: Context, url: String) {
        var intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, url)
        intent = Intent.createChooser(intent, "分享連結")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

//    fun settingShare(context: Context, url: String) {
//        val title: String = context.resources.getString(R.string.app_name)
//        val text: String =
//            String.format(context.resources.getString(R.string.setting_share_link), title, url)
//        var intent = Intent()
//        intent.setAction(Intent.ACTION_SEND)
//            .setType("text/plain")
//            .putExtra(Intent.EXTRA_TEXT, text).flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        intent = Intent.createChooser(intent, text)
//        context.startActivity(intent)
//    }

    fun toGoogleUpdate(context: Context) {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(
            "http://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)))
    }

    fun toGooglePlay(context: Context, url: String) {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    fun toSocialSoftware(context: Context, type: String) {
        if (!isFastDoubleClick()) {
            var url = ""
            when (type) {
                "fb" -> {
                    try {
                        url = "fb://page/1490621264490659"
                        context.packageManager.getPackageInfo("com.facebook.katana", 0);
                    } catch (e: Exception) {
//                        url = FIND_US_FB_URL
                    }

                }
                "youtube" -> {
//                    url = FIND_US_YOUTUBE_URL
                }
                "line" -> {
//                    url = FIND_US_LINE_URL
                }
                else -> {
                }
            }
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            } catch (e: Exception) {
//                url = FIND_US_FB_URL
                intentToChrome(context, "", url)
            }
        }
    }

//    fun logout(context: Context, keycloakViewModel: KeycloakViewModel) {
//        val mKeycloakToken: KeycloakTokenModel? by SharedPreferencesUtil(context,
//            SharedPreferencesUtil._SP_TOKEN, SharedPreferencesUtil.PREF_KeycloakToken,
//            KeycloakTokenModel(), KeycloakTokenModel::class.java)
//        var mSeteToken: LoginResultModel? by SharedPreferencesUtil(context,
//            SharedPreferencesUtil._SP_TOKEN, SharedPreferencesUtil.PREF_SeteToken,
//            LoginResultModel(), LoginResultModel::class.java)
//        mKeycloakToken?.let { keycloakViewModel.logout(it) }
//
//        mSeteToken = null
//        TempData.mMemberProfile = null
//        intentToAnyPage(context, SettingActivity::class.java)
//    }

    private fun intentToChrome(context: Context, url: String?) {
        intentToChrome(context, context.getString(R.string.app_name), url)
    }

    private fun intentToChrome(context: Context, webTitle: String?, url: String?) {
        val bundle = Bundle()
        val packageName = "com.android.chrome"
        if (isPackageExisted(context, packageName)) {
            val customTabsIntent: CustomTabsIntent
            val builder = CustomTabsIntent.Builder()
            builder.enableUrlBarHiding()
            builder.setShowTitle(true)
            builder.addDefaultShareMenuItem()
            builder.setToolbarColor(context.resources.getColor(R.color.colorf6f6f6))
            customTabsIntent = builder.build()
            customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            customTabsIntent.intent.setPackage(packageName)
            try {
                if (!isFastDoubleClick()) {
                    customTabsIntent.launchUrl(context, Uri.parse(url))
                }
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
                lastClickTime = System.currentTimeMillis() - 401
                bundle.putString(WebViewActivity.WEB_URL, url)
                bundle.putString(WebViewActivity.WEB_TYPE, WebViewActivity.WEB_VIEW)
                intentToAnyPage(context, bundle, WebViewActivity::class.java)
            }
        } else {
            lastClickTime = System.currentTimeMillis() - 401
            bundle.putString(WebViewActivity.WEB_URL, url)
            bundle.putString(WebViewActivity.WEB_TYPE, WebViewActivity.WEB_VIEW)
            intentToAnyPage(context, bundle, WebViewActivity::class.java)
        }
    }

    private fun isPackageExisted(context: Context, targetPackage: String): Boolean {
        val packages: List<ApplicationInfo>
        val pm: PackageManager = context.packageManager
        packages = pm.getInstalledApplications(0)
        for (packageInfo in packages) {
            if (packageInfo.packageName == targetPackage) return true
        }
        return false
    }

    /**
     * 防止連點 開啟多個activity
     */
    private fun isFastDoubleClick(): Boolean {
        val time = System.currentTimeMillis()
        if (time - lastClickTime < 400) {
            Logger.i(TAG, "Intent < 200ms")
            return true
        }
        lastClickTime = time
        return false
    }
}
