package com.setDDG.homePage

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.setDDG.news.NewsFragment
import com.setDDG.util.StatusBarUtil
import com.rockex6.practiceappfoundation.R
import com.rockex6.practiceappfoundation.databinding.ActivityHomePageBinding
import com.setDDG.util.IntentUtil

class HomePageActivity : AppCompatActivity(), BottomBarOnClick {

    private lateinit var binding: ActivityHomePageBinding
    private var mMenu: Menu? = null
    private lateinit var vToolbar: Toolbar
    private lateinit var homePageViewModel: HomePageViewModel

    override fun onStart() {
        super.onStart()
        StatusBarUtil.setStatusBar(window, R.color.white)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        vToolbar = findViewById(R.id.vToolbarWithTitle)
        setSupportActionBar(vToolbar)
        initToolBar()
        homePageViewModel = ViewModelProvider(this).get(HomePageViewModel::class.java)
        observeViewModel()
        homePageViewModel.fetchBottomBar()
    }

    private fun observeViewModel() {
        homePageViewModel.bottomBarData.observe(this, Observer { bottomBarModels ->
            bottomBarModels?.let {
                binding.bottomBar.apply {
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
        setSupportActionBar(vToolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setHomeButtonEnabled(true)
            vToolbar.setNavigationIcon(R.drawable.ic_setting)
            supportActionBar!!.title = ""
            vToolbar.setNavigationOnClickListener {
                //設定頁
            }
        }
    }

    override fun onBottomBarClick(id: String) {
        when (id) {
            "news" -> changePage(NewsFragment())
            "project" -> changePage(NewsFragment())
            "live" -> changePage(NewsFragment())
            "video" -> changePage(NewsFragment())
            "picture" -> {
                changePage(NewsFragment())
                IntentUtil.startUrl(this,"https://stackoverflow.com/questions/17504169/how-to-get-installed-applications-in-android-and-no-system-apps")
            }
        }
    }

    private fun changePage(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.homePageContent.id, fragment)
        supportFragmentManager.executePendingTransactions()
        supportFragmentManager.beginTransaction()
            .replace(binding.homePageContent.id, fragment)
            .commitAllowingStateLoss()
    }
}