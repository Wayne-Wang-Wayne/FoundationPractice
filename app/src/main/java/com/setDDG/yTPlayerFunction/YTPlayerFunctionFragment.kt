package com.setDDG.yTPlayerFunction

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rockex6.practiceappfoundation.R
import com.set.app.entertainment.videomanager.VideoPlayCallback.Companion.videoManager
import com.setDDG.baseViewPager.StopMainViewPagerScroll
import com.setDDG.util.screenViewDP
import com.setDDG.videomanager.VideoManager
import kotlinx.android.synthetic.main.fragment_y_t_player_function.*
import kotlin.properties.Delegates


class YTPlayerFunctionFragment(private val stopMainViewPagerScroll: StopMainViewPagerScroll) :
    Fragment() {

    lateinit var mContext: Context
    private lateinit var yTPlayerFunctionViewModel: YTPlayerFunctionViewModel
    private var viewWidth by Delegates.notNull<Int>()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_y_t_player_function, container, false)
    }

    companion object {
        fun newInstance(stopMainViewPagerScroll: StopMainViewPagerScroll) =
            YTPlayerFunctionFragment(stopMainViewPagerScroll)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewWidth =
            mContext.resources.displayMetrics.widthPixels - screenViewDP(mContext, R.dimen._30dp)
        yTPlayerFunctionViewModel =
            ViewModelProvider(this).get(YTPlayerFunctionViewModel::class.java)
        observeViewModel()
        yTPlayerFunctionViewModel.fetchVideo()

    }

    private fun observeViewModel() {
        yTPlayerFunctionViewModel.videoListData.observe(viewLifecycleOwner, Observer { videoData ->
            videoManager =
                VideoManager(mContext as Activity, videoData.url, vVideoPlayer, viewWidth, 0,
                    videoData.imageUrl, true, stopMainViewPagerScroll)

        })
    }
}