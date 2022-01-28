package com.foundationPractice.carouselPagerFunction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.rockex6.practiceappfoundation.R
import com.foundationPractice.customView.ZoomOutTransformation
import kotlinx.android.synthetic.main.fragment_carousel_pager_function.*
import java.util.*


class CarouselPagerFunctionFragment : Fragment() {

    private lateinit var mCarouselPagerAdapter: CarouselPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_carousel_pager_function, container, false)
    }

    companion object {
        fun newInstance() = CarouselPagerFunctionFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val formatMarqueeModels: ArrayList<PokemonPicModel> = ArrayList<PokemonPicModel>()
        for (i in 1..9) {
            val pokemonPicModel = PokemonPicModel()
            pokemonPicModel.picUrl =
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$i.png"
            formatMarqueeModels.add(pokemonPicModel)
        }
        if (isAdded) {
            setPicMarquee(formatMarqueeModels, formatMarqueeModels.size)
        }
    }
    //view pager 可以參考這篇：https://stackoverflow.com/questions/28783690/android-viewpager-center-item-on-screen/28811330
    private fun setPicMarquee(formatMarqueeModels: ArrayList<PokemonPicModel>, tabSize: Int) {
        mCarouselPagerAdapter =
            CarouselPagerAdapter(this.childFragmentManager, formatMarqueeModels, viewPager,
                bottom_tabLayout)
        viewPager.apply {
            adapter = mCarouselPagerAdapter
            addOnPageChangeListener(mCarouselPagerAdapter)
            currentItem = 1
            offscreenPageLimit = formatMarqueeModels.size + 2
            setPageTransformer(false, ZoomOutTransformation())
            setTabDot(bottom_tabLayout, tabSize)
        }
        mCarouselPagerAdapter
        mCarouselPagerAdapter.notifyDataSetChanged()
        viewPager.addOnPageChangeListener(mCarouselPagerAdapter)
        bottom_tabLayout.addOnTabSelectedListener(mCarouselPagerAdapter)

    }

    /**
     * 設置輪播之下方圓點
     */
    private fun setTabDot(tabLayout: TabLayout, size: Int) {
        tabLayout.removeAllTabs()
        for (i in 0 until size) {
            tabLayout.addTab(tabLayout.newTab())
        }
    }

}