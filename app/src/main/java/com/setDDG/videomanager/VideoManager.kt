package com.setDDG.videomanager

import android.app.Activity
import android.app.Dialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.set.app.entertainment.videomanager.VideoInterFace
import com.set.app.entertainment.videomanager.VideoPlayCallback
import com.set.app.entertainment.videomanager.VideoPrepareInterFace
import com.setDDG.baseViewPager.StopMainViewPagerScroll
import java.util.*

class VideoManager(var mActivity : Activity,
                   var videoURL : String,
                   var adCode : String,
                   var customLayoutId : Int,
                   private val videoLayout : ConstraintLayout,
                   val position : Int,
                   var viewWidth : Int = 0,
                   var thumbImg : String,
                   var isSeekbar : Boolean,
                   var isShowThumbnail: Boolean,
                   private val videoInterFace : VideoInterFace?,
                   private val videoPrepareInterFace : VideoPrepareInterFace?,
                   private var stopMainViewPagerScroll:StopMainViewPagerScroll? = null
) {


    private val TAG = "VideoManager"
    var exoPlayerManager : ExoPlayerManager? = null
    var youtubeManager : YoutubeManager? = null

    constructor(activity : Activity,
                videoURL : String,
                customLayoutId : Int,
                videoLayout : ConstraintLayout,
                viewWidth : Int,
                thumbImg : String,
                isSeekbar : Boolean,
                interFace : VideoInterFace?,
                prepareInterFace : VideoPrepareInterFace?) : this(activity,
        videoURL,
        "",
        customLayoutId,
        videoLayout,
        0,
        viewWidth,
        thumbImg,
        isSeekbar,
        true,
        interFace,
        prepareInterFace)

    constructor(activity : Activity,
                videoURL : String,
                videoLayout : ConstraintLayout,
                viewWidth : Int,
                thumbImg : String,
                isSeekbar : Boolean,
                videoPrepareInterFace : VideoPrepareInterFace?) : this(activity,
        videoURL,
        "",
        0,
        videoLayout,
        0,
        viewWidth,
        thumbImg,
        isSeekbar,
        true,
        null,
        videoPrepareInterFace)

    //11
    constructor(activity : Activity,
                videoURL : String,
                videoLayout : ConstraintLayout,
                viewWidth : Int,
                position : Int,
                thumbImg : String,
                isSeekbar : Boolean) : this(activity,
        videoURL,
        "",
        0,
        videoLayout,
        position,
        viewWidth,
        thumbImg,
        isSeekbar,
        false,
        null,
        null)

    constructor(activity : Activity,
                videoURL : String,
                videoLayout : ConstraintLayout,
                viewWidth : Int,
                position : Int,
                thumbImg : String,
                isSeekbar : Boolean,
                stopMainViewPagerScroll: StopMainViewPagerScroll) : this(activity,
        videoURL,
        "",
        0,
        videoLayout,
        position,
        viewWidth,
        thumbImg,
        isSeekbar,
        false,
        null,
        null,
        stopMainViewPagerScroll)

    //111
    constructor(activity : Activity,
                videoURL : String,
                videoLayout : ConstraintLayout,
                viewWidth : Int,
                position : Int,
                adCode : String,
                thumbImg : String,
                isSeekbar : Boolean) : this(activity,
        videoURL,
        adCode,
        0,
        videoLayout,
        position,
        viewWidth,
        thumbImg,
        isSeekbar,
        false,
        null,
        null)

    constructor(activity : Activity,
                videoURL : String,
                adCode : String,
                videoLayout : ConstraintLayout,
                thumbImg : String) : this(activity,
        videoURL,
        adCode,
        0,
        videoLayout,
        0,
        0,
        thumbImg,
        true,
        true,
        null,
        null)

    constructor(activity : Activity,
                videoURL : String,
                adCode : String,
                videoLayout : ConstraintLayout,
                thumbImg : String,
                videoPrepareInterFace : VideoPrepareInterFace?) : this(activity,
        videoURL,
        adCode,
        0,
        videoLayout,
        0,
        0,
        thumbImg,
        true,
        true,
        null,
        videoPrepareInterFace)

    constructor(activity : Activity,
                videoURL : String,
                adCode : String,
                videoLayout : ConstraintLayout,
                position : Int,
                thumbImg : String) : this(activity,
        videoURL,
        adCode,
        0,
        videoLayout,
        position,
        0,
        thumbImg,
        true,
        true,
        null,
        null)

    constructor(activity : Activity,
                videoURL : String,
                videoLayout : ConstraintLayout,
                thumbImg : String) : this(
        activity,
        videoURL,
        "",
        0,
        videoLayout,
        0,
        0,
        thumbImg,
        true,
        true,
        null,
        null)

    init {
        initView()
    }

    private fun initView() {
        val mFullScreenDialog : Dialog? =
            object : Dialog(mActivity, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
                override fun onBackPressed() {
                    if (VideoPlayCallback.isFullScreen) {
                        exoPlayerManager?.closeExoPlayerFullScreen()
                        youtubeManager?.closeYoutubeFullScreen()
                    }
                    super.onBackPressed()
                }
            }

        if (isYoutubeUrl(videoURL)) {
            //customLayoutId如果要用預設的就設定為0
            youtubeManager = YoutubeManager(mActivity,
                videoURL,
                customLayoutId, VideoPlayCallback.isMute,
                videoLayout,
                mFullScreenDialog,
                position,
                viewWidth,
                thumbImg,
                isSeekbar,
                isShowThumbnail,
                videoInterFace,
                videoPrepareInterFace,
                //此APP多加的
                stopMainViewPagerScroll)
        } else {
            exoPlayerManager = ExoPlayerManager(mActivity,
                videoURL,
                adCode, VideoPlayCallback.isMute,
                videoLayout,
                mFullScreenDialog,
                position,
                viewWidth,
                thumbImg,
                isSeekbar,
                isShowThumbnail,
                videoPrepareInterFace)
        }
    }

    private fun isYoutubeUrl(url : String) : Boolean {
        return !url.toLowerCase(Locale.ROOT).contains("m3u8")
    }

    fun playerToPlay() {
        //播放
        youtubeManager?.isPlayManager(true)
        exoPlayerManager?.let { exoPlayerManager ->
            exoPlayerManager.isPlay = true
            exoPlayerManager.mSimpleExoPlayer?.playWhenReady = true
        }
    }

    fun playerToPause() {
        //暫停
        exoPlayerManager?.let { exoPlayerManager ->
            exoPlayerManager.mSimpleExoPlayer?.playWhenReady = false
        }
        youtubeManager?.isPlayManager(false)
        VideoPlayCallback.isPlayPosition = -1
    }

    fun playerDestroy() {
        //所有資料清除
        exoPlayerManager?.let { exoPlayerManager ->
            exoPlayerManager.mSimpleExoPlayer?.let {
                it.release()
                exoPlayerManager.mImaAdsLoader?.release()
                exoPlayerManager.mPlayerView = null
                exoPlayerManager.vExoPlayerThumbnail?.setImageDrawable(null)
                exoPlayerManager.vExoPlayerThumbnail?.let {
                    if (!exoPlayerManager.mActivity.isDestroyed){
                        Glide.with(exoPlayerManager.mActivity).clear(it)
                    }
                }
                exoPlayerManager.vPlayIcon?.setImageDrawable(null)
                exoPlayerManager.vExoPlayerFullScreen?.setImageDrawable(null)
                exoPlayerManager.vExoPlayerMute?.setImageDrawable(null)
            }

        }
        youtubeManager?.let { youtubeManager ->
            youtubeManager.vYoutubePlayerView?.let {
                youtubeManager.vYoutubePlayerView?.let {
                    it.release()
                    youtubeManager.vYoutubePlayer = null
                    youtubeManager.vYoutubeThumbnail?.setImageDrawable(null)
                    youtubeManager.vYoutubeThumbnail?.let {
                        if (!youtubeManager.mActivity.isDestroyed){
                            Glide.with(youtubeManager.mActivity).clear(it)
                        }
                    }
                    youtubeManager.mScreenControllerHandler.removeCallbacksAndMessages(null)
                    youtubeManager.vPlayIcon?.setImageDrawable(null)
                    youtubeManager.vYoutubeFullScreen?.setImageDrawable(null)
                    youtubeManager.vYoutubeMute?.setImageDrawable(null)
                }

            }
        }
        if (VideoPlayCallback.isFullScreen) return
        VideoPlayCallback.isPlayPosition = -1

    }

    fun isPlaying() : Boolean {
        exoPlayerManager?.let { exoPlayerManager ->
            return exoPlayerManager.isPlay
        }
        youtubeManager?.let { youtubeManager ->
            return youtubeManager.isPlay
        }
        return false
    }

    fun isPlayerMute() : Boolean {
        exoPlayerManager?.let {
            return it.isMute
        }
        youtubeManager?.let {
            return it.isMute
        }
        return true
    }

    fun disableController() {
        exoPlayerManager?.let {
            it.disableController()
        }
        youtubeManager?.let {
            it.disableController()
        }
    }

    fun enableController() {
        exoPlayerManager?.let {
            it.enableController()
        }
        youtubeManager?.let {
            it.enableController()
        }
    }

    fun setCircleViewColor(color : Int) {
        exoPlayerManager?.let { exoPlayerManager ->
            exoPlayerManager.setCircleViewColor(color)
        }
        youtubeManager?.let { youtubeManager ->
            youtubeManager.setCircleViewColor(color)
        }
    }
}
