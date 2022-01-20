package com.setDDG.homePage

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.setDDG.baseViewPager.BaseViewPagerFragment
import com.setDDG.util.StatusBarUtil
import com.rockex6.practiceappfoundation.R
import com.setDDG.util.IntentUtil
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.toolbar_layout_with_title.*

class HomePageActivity : AppCompatActivity(), BottomBarOnClick {

    private var mMenu: Menu? = null
    private lateinit var homePageViewModel: HomePageViewModel

    override fun onStart() {
        super.onStart()
        StatusBarUtil.setStatusBar(window, R.color.white)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        setSupportActionBar(vToolbarWithTitle)
        initToolBar()
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
}