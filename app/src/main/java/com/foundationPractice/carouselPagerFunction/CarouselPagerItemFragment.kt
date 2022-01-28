package com.foundationPractice.carouselPagerFunction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rockex6.practiceappfoundation.R
import com.foundationPractice.util.loadImage
import kotlinx.android.synthetic.main.fragment_carousel_pager_item.*
import java.util.*


class CarouselPagerItemFragment(
    private val mFormatMarqueeModels: ArrayList<PokemonPicModel>,
    val position: Int) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_carousel_pager_item, container, false)
    }

    companion object {
        fun newInstance(mFormatMarqueeModels: ArrayList<PokemonPicModel>, position: Int) =
            CarouselPagerItemFragment(mFormatMarqueeModels, position)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        image_news_view_item.loadImage(this, mFormatMarqueeModels[position].picUrl)
    }
}