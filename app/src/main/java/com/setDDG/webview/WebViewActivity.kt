package com.setDDG.webview

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebSettingsCompat.DARK_STRATEGY_PREFER_WEB_THEME_OVER_USER_AGENT_DARKENING
import androidx.webkit.WebViewFeature
import com.rockex6.practiceappfoundation.R
import com.rockex6.practiceappfoundation.databinding.ActivityWebViewBinding
import com.setDDG.webview.viewmodel.WebViewModel
import com.setDDG.util.StatusBarUtil



class WebViewActivity : AppCompatActivity() {
    private val TAG: String = javaClass.simpleName
    lateinit var webViewModel: WebViewModel
    private var chromePackage: String = "com.android.chrome"
    private lateinit var binding: ActivityWebViewBinding

    companion object {
        const val WEB_URL = "web_url"
        const val WEB_TYPE = "web_type"
        const val WEB_CHROME = "web_chrome"
        const val WEB_VIEW = "web_view"
        const val WEB_VIEW_TITLE = "web_view"
    }

    override fun onStart() {
        super.onStart()
        StatusBarUtil.setStatusBar(window, R.color.black)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        webViewModel = ViewModelProvider(this).get(WebViewModel::class.java)
        intent.extras?.let { webViewModel.setWebViewActivity(it) }
        observeViewModel()
    }

    private fun observeViewModel() {
        webViewModel.data.observe(this, Observer { data ->
            data?.let {
                val url = it.getString(WEB_URL, "")
                val title = it.getString(WEB_VIEW_TITLE, "")

                when (it.getString(WEB_TYPE)) {
                    WEB_CHROME -> {
                        setCustomTabsIntent(url)
                    }
                    WEB_VIEW -> {
                        setWebView(url, title)
                    }
                }
            }
        })
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun setWebView(url: String, title: String) {

        binding.vWebViewToolbar.setTitle(title)
        binding.vWebViewToolbar.setTitleColor(R.color.white)
        val webSettings = binding.vWebView.settings
        webSettings.setSupportZoom(true)
        webSettings.builtInZoomControls = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            webSettings.safeBrowsingEnabled = true
        }
        webSettings.javaScriptEnabled = true
        binding.vWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                binding.vWebViewToolbar.setTitle(view.title)
            }
        }
        binding.vWebView.visibility = View.VISIBLE
        binding.vWebView.loadUrl(url)
    }

    fun setCustomTabsIntent(url: String) {
        val mCustomTabsIntent: CustomTabsIntent
        val builder = CustomTabsIntent.Builder()
        builder.enableUrlBarHiding()
        builder.setShowTitle(true)
        builder.addDefaultShareMenuItem()
//        builder.setToolbarColor(resources.getColor(R.color.))
        mCustomTabsIntent = builder.build()
        mCustomTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        mCustomTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        mCustomTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        mCustomTabsIntent.intent.setPackage(chromePackage)
        try{
            mCustomTabsIntent.launchUrl(this, Uri.parse(url))
        }catch (e:Exception){
            mCustomTabsIntent.launchUrl(this, Uri.parse("https://www.google.com.tw/"))
        }
        finish()
    }

    //WebView跟隨app系統暗黑系統
    fun setDarkWebMode(webSettings: WebSettings) {
        val nightModeFlag = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (nightModeFlag == Configuration.UI_MODE_NIGHT_YES) {
            if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                WebSettingsCompat.setForceDark(webSettings, WebSettingsCompat.FORCE_DARK_ON)
            }
            if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK_STRATEGY)) {
                WebSettingsCompat.setForceDarkStrategy(webSettings,
                    DARK_STRATEGY_PREFER_WEB_THEME_OVER_USER_AGENT_DARKENING)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.vWebView?.let {
            binding.vWebView.onPause()
            binding.vWebView.destroy()
            binding.vWebView.clearHistory()
        }
    }
}
