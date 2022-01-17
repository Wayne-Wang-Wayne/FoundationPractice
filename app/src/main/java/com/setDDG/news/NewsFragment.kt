package com.setDDG.news

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.setDDG.testList.TestListFragment
import com.rockex6.practiceappfoundation.R
import com.rockex6.practiceappfoundation.databinding.FragmentNewsBinding

open class NewsFragment : Fragment() {
    private lateinit var newsViewModel: NewsViewModel
    //bindingView
    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = NewsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //bindingView
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsViewModel = ViewModelProvider(this).get(NewsViewModel::class.java)
        newsViewModel.fetchNewsTabs()
        observeViewModel()
    }

    private fun observeViewModel(){
        newsViewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            isLoading?.let {
                binding.vProgress.visibility = if (it) View.VISIBLE else View.GONE
            }
        })
        newsViewModel.newsTabs.observe(viewLifecycleOwner, Observer { newsTabs->
            newsTabs.let {
                initViewPager(newsTabs)
                //設置tab
                TabLayoutMediator(binding.vNewsTabLayout, binding.vViewPager) { tab, position ->
                    tab.setCustomView(R.layout.item_tab)
                    tab.customView?.findViewById<TextView>(R.id.vTabTitle)?.text =
                        newsTabs[position].url
                    binding.vViewPager.setCurrentItem(tab.position, true)
                }.attach()
            }
        })
        newsViewModel.errorMessage.observe(viewLifecycleOwner, Observer {  message ->
            message?.let {
                binding.retryLayout.vRefreshLayout.visibility = View.VISIBLE
                binding.retryLayout.vErrorMessage.text = it
                binding.retryLayout.vRefreshLayout.setOnClickListener {
                    binding.retryLayout.vRefreshLayout.visibility = View.GONE
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
                binding.vViewPager.apply {
                    adapter = PagerFragmentAdapter(this@NewsFragment, newsListFragments)
                    offscreenPageLimit = 1
                }
                binding.vViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
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
        for (i in 0 until binding.vNewsTabLayout.tabCount) {
            val tab = binding.vNewsTabLayout.getTabAt(i)
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