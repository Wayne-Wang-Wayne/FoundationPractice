package com.foundationPractice.videomanager

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.utils.TimeUtilities
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.views.YouTubePlayerSeekBar
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.views.YouTubePlayerSeekBarListener
import com.rockex6.practiceappfoundation.R
import com.foundationPractice.videomanager.VideoPlayCallback.Companion.closeFullScreenDialog
import com.foundationPractice.videomanager.VideoPlayCallback.Companion.isFullScreen
import com.foundationPractice.videomanager.VideoPlayCallback.Companion.onUpdatePlay
import com.foundationPractice.videomanager.VideoPlayCallback.Companion.showFullScreenDialog
import com.foundationPractice.util.*
import com.foundationPractice.util.UrlUtil.getYoutubeID

class YoutubeManager(
    var mActivity: Activity,
    var YoutubeURL: String,
    var customLayoutId: Int,
    var isMuteDefault: Boolean,
    var vYoutubeLayout: ConstraintLayout,
    private var mFullScreenDialog: Dialog?,
    var position: Int,
    var viewWidth: Int,
    var thumbImg: String,
    var isSeekBar: Boolean,
    var isShowThumbnail: Boolean = true,
    val videoInterFace: VideoInterFace?,
    private val videoPrepareInterFace: VideoPrepareInterFace?
) : View.OnClickListener {
    private val TAG = "YoutubeManager"
    private var vYoutubePlay: ImageButton? = null
    private var vYoutubeReturn: ImageButton? = null
    private var vYoutubeNext: ImageButton? = null
    var vYoutubeMute: ImageView? = null
    private var vYoutubeControllerLayout: ConstraintLayout? = null
    private var vTimeBar: TextView? = null
    private var vYoutubePlayerSeekBar: YouTubePlayerSeekBar? = null
    private var vController: View? = null
    private var vProgressBar: ProgressBar? = null
    private var delayMillis = 5000
    private var isScreenControllerRunnable = false
    private lateinit var mScreenRunnable: Runnable
    private var isStateController: Boolean = false
    private var videoDuration: String = ""
    private var videoDurationFloat: Float = 0F
    private var vConstraintSet: ConstraintSet? = null
    private var vYoutubeView = View(mActivity)
    var vYoutubeThumbnail: AppCompatImageView? = null
    var vPlayIcon: ImageView? = null
    private var exitFullScreenHeight = 0
    var vYoutubePlayerView: YouTubePlayerView? = null
    var vYoutubePlayer: YouTubePlayer? = null
    var vYoutubeFullScreen: ImageView? = null
    var isMute: Boolean = false
    var isPlay: Boolean = false
    var mScreenControllerHandler = Handler(Looper.getMainLooper())
    var viewCircleView = View(mActivity)
    var startPoint: Float = 0F
    private var mIsScrolling = false
    private var vVideoCover: ImageView? = null
    private var vYoutubeControllerBar: RelativeLayout? = null
    private val handler: Handler = Handler(Looper.getMainLooper())
    private var scrollingTimeResult: Float? = null
    private var isDoubleClicked = false


    init {
        if (viewWidth == 0) {
            //全螢幕寬度16:9
            vYoutubeLayout.layoutParams.height = viewScaleHeight(mActivity)
            vYoutubeLayout.layoutParams.width = viewScaleWidth(mActivity)
            exitFullScreenHeight = viewScaleHeight(mActivity)
        } else {
            //客製化寬度16:9
            vYoutubeLayout.layoutParams.height = viewScaleCustomerHeight(viewWidth)
            vYoutubeLayout.layoutParams.width = viewWidth
            exitFullScreenHeight = viewScaleCustomerHeight(viewWidth)
        }
        vConstraintSet = ConstraintSet()
        vConstraintSet?.clone(vYoutubeLayout)
        initYoutubeController()
    }

    private fun initYoutubeController() {
        vYoutubePlayerView = YouTubePlayerView(mActivity)
        val customView: View? = vYoutubePlayerView?.let { youtubePlayerView ->
            youtubePlayerView.apply {
                if (customLayoutId != 0) {
                    inflateCustomPlayerUi(customLayoutId)
                } else {
                    inflateCustomPlayerUi(R.layout.item_youtube_layout)
                }
            }
        }
        customView?.let {
            it.apply {
                vYoutubeControllerLayout = findViewById(R.id.vYoutubeControllerLayout)
                vController = findViewById(R.id.vYoutubeController)
                vYoutubeReturn = findViewById(R.id.vYoutubeReturn)
                vYoutubeNext = findViewById(R.id.vYoutubeNext)
                vYoutubePlay = findViewById(R.id.vYoutubePlay)
                vYoutubeMute = findViewById(R.id.vYoutubeMute)
                vYoutubeFullScreen = findViewById(R.id.vYoutubeFullScreen)
                vTimeBar = findViewById(R.id.vTimeBar)
                vProgressBar = findViewById(R.id.vProgressBar)
                vYoutubePlayerSeekBar = findViewById(R.id.vYoutubePlayerSeekBar)
                vVideoCover = findViewById(R.id.vVideoBlackCover)
                vYoutubeControllerBar = findViewById(R.id.vYoutubeControllerBar)
            }
        }

        vYoutubeControllerLayout?.setOnClickListener(this)
        vController?.setOnClickListener(this)
        vYoutubePlay?.setOnClickListener(this)
        vYoutubeMute?.setOnClickListener(this)
        vYoutubeFullScreen?.setOnClickListener(this)


        //有用到上下集才會使用
//        if (videoAPIList.size > 0) {
//            vYoutubeReturn?.visibility = View.VISIBLE
//            vYoutubeNext?.visibility = View.VISIBLE
//            if (videoAPIList[0].position == 0) {
//                vYoutubeReturn?.setImageResource(R.drawable.ic_gray_last_episode)
//            } else {
//                vYoutubeReturn?.setOnClickListener(this)
//            }
//            if (videoAPIList[0].position == (videoAPIList.size - 1)) {
//                vYoutubeNext?.setImageResource(R.drawable.ic_gray_next_episode)
//            } else {
//                vYoutubeNext?.setOnClickListener(this)
//            }
//        }
        isStateController = true
        mScreenRunnable = Runnable {
            isScreenControllerRunnable = false
            vYoutubeControllerBar?.visibility = View.INVISIBLE
            isStateController = false
        }

        showFullScreenHandler()

        setYoutubePlayer() //初始youtube播放控制
    }


    private fun setYoutubePlayer() {
        vYoutubeView = vYoutubePlayerView as View
        vYoutubeView.id = View.generateViewId()
        vYoutubeLayout.addView(vYoutubeView)

        if (customLayoutId == 0) {
            if (isSeekBar) {
                //Show SeekBar
                vYoutubePlayerSeekBar?.visibility = View.VISIBLE
                vTimeBar?.visibility = View.VISIBLE
                initYoutubePlayerSeekBar()
                if (viewWidth != 0 && isSeekBar) {
                    vConstraintSet?.constrainHeight(vYoutubeView.id,
                        viewScaleCustomerHeight(viewWidth))
                } else {
                    vConstraintSet?.constrainHeight(vYoutubeView.id, viewScaleHeight(mActivity))
                }
                vConstraintSet?.applyTo(vYoutubeLayout)
            }
        }

//        if (isShowThumbnail) {
        initThumbnailView()
//        }
        if (viewWidth != 0) {
            initViewCircle()
        }
        setYouTubePlayerListener()
    }

    private fun showFullScreenHandler() {
        if (isScreenControllerRunnable) {
            isScreenControllerRunnable = false
            mScreenControllerHandler.removeCallbacks(mScreenRunnable)
        }
        isScreenControllerRunnable = true
        mScreenControllerHandler.postDelayed(mScreenRunnable, delayMillis.toLong())
    }

    private fun setYouTubePlayerListener() {
        vYoutubePlayerView?.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                vController?.visibility = VISIBLE
//                vYoutubeControllerBar?.visibility = VISIBLE
                vYoutubePlayer = youTubePlayer
                vYoutubePlayer?.cueVideo(getYoutubeID(YoutubeURL)!!, 0f)

                videoPrepareInterFace?.onVideoPrepare()
                VideoPlayCallback.onVideoReady()
                    .onUpdate()
                isMutingManager(isMuteDefault)
                vYoutubePlayerSeekBar?.let {
                    vYoutubePlayer?.addListener(vYoutubePlayerSeekBar!!)
                    it.youtubePlayerSeekBarListener = object : YouTubePlayerSeekBarListener {
                        override fun seekTo(time: Float) {
                            vYoutubePlayer?.seekTo(time)
                            isStateController = true
//                            vYoutubeControllerBar?.visibility = View.VISIBLE
                            showFullScreenHandler()
                            startPoint = time
                        }
                    }
                }

                vYoutubePlayerSeekBar?.videoCurrentTimeTextView?.doOnTextChanged { text, start, before, count ->
                    val currentTimeString =
                        vYoutubePlayerSeekBar?.videoCurrentTimeTextView?.text.toString()
                    vTimeBar?.text = ("$currentTimeString / $videoDuration")
                }
                setGestureAndCover()
            }

            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                super.onCurrentSecond(youTubePlayer, second)
//                if (TimeUtilities.formatTime(second) == videoDuration) {
//                    if (videoAPIList.size > 0) {
//                        VideoPlayCallback.onNextVideo(mActivity)
//                            .onUpdate()
//                    }
//                }
                startPoint = second
            }

            override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {
                super.onVideoDuration(youTubePlayer, duration)
                videoDurationFloat = duration
                videoDuration = TimeUtilities.formatTime(duration)
            }

            override fun onStateChange(
                youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
                super.onStateChange(youTubePlayer, state)
                when (state) {
                    PlayerConstants.PlayerState.BUFFERING -> {
                        vProgressBar?.visibility = View.GONE
                    }

                    PlayerConstants.PlayerState.PLAYING -> {
                        vProgressBar?.visibility = View.GONE
                        vYoutubePlay?.setImageResource(R.drawable.ic_media_pause)
                        vYoutubeThumbnail?.visibility = View.GONE
                        vPlayIcon?.visibility = View.GONE
                        onUpdatePlay().onUpdate(position)
                    }

                    PlayerConstants.PlayerState.PAUSED -> {
                        vYoutubePlay?.setImageResource(R.drawable.ic_play_video)
                    }

                    PlayerConstants.PlayerState.ENDED -> {
                        videoInterFace?.onVideoEnd()
                    }

                    else -> {
                    }
                }
            }

            override fun onError(
                youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {
                super.onError(youTubePlayer, error)
                Logger.i(TAG, "error= " + error)
            }
        })
    }

    override fun onClick(v: View?) {
        when (v) {
            vYoutubeControllerLayout, vController -> {
                val handler = Handler()
                //畫面controller
                val r = Runnable {
                    //單點做的事
                    if (isStateController) {
                        isStateController = false
                        vYoutubeControllerBar?.visibility = View.INVISIBLE
                    } else {
                        isStateController = true
                        vYoutubeControllerBar?.visibility = View.VISIBLE
                    }
                    isDoubleClicked = false
                }

                if (isDoubleClicked) {
                    //雙擊做的事
                    vYoutubePlay?.performClick()
                    isDoubleClicked = false
                    handler.removeCallbacks(r)

                } else {
                    isDoubleClicked = true
                    handler.postDelayed(r, 300)

                }

            }

            vYoutubePlay -> {
                //暫停與播放
                if (isPlay) {
                    isPlayManager(false)
                } else {
                    isPlayManager(true)
                }
            }

            vYoutubeMute -> {
                //靜音
                if (isMute) {
                    isMutingManager(false)
                    VideoPlayCallback.isMute = false
                } else {
                    isMutingManager(true)
                    VideoPlayCallback.isMute = true
                }
            }

            vYoutubeFullScreen -> {
                //全螢幕開關
                if (isFullScreen) {
                    vYoutubePlayerSeekBar?.setPadding(0, 0, 0, 0)
                    vYoutubeMute?.setPadding(0, 0, 0, 0)
                    vTimeBar?.setPadding(0, 0, 0, 0)
                    closeYoutubeFullScreen()
                } else {
                    isPlayManager(true)
                    vYoutubePlayerSeekBar?.setPadding(20, 0, 20, 20)
                    vYoutubeMute?.setPadding(0, 0, 0, 25)
                    vTimeBar?.setPadding(0, 0, 0, 25)
                    vConstraintSet?.constrainHeight(vYoutubeView.id, viewScaleHeight(mActivity))
                    vConstraintSet?.applyTo(vYoutubeLayout)
                    showFullScreenDialog(mActivity, vYoutubeView, mFullScreenDialog)
                    vYoutubeFullScreen?.setImageResource(R.drawable.ic_fullscreen_exit)
                }
            }

//            vYoutubeReturn -> {
//                VideoPlayCallback.onReturnVideo(mActivity)
//                    .onUpdate()
//            }
//
//            vYoutubeNext -> {
//                VideoPlayCallback.onNextVideo(mActivity)
//                    .onUpdate()
//            }

            vYoutubeThumbnail -> {
                vYoutubeThumbnail?.visibility = View.GONE
                vPlayIcon?.visibility = View.GONE
                isPlayManager(true)
            }
        }
        showFullScreenHandler()
    }

    fun isPlayManager(play: Boolean) {
        isPlay = if (play) {
            vYoutubePlayer?.play()
            vYoutubePlay?.setImageResource(R.drawable.ic_media_pause)
            true
        } else {
            vYoutubePlayer?.pause()
            vYoutubePlay?.setImageResource(R.drawable.ic_play_video)
            false
        }
    }

    fun isMutingManager(mute: Boolean) {
        isMute = if (mute) {
            vYoutubePlayer?.mute()
            vYoutubeMute?.setImageResource(R.drawable.ic_mute)
            true
        } else {
            vYoutubePlayer?.unMute()
            vYoutubeMute?.setImageResource(R.drawable.ic_unmute)
            false
        }
    }

    fun closeYoutubeFullScreen() {
        //關閉全螢幕時
        closeFullScreenDialog(mActivity, mFullScreenDialog)
        vYoutubeView.parent?.let {
            (vYoutubeView.parent as ViewGroup).removeView(vYoutubeView)
        }
        vYoutubeLayout.addView(vYoutubeView)
        vConstraintSet?.apply {
            constrainHeight(vYoutubeView.id, exitFullScreenHeight)
            applyTo(vYoutubeLayout)
        }
        vYoutubeFullScreen?.setImageResource(R.drawable.ic_fullscreen)
    }

    private fun initViewCircle() {
        viewCircleView.id = View.generateViewId()
        setCircleViewColor(R.drawable.shape_video_white_corner)
        vYoutubeLayout.addView(viewCircleView)
        vConstraintSet?.apply {
            connect(viewCircleView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID,
                ConstraintSet.TOP)
            connect(viewCircleView.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM)
            connect(viewCircleView.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID,
                ConstraintSet.RIGHT)
            connect(viewCircleView.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID,
                ConstraintSet.LEFT)
            connect(vYoutubeView.id, ConstraintSet.TOP, viewCircleView.id, ConstraintSet.TOP)
            connect(vYoutubeView.id, ConstraintSet.BOTTOM, viewCircleView.id, ConstraintSet.BOTTOM)
            connect(vYoutubeView.id, ConstraintSet.RIGHT, viewCircleView.id, ConstraintSet.RIGHT)
            connect(vYoutubeView.id, ConstraintSet.LEFT, viewCircleView.id, ConstraintSet.LEFT)
            applyTo(vYoutubeLayout)
        }
    }

    private fun initThumbnailView() {
        //影片預覽圖？
        vYoutubeThumbnail = AppCompatImageView(mActivity)
        vPlayIcon = ImageView(mActivity)
        vYoutubeThumbnail?.setOnClickListener(this)
        vYoutubeThumbnail?.let {
            it.apply {
                loadImage(mActivity, thumbImg)
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
        }
        vPlayIcon?.setImageResource(R.drawable.ic_video_hint_big)
        vYoutubeThumbnail?.id = View.generateViewId()
        vPlayIcon?.id = View.generateViewId()
        vYoutubeLayout.addView(vYoutubeThumbnail)
        vYoutubeLayout.addView(vPlayIcon)
        vYoutubeThumbnail?.let {
            vConstraintSet?.apply {
                connect(vYoutubeThumbnail!!.id, ConstraintSet.BOTTOM, vYoutubeLayout.id,
                    ConstraintSet.BOTTOM)
                connect(vYoutubeThumbnail!!.id, ConstraintSet.RIGHT, vYoutubeLayout.id,
                    ConstraintSet.RIGHT)
                connect(vYoutubeThumbnail!!.id, ConstraintSet.LEFT, vYoutubeLayout.id,
                    ConstraintSet.LEFT)
                connect(vYoutubeThumbnail!!.id, ConstraintSet.TOP, vYoutubeLayout.id,
                    ConstraintSet.TOP)
                connect(vPlayIcon?.id!!, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID,
                    ConstraintSet.BOTTOM)
                connect(vPlayIcon?.id!!, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID,
                    ConstraintSet.RIGHT)
                connect(vPlayIcon?.id!!, ConstraintSet.LEFT, ConstraintSet.PARENT_ID,
                    ConstraintSet.LEFT)
                connect(vPlayIcon?.id!!, ConstraintSet.TOP, ConstraintSet.PARENT_ID,
                    ConstraintSet.TOP)
                constrainHeight(vPlayIcon?.id!!, screenViewDP(mActivity, R.dimen._70dp))
                constrainWidth(vPlayIcon?.id!!, screenViewDP(mActivity, R.dimen._70dp))
                applyTo(vYoutubeLayout)
            }
        }
        vYoutubeLayout.visibility = View.VISIBLE
    }

    private fun initYoutubePlayerSeekBar() {
        val thumbView: Drawable? =
            ContextCompat.getDrawable(mActivity, R.drawable.seekbar_youtube_thumb)
        thumbView?.let {
            it.apply {
                bounds = Rect(0, 0, thumbView.intrinsicWidth, thumbView.intrinsicHeight)
            }
        }
        vYoutubePlayerSeekBar?.let { ytSeekbar ->
            ytSeekbar.apply {
                setFontSize(0f)
                videoCurrentTimeTextView.visibility = View.GONE
                videoDurationTextView.visibility = View.GONE
                seekBar.let {
                    it.apply {
                        progressDrawable =
                            ContextCompat.getDrawable(mActivity, R.drawable.seekbar_youtube)
                        thumb = thumbView
                        setPaddingRelative(0, 0, 0, 0)
                        thumbOffset = 0
                    }
                }
            }
        }
    }

    fun disableController() {
        vYoutubeControllerLayout?.visibility = View.GONE
        vYoutubePlayerSeekBar?.visibility = View.GONE
    }

    fun enableController() {
        vYoutubeControllerLayout?.visibility = View.VISIBLE
        vYoutubePlayerSeekBar?.visibility = View.VISIBLE
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
                        //滑動時讓影片暫停
                        vYoutubePlayer?.pause()

                        //控制滑動時Controller顯示
                        vYoutubeControllerBar?.visibility = VISIBLE
                        vYoutubeReturn?.visibility = INVISIBLE
                        vYoutubeNext?.visibility = INVISIBLE
                        vYoutubePlay?.visibility = INVISIBLE
                        vYoutubeMute?.visibility = INVISIBLE
                        vYoutubeFullScreen?.visibility = INVISIBLE

                        //時間邏輯
                        scrollingTimeResult =
                            (startPoint + (videoDurationFloat / 1500) * (e2.x - e1.x))
                        vYoutubePlayerSeekBar?.seekBar?.progress = scrollingTimeResult!!.toInt()
                        //改變黑幕大小
                        val parent = vVideoCover?.parent
                        val parentWidth = (parent as View).width.toFloat()
                        if (videoDurationFloat != 0F) {
                            vVideoCover?.layoutParams?.width =
                                (parentWidth * (scrollingTimeResult!! / videoDurationFloat)).toInt()
                        }
                        vVideoCover?.visibility = VISIBLE
                    }

                    mIsScrolling = true
                    return true
                }

                override fun onDown(e: MotionEvent?): Boolean {
                    return true
                }
            })

        val mGestureListener = OnTouchListener { v, event ->
            //讓滑動影片時，畫面不會亂跑
            v.parent.requestDisallowInterceptTouchEvent(true)
            if (gestureDetector.onTouchEvent(event)) {
                return@OnTouchListener true
            }
            if (event.action == MotionEvent.ACTION_UP) {
                //滑動結束要做的事
                if (mIsScrolling) {
                    mIsScrolling = false

                    //滑動結束時紀錄時間並播放
                    vYoutubePlayer?.seekTo(scrollingTimeResult!!)
                    vYoutubePlayer?.play()

                    //紀錄滑動結束時間
                    val currentTimeString =
                        vYoutubePlayerSeekBar?.videoCurrentTimeTextView?.text.toString()
                    vTimeBar?.text = ("$currentTimeString / $videoDuration")
                    val currentTimeList = currentTimeString.split(":")
                    val currentTimeFloat =
                        currentTimeList[0].toFloat() * 60 + currentTimeString[1].toFloat()
                    startPoint = currentTimeFloat

                    //控制滑動結束時controller顯示
                    vVideoCover?.visibility = INVISIBLE
                    handler.postDelayed({
                        vYoutubeReturn?.visibility = VISIBLE
                        vYoutubeNext?.visibility = VISIBLE
                        vYoutubePlay?.visibility = VISIBLE
                        vYoutubeMute?.visibility = VISIBLE
                        vYoutubeFullScreen?.visibility = VISIBLE
                    }, 350)
                    if (isStateController) {
                        vYoutubeControllerBar?.visibility = View.VISIBLE
                    } else {
                        vYoutubeControllerBar?.visibility = View.INVISIBLE
                    }

                } else {
                    onClick(vController)
                }
            }

            false
        }
        vController?.setOnTouchListener(mGestureListener)
    }

}
