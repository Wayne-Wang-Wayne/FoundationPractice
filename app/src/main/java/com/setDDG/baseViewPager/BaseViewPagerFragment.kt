package com.setDDG.baseViewPager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.rockex6.practiceappfoundation.R
import com.setDDG.carouselPagerFunction.CarouselPagerFunctionFragment
import com.setDDG.glideFunction.GlideFunctionFragment
import com.setDDG.weatherViewFunction.WeatherViewFunctionFragment
import com.setDDG.webViewFunction.WebViewFunctionFragment
import com.setDDG.yTPlayerFunction.YTPlayerFunctionFragment
import kotlinx.android.synthetic.main.fragment_base_view_pager.*
import kotlinx.android.synthetic.main.retry_layout.view.*


open class BaseViewPagerFragment : Fragment() {
    private lateinit var baseViewPagerViewModel: BaseViewPagerViewModel

    companion object {
        fun newInstance() = BaseViewPagerFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_base_view_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        baseViewPagerViewModel = ViewModelProvider(this).get(BaseViewPagerViewModel::class.java)
        baseViewPagerViewModel.fetchNewsTabs()
        observeViewModel()
    }

    private fun observeViewModel() {
        baseViewPagerViewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            isLoading?.let {
                vProgress.visibility = if (it) View.VISIBLE else View.GONE
            }
        })
        baseViewPagerViewModel.newsTabs.observe(viewLifecycleOwner, Observer { newsTabs ->
            newsTabs.let {
                initViewPager(newsTabs)
                //設置tab
                TabLayoutMediator(vNewsTabLayout, vViewPager) { tab, position ->
                    tab.setCustomView(R.layout.item_tab)
                    tab.customView?.findViewById<TextView>(R.id.vTabTitle)?.text =
                        newsTabs[position].title
                    vViewPager.setCurrentItem(tab.position, true)
                }.attach()
            }
        })
        baseViewPagerViewModel.errorMessage.observe(viewLifecycleOwner, Observer { message ->
            message?.let {
                retryLayout.vRefreshLayout.visibility = View.VISIBLE
                retryLayout.vErrorMessage.text = it
                retryLayout.vRefreshLayout.setOnClickListener {
                    retryLayout.vRefreshLayout.visibility = View.GONE
                    baseViewPagerViewModel.fetchNewsTabs()
                }
            }

        })
    }

    //設置新聞列表fragment
    private fun initViewPager(newsTabs: List<NewsTabsModel>) {
        activity?.let {
            it.runOnUiThread {
                val newsListFragments = ArrayList<Fragment>()
                newsTabs.forEach { newsTab ->
                    when (newsTab.id) {
                        "glide_picture" -> newsListFragments.add(
                            GlideFunctionFragment.newInstance())
                        "open_web_view" -> newsListFragments.add(
                            WebViewFunctionFragment.newInstance())
                        "weather_view" -> newsListFragments.add(
                            WeatherViewFunctionFragment.newInstance())
                        "youtube_player_view" -> {
                            newsListFragments.add(YTPlayerFunctionFragment.newInstance(object :StopMainViewPagerScroll{
                                override fun stopScroll(flag: Boolean) {
                                    vViewPager.isUserInputEnabled = !flag
                                }
                            }))
                        }
                        "carousel_pager" -> newsListFragments.add(
                            CarouselPagerFunctionFragment.newInstance())
                    }

                }
                vViewPager.apply {
                    adapter = PagerFragmentAdapter(this@BaseViewPagerFragment, newsListFragments)
                    offscreenPageLimit = 1
                }
                vViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        setTab(position)
                    }
                })
            }
        }
    }

    //設置選中tab樣式
    private fun setTab(position: Int) {
        for (i in 0 until vNewsTabLayout.tabCount) {
            val tab = vNewsTabLayout.getTabAt(i)
            tab?.let {
                it.customView?.let { view ->
                    val textView = view.findViewById<TextView>(R.id.vTabTitle)
                    context?.let { context ->
                        if (i != position) {
                            textView.setTextColor(
                                ContextCompat.getColor(context, R.color.tab_unselect_text))
                            textView.background = null
                        } else {
                            textView.setTextColor(
                                ContextCompat.getColor(context, R.color.tab_select_text))
                            textView.background =
                                ContextCompat.getDrawable(context, R.drawable.bg_tab_select)
                        }
                    }
                }
            }
        }
    }

}

interface StopMainViewPagerScroll {
    fun stopScroll(flag: Boolean)
}