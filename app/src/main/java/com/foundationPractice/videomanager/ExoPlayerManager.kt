package com.setDDG.videomanager

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ext.ima.ImaAdsLoader
import com.google.android.exoplayer2.source.ads.AdsMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.DefaultTimeBar
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.setDDG.videomanager.VideoPlayCallback.Companion.isFullScreen
import com.setDDG.videomanager.VideoPlayCallback.Companion.onUpdatePlay
import com.setDDG.videomanager.VideoPlayCallback.Companion.showFullScreenDialog
import kotlinx.android.synthetic.main.exo_player_control_view.view.*
import kotlinx.android.synthetic.main.item_exoplayer_controller.view.*
import com.rockex6.practiceappfoundation.R
import com.setDDG.util.*

class ExoPlayerManager(
    var mActivity: Activity,
    var videoURL: String,
    var adCode: String,
    var isMuteDefault: Boolean,
    var vExoPlayerLayout: ConstraintLayout,
    private var mFullScreenDialog: Dialog?,
    var position: Int,
    var viewWidth: Int,
    var thumbImg: String,
    var isSeekBar: Boolean,
    var isShowThumbnail: Boolean,
    var videoPrepareInterFace: VideoPrepareInterFace?) : View.OnClickListener {


    private val TAG = "ExoPlayerManager"
    var mPlayerView: PlayerView? = null
    var mSimpleExoPlayer: SimpleExoPlayer? = null
    var mImaAdsLoader: ImaAdsLoader? = null
    var vExoPlayerFullScreen: ImageView? = null
    var vExoTimeBar: RelativeLayout? = null
    var vExoPlayerThumbnail: AppCompatImageView? = null
    var vPlayIcon: ImageView? = null
    var isMute: Boolean = false
    var vExoPlayerMute: ImageView? = null
    var isPlay: Boolean = false
    var vExoPlayerController: View? = null
    private var vExoPlayerView = View(mActivity)
    private var vExoSeekBar: DefaultTimeBar? = null
    private var vConstraintSet: ConstraintSet? = null
    private var exitFullScreenHeight = 0
    private var vChangeLineColor = View(mActivity)
    private var vProgressBar: ProgressBar? = null
    private var viewCircleView = View(mActivity)
    private var videoDurationLong: Long = 0
    var startPoint: Long = 0
    private var scrollingTimeResult: Long? = null
    private var mIsScrolling = false
    private var vVideoCover: ImageView? = null
    private var vExoplayerPlayLayout: RelativeLayout? = null
    var mScreenControllerHandler = Handler(Looper.getMainLooper())
    private var isScreenControllerRunnable = false
    private var delayMillis = 5000
    private var mScreenRunnable: Runnable = Runnable {
        isScreenControllerRunnable = false
        vExoPlayerMute?.visibility = View.INVISIBLE
        vExoSeekBar?.visibility = View.INVISIBLE
        vExoTimeBar?.visibility = View.INVISIBLE
        vExoPlayerFullScreen?.visibility = View.INVISIBLE
        vExoplayerPlayLayout?.visibility = View.INVISIBLE
        isStateController = false
    }
    private var exo_position:TextView? = null
    private var exo_duration:TextView? = null
    private var isStateController: Boolean = false
    private var isDoubleClicked = false


    init {

        if (viewWidth != 0) {
            vExoPlayerLayout.layoutParams =
                ConstraintLayout.LayoutParams(viewWidth, viewScaleCustomerHeight(viewWidth))
            exitFullScreenHeight = viewScaleCustomerHeight(viewWidth)
        } else {
            vExoPlayerLayout.layoutParams =
                ConstraintLayout.LayoutParams(viewScaleWidth(mActivity), viewScaleHeight(mActivity))
            exitFullScreenHeight = viewScaleHeight(mActivity)
        }

        vConstraintSet = ConstraintSet()
        vConstraintSet?.clone(vExoPlayerLayout)

        initExoPlayerView()
    }

    private fun initExoPlayerView() {
        mPlayerView = PlayerView(mActivity)
        mSimpleExoPlayer = SimpleExoPlayer.Builder(mActivity, DefaultRenderersFactory(mActivity))
            .setLoadControl(DefaultLoadControl.Builder()
                .setPrioritizeTimeOverSizeThresholds(false)
                .createDefaultLoadControl())
            .build()

        mImaAdsLoader = ImaAdsLoader(mActivity, Uri.parse(adCode))
        mImaAdsLoader?.setPlayer(mSimpleExoPlayer)
        mImaAdsLoader?.adsLoader?.addAdErrorListener {
            //廣告錯誤
            Logger.i(TAG, " addAdErrorListener")
            Logger.i(TAG, " it.error= " + it.error)
            Logger.i(TAG, " it.userRequestContext= " + it.userRequestContext)
        }

        mPlayerView?.player = mSimpleExoPlayer
        mPlayerView?.let {
            vExoPlayerMute = it.vExoPlayerMute
            vExoSeekBar = it.exo_progress
            vExoTimeBar = it.vExoTimeBar
            vProgressBar = it.vProgressBar
            vExoPlayerFullScreen = it.vExoPlayerFullScreen
            vExoPlayerController = it.vExoPlayerController
            vVideoCover = it.vVideoBlackCover
            vExoplayerPlayLayout = it.vExoplayerPlayLayout
            exo_position = it.exo_position
            exo_duration = it.exo_duration
        }

        vExoPlayerMute?.setOnClickListener(this)
        vExoPlayerFullScreen?.setOnClickListener(this)
        val dataSourceFactory = DefaultDataSourceFactory(mActivity,
            Util.getUserAgent(mActivity, mActivity.getString(R.string.app_name)))
        val source: HlsMediaSource = HlsMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse(videoURL))
        val adsMediaSource = AdsMediaSource(source, dataSourceFactory, mImaAdsLoader, mPlayerView)
        mSimpleExoPlayer?.addListener(object : Player.EventListener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                isPlay = isPlaying
                //暫停與播放
                if (isPlaying) {
                    vProgressBar?.visibility = View.GONE
                    vExoPlayerThumbnail?.visibility = View.GONE
                    vPlayIcon?.visibility = View.GONE
                    onUpdatePlay().onUpdate(position)
                }
                showFullScreenHandler()
            }

            override fun onSeekProcessed() {
                super.onSeekProcessed()
                mPlayerView?.showController()

            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                super.onPlayerStateChanged(playWhenReady, playbackState)
                //影片載入成功做的事情
                if (playbackState == ExoPlayer.STATE_READY) {
                    videoDurationLong = mSimpleExoPlayer!!.duration
                    mPlayerView?.controllerHideOnTouch = false
                    mPlayerView?.controllerShowTimeoutMs = 0
                }
                vExoSeekBar?.isEnabled = false

                if (playbackState == 2) {
                    vProgressBar?.visibility = View.VISIBLE
                } else {
                    vProgressBar?.visibility = View.GONE
                }

                startPoint = mSimpleExoPlayer?.currentPosition!!
            }

        })


        mSimpleExoPlayer?.prepare(adsMediaSource)
        isPlay = false
        mSimpleExoPlayer?.playWhenReady = false
        isExoPlayerMutingManager(isMuteDefault)
        setExoPlayerView()
        setGestureAndCover()
    }

    private fun setExoPlayerView() {
        vExoPlayerView = mPlayerView as View
        vExoPlayerView.id = View.generateViewId()

        vExoPlayerLayout.addView(vExoPlayerView)
        vConstraintSet?.applyTo(vExoPlayerLayout)
        vChangeLineColor.id = View.generateViewId()
        if (isShowThumbnail) {
            initThumbnailView()
        }
        if (viewWidth != 0) {
            initViewCircle()
        }
        if (isSeekBar) {
            vExoSeekBar?.visibility = View.VISIBLE
            vExoTimeBar?.visibility = View.VISIBLE
        }

        videoPrepareInterFace?.onVideoPrepare()
    }

    override fun onClick(v: View?) {
        when (v) {
            vExoPlayerMute -> {
                if (isMute) {
                    isExoPlayerMutingManager(false)
                    VideoPlayCallback.isMute = false
                } else {
                    isExoPlayerMutingManager(true)
                    VideoPlayCallback.isMute = true
                }
            }

            vExoPlayerFullScreen -> {
                if (isFullScreen) {
                    vExoPlayerMute?.setPadding(0, 0, 0, 0)
                    vExoTimeBar?.setPadding(0, 0, 0, 0)
                    closeExoPlayerFullScreen()
                } else {
                    isPlay = true
                    mSimpleExoPlayer?.playWhenReady = true
                    vExoPlayerMute?.setPadding(0, 0, 0, 25)
                    vExoTimeBar?.setPadding(0, 0, 0, 25)
                    VideoPlayCallback.isPlayPosition = position
                    vExoPlayerFullScreen?.setImageResource(R.drawable.ic_fullscreen_exit)
                    vConstraintSet?.constrainHeight(vExoPlayerView.id, viewScaleHeight(mActivity))
                    vConstraintSet?.applyTo(vExoPlayerLayout)
                    showFullScreenDialog(mActivity, vExoPlayerView, mFullScreenDialog)
                }
            }

            vExoPlayerThumbnail -> {

                vExoPlayerThumbnail?.visibility = View.GONE
                vPlayIcon?.visibility = View.GONE
                mSimpleExoPlayer?.playWhenReady = true
                isPlay = true
            }

            vExoPlayerController->{
                //畫面controller

                val handler = Handler()
                val r = Runnable {
                    //單點要做的事
                    if (isStateController) {
                        isStateController = false
                        setControllerShow(false)
                    } else {
                        isStateController = true
                        setControllerShow(true)
                    }
                    isDoubleClicked = false
                }

                if(isDoubleClicked){
                    //雙擊做的事
                    if(isPlay){
                        mSimpleExoPlayer?.playWhenReady = false
                        isPlay = false
                        isStateController = false
                        setControllerShow(false)

                    }else{
                        mSimpleExoPlayer?.playWhenReady = true
                        isPlay = true
                        isStateController = true
                        setControllerShow(true)
                    }

                    isDoubleClicked=false
                    handler.removeCallbacks(r)
                }
                else{
                    isDoubleClicked=true;
                    handler.postDelayed(r,300)

                }


            }
        }


        showFullScreenHandler()
    }

    fun isExoPlayerMutingManager(mute: Boolean) {
        isMute = if (mute) {
            mSimpleExoPlayer?.volume = 0F
            vExoPlayerMute?.setImageResource(R.drawable.ic_mute)
            true
        } else {
            mSimpleExoPlayer?.volume = 100F
            vExoPlayerMute?.setImageResource(R.drawable.ic_unmute)
            false
        }
    }

    fun closeExoPlayerFullScreen() {
        VideoPlayCallback.closeFullScreenDialog(mActivity, mFullScreenDialog)
        vExoPlayerView.parent?.let {
            (vExoPlayerView.parent as ViewGroup).removeView(vExoPlayerView)
        }
        vExoPlayerLayout.addView(vExoPlayerView)
        vConstraintSet?.constrainHeight(vExoPlayerView.id, exitFullScreenHeight)
        vConstraintSet?.applyTo(vExoPlayerLayout)
        vExoPlayerFullScreen?.setImageResource(R.drawable.ic_fullscreen)
    }

    private fun initViewCircle() {
        viewCircleView.id = View.generateViewId()
        setCircleViewColor(R.drawable.shape_video_white_corner)
        vExoPlayerLayout.addView(viewCircleView)
        vConstraintSet?.apply {
            connect(viewCircleView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID,
                ConstraintSet.TOP)
            connect(viewCircleView.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM)
            connect(viewCircleView.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID,
                ConstraintSet.RIGHT)
            connect(viewCircleView.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID,
                ConstraintSet.LEFT)
            connect(vExoPlayerLayout.id, ConstraintSet.TOP, viewCircleView.id, ConstraintSet.TOP)
            connect(vExoPlayerLayout.id, ConstraintSet.BOTTOM, viewCircleView.id,
                ConstraintSet.BOTTOM)
            connect(vExoPlayerLayout.id, ConstraintSet.RIGHT, viewCircleView.id,
                ConstraintSet.RIGHT)
            connect(vExoPlayerLayout.id, ConstraintSet.LEFT, viewCircleView.id, ConstraintSet.LEFT)
            applyTo(vExoPlayerLayout)
        }
    }

    private fun initThumbnailView() {
        vExoPlayerThumbnail = AppCompatImageView(mActivity)
        vPlayIcon = ImageView(mActivity)
        vExoPlayerThumbnail?.id = View.generateViewId()
        vPlayIcon?.id = View.generateViewId()
        vExoPlayerThumbnail?.setOnClickListener(this)
        vExoPlayerThumbnail?.let {
            it.apply {
                loadImage(mActivity, thumbImg)
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
        }
        vPlayIcon?.setImageResource(R.drawable.ic_video_hint_big)
        vExoPlayerLayout.addView(vExoPlayerThumbnail)
        vExoPlayerLayout.addView(vPlayIcon)
        vConstraintSet?.apply {
            connect(vExoPlayerThumbnail?.id!!, ConstraintSet.BOTTOM, vExoPlayerLayout.id,
                ConstraintSet.BOTTOM)
            connect(vExoPlayerThumbnail?.id!!, ConstraintSet.RIGHT, vExoPlayerLayout.id,
                ConstraintSet.RIGHT)
            connect(vExoPlayerThumbnail?.id!!, ConstraintSet.LEFT, vExoPlayerLayout.id,
                ConstraintSet.LEFT)
            connect(vExoPlayerThumbnail?.id!!, ConstraintSet.TOP, vExoPlayerLayout.id,
                ConstraintSet.TOP)
            connect(vPlayIcon?.id!!, ConstraintSet.BOTTOM, vExoPlayerLayout.id,
                ConstraintSet.BOTTOM)
            connect(vPlayIcon?.id!!, ConstraintSet.RIGHT, vExoPlayerLayout.id, ConstraintSet.RIGHT)
            connect(vPlayIcon?.id!!, ConstraintSet.LEFT, vExoPlayerLayout.id, ConstraintSet.LEFT)
            connect(vPlayIcon?.id!!, ConstraintSet.TOP, vExoPlayerLayout.id, ConstraintSet.TOP)
            constrainHeight(vPlayIcon?.id!!, screenViewDP(mActivity, R.dimen._70dp))
            constrainWidth(vPlayIcon?.id!!, screenViewDP(mActivity, R.dimen._70dp))
            applyTo(vExoPlayerLayout)
        }
    }

    fun disableController() {
        vExoPlayerController?.visibility = View.GONE
    }

    fun enableController() {
        vExoPlayerController?.visibility = View.VISIBLE
    }

    fun setCircleViewColor(id: Int) {
        viewCircleView.background = ContextCompat.getDrawable(mActivity, id)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setGestureAndCover() {
        val gestureDetector =
            GestureDetector(mActivity, object : GestureDetector.SimpleOnGestureListener() {
                override fun onScroll(
                    e1: MotionEvent?,
                    e2: MotionEvent?,
                    distanceX: Float,
                    distanceY: Float): Boolean {
                    //滑動期間要做的事
                    if (e2 != null && e1 != null) {
                        //時間邏輯
                        mPlayerView?.player?.playWhenReady = false
                        scrollingTimeResult =
                            (startPoint + ((videoDurationLong / 1500) * (e2.x - e1.x).toLong()))
                        if(scrollingTimeResult!! < 0){
                            mSimpleExoPlayer?.seekTo(0)
                        }else if(scrollingTimeResult!! > videoDurationLong){
                            mSimpleExoPlayer?.seekTo(videoDurationLong)
                        }else{
                            mSimpleExoPlayer?.seekTo(scrollingTimeResult!!)
                        }

                        //改變黑幕大小
                        val parent = vVideoCover?.parent
                        val parentWidth = (parent as View).width.toLong()

                        if (videoDurationLong != 0L) {
                            val newWidth =
                                (parentWidth.toFloat() * (scrollingTimeResult!!.toFloat() / (videoDurationLong.toFloat()))).toInt()
                            vVideoCover?.layoutParams?.width = newWidth
                        }

                        //控制滑動時controller顯示
                        vVideoCover?.visibility = View.VISIBLE
                        vExoSeekBar?.visibility = View.VISIBLE
                        vExoTimeBar?.visibility = View.VISIBLE
                        vProgressBar?.visibility = View.INVISIBLE
                    }

                    mIsScrolling = true
                    return true
                }
                override fun onDown(e: MotionEvent?): Boolean {
                    return true
                }
            })

        val mGestureListener = View.OnTouchListener { v, event ->
            //讓滑動影片時，畫面不會亂跑
            v.parent.requestDisallowInterceptTouchEvent(true)
            if (gestureDetector.onTouchEvent(event)) {
                return@OnTouchListener true
            }
            if (event.action == MotionEvent.ACTION_UP) {
                //滑動結束要做的事
                if (mIsScrolling) {
                    mIsScrolling = false

                    mPlayerView?.player?.playWhenReady = true

                    //紀錄滑動結束時間
                    startPoint = mSimpleExoPlayer?.currentPosition!!

                    //控制滑動結束時controller顯示
                    vVideoCover?.visibility = View.INVISIBLE
                    if (isStateController) {
                        setControllerShow(true)
                    } else {
                        setControllerShow(false)
                    }

                } else {
                    onClick(vExoPlayerController)
                }
            }
            false
        }
        vExoPlayerController?.setOnTouchListener(mGestureListener)
    }
    private fun showFullScreenHandler() {
        if (isScreenControllerRunnable) {
            isScreenControllerRunnable = false
            mScreenControllerHandler.removeCallbacks(mScreenRunnable)
        }
        isScreenControllerRunnable = true
        mScreenControllerHandler.postDelayed(mScreenRunnable, delayMillis.toLong())
    }

    private fun setControllerShow(boolean: Boolean){
        if(boolean){
            vExoPlayerMute?.visibility = View.VISIBLE
            vExoSeekBar?.visibility = View.VISIBLE
            vExoTimeBar?.visibility = View.VISIBLE
            vExoPlayerFullScreen?.visibility = View.VISIBLE
            vExoplayerPlayLayout?.visibility = View.VISIBLE
        }else{
            vExoPlayerMute?.visibility = View.INVISIBLE
            vExoSeekBar?.visibility = View.INVISIBLE
            vExoTimeBar?.visibility = View.INVISIBLE
            vExoPlayerFullScreen?.visibility = View.INVISIBLE
            vExoplayerPlayLayout?.visibility = View.INVISIBLE
        }
    }
}
