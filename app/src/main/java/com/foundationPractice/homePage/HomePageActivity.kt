package com.foundationPractice.homePage

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.foundationPractice.baseViewPager.BaseViewPagerFragment
import com.foundationPractice.util.StatusBarUtil
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.rockex6.practiceappfoundation.BuildConfig
import com.rockex6.practiceappfoundation.R
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.toolbar_layout_with_title.*
import timber.log.Timber


class HomePageActivity : AppCompatActivity(), BottomBarOnClick {

    private var mMenu: Menu? = null
    private lateinit var homePageViewModel: HomePageViewModel

    companion object {

    }
    override fun onStart() {
        super.onStart()
        StatusBarUtil.setStatusBar(window, R.color.white)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        init()
        homePageViewModel = ViewModelProvider(this).get(HomePageViewModel::class.java)
        observeViewModel()
        homePageViewModel.fetchBottomBar()

        //不需要，是為了不讓bottomBar起作用才加的
        changePage(BaseViewPagerFragment())


    }

    private fun observeViewModel() {
        homePageViewModel.bottomBarData.observe(this, Observer { bottomBarModels ->
            bottomBarModels?.let {
                bottom_bar.apply {
                    val gridLayoutManager = GridLayoutManager(context, 5)
                    val bottomBarAdapter = BottomBarAdapter(context, it, this@HomePageActivity)
                    layoutManager = gridLayoutManager
                    adapter = bottomBarAdapter
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        mMenu = menu
        menuInflater.inflate(R.menu.menu_home_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun initToolBar() {
        setSupportActionBar(vToolbarWithTitle)
        if (supportActionBar != null) {
            supportActionBar!!.setHomeButtonEnabled(true)
            vToolbarWithTitle.setNavigationIcon(R.drawable.ic_setting)
            supportActionBar!!.title = ""
            vToolbarWithTitle.setNavigationOnClickListener {
                //設定頁
            }
        }
    }

    override fun onBottomBarClick(id: String) {
        when (id) {
//            "news" -> changePage(BaseViewPagerFragment())
//            "project" -> changePage(BaseViewPagerFragment())
//            "live" -> changePage(BaseViewPagerFragment())
//            "video" -> changePage(BaseViewPagerFragment())
//            "picture" -> {
//                changePage(BaseViewPagerFragment())
//              }
        }
    }

    private fun changePage(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(home_page_content.id, fragment)
        supportFragmentManager.executePendingTransactions()
        supportFragmentManager.beginTransaction()
            .replace(home_page_content.id, fragment)
            .commitAllowingStateLoss()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun init() {
        FirebaseApp.initializeApp(this)
        FirebaseCrashlytics.getInstance()
            .setCrashlyticsCollectionEnabled(true)
        initTimber()
        setSupportActionBar(vToolbarWithTitle)
        initToolBar()

    }
}