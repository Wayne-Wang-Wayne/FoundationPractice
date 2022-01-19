package com.setDDG.news

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
import com.setDDG.testList.TestListFragment
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.retry_layout.view.*


open class NewsFragment : Fragment() {
    private lateinit var newsViewModel: NewsViewModel

    companion object {
        fun newInstance() = NewsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsViewModel = ViewModelProvider(this).get(NewsViewModel::class.java)
        newsViewModel.fetchNewsTabs()
        observeViewModel()
    }

    private fun observeViewModel() {
        newsViewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            isLoading?.let {
                vProgress.visibility = if (it) View.VISIBLE else View.GONE
            }
        })
        newsViewModel.newsTabs.observe(viewLifecycleOwner, Observer { newsTabs ->
            newsTabs.let {
                initViewPager(newsTabs)
                //設置tab
                TabLayoutMediator(vNewsTabLayout, vViewPager) { tab, position ->
                    tab.setCustomView(R.layout.item_tab)
                    tab.customView?.findViewById<TextView>(R.id.vTabTitle)?.text =
                        newsTabs[position].url
                    vViewPager.setCurrentItem(tab.position, true)
                }.attach()
            }
        })
        newsViewModel.errorMessage.observe(viewLifecycleOwner, Observer { message ->
            message?.let {
                retryLayout.vRefreshLayout.visibility = View.VISIBLE
                retryLayout.vErrorMessage.text = it
                retryLayout.vRefreshLayout.setOnClickListener {
                    retryLayout.vRefreshLayout.visibility = View.GONE
                    newsViewModel.fetchNewsTabs()
                }
            }

        })
    }

    //設置新聞列表fragment
    private fun initViewPager(newsTabs: List<NewsTabsModel>) {
        activity?.let {
            it.runOnUiThread {
                val newsListFragments = ArrayList<TestListFragment>()
                newsTabs.forEach { newsTab ->
                    val newsListFragment = TestListFragment.newInstance()
                    newsListFragments.add(newsListFragment)
                }
                vViewPager.apply {
                    adapter = PagerFragmentAdapter(this@NewsFragment, newsListFragments)
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