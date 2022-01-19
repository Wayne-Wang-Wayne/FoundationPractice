package com.set.app.entertainment.videomanager

import android.app.Activity
import android.app.Dialog
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.setDDG.util.MatomoTracker
import com.setDDG.util.MatomoTracker.trackEvent
import java.util.*

class VideoPlayCallback {
    interface UpdatePlayListener {
        fun onUpdate(position: Int)
    }

    interface UpdateVideoListener {
        fun onUpdate()
    }

    interface ReadyVideoListener {
        fun onUpdate()
    }

    companion object {
        var isFullScreen: Boolean = false
        var isMute: Boolean = true
        var videoList: HashMap<Int, VideoManager> = hashMapOf()
        var isPlayPosition: Int = -1
        var videoManager: VideoManager? = null
        var videoAPIList: ArrayList<VideoModel> = arrayListOf() //上下集按鈕data
        var videoRecyclerView: RecyclerView? = null
        var videoContentViewPager: ViewPager2? = null

        //開啟全螢幕
        fun showFullScreenDialog(activity: Activity, vPlayerView: View?, dialog: Dialog?) {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            vPlayerView?.parent?.let { (vPlayerView.parent as ViewGroup).removeView(vPlayerView) }

            dialog?.let { d ->
                val vPlayerConstraint = ConstraintLayout(activity)
                vPlayerConstraint.id = View.generateViewId()
                vPlayerConstraint.layoutParams =
                    ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
                        ConstraintLayout.LayoutParams.MATCH_PARENT)
                vPlayerView?.layoutParams =
                    ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
                        ConstraintLayout.LayoutParams.MATCH_PARENT)
                vPlayerConstraint.addView(vPlayerView)
                d.setContentView(vPlayerConstraint)
                d.window?.decorView?.apply {
                    systemUiVisibility =
                        (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE)
                }
                if (!activity.isFinishing && !activity.isDestroyed) {
                    d.show()
                }
            }
            isFullScreen = true
        }

        //關閉全螢幕
        fun closeFullScreenDialog(activity: Activity, dialog: Dialog?) {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            dialog?.dismiss()
            isFullScreen = false
        }

        //確保列表的開始播放只有一部影片
        fun onUpdatePlay(): UpdatePlayListener {
            return object : UpdatePlayListener {
                override fun onUpdate(position: Int) {
                    videoList.filterKeys {
                        if (position != it) {
                            videoList[it]?.playerToPause()
                        }
                        true
                    }
                    isPlayPosition = position
                }
            }
        }

        //滾動時exoplayer離開畫面暫停播放
        fun onStopPlay(): UpdatePlayListener {
            return object : UpdatePlayListener {
                override fun onUpdate(position: Int) {
                    videoList[position]?.playerToPause()
                    isPlayPosition = -1
                }
            }
        }

        //滑動暫停影片的功能
        fun onScrollView(recyclerView: RecyclerView) {
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (isPlayPosition != -1) {
                        if (isFullScreen) return
                        if (isPlayPosition < (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() || isPlayPosition > (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()) {
                            onStopPlay().onUpdate(isPlayPosition)
                        }
                    }

                }
            })
        }

////        下一集
//        fun onNextVideo(activity: Activity): UpdateVideoListener {
//            return object : UpdateVideoListener {
//                override fun onUpdate() {
//                    trackEvent(activity, MatomoTracker.Category.VIDEO_PLAY_PAGE,
//                        MatomoTracker.Action.CLICK, "下一集")
//                    videoAPIList.add(videoAPIList[0])
//                    videoAPIList.remove(videoAPIList[0])
//                    goVideoPlayerPageActivity(activity)
//                }
//            }
//        }
//
//        //上一集
//        fun onReturnVideo(activity: Activity): UpdateVideoListener {
//            return object : UpdateVideoListener {
//                override fun onUpdate() {
//                    Tracker.trackEvent(activity, Tracker.Category.VIDEO_PLAY_PAGE,
//                        Tracker.Action.CLICK, "上一集")
//                    videoAPIList.add(0, videoAPIList[videoAPIList.size - 1])
//                    videoAPIList.removeAt(videoAPIList.size - 1)
//                    goVideoPlayerPageActivity(activity)
//                }
//            }
//        }

//        fun goVideoPlayerPageActivity(activity: Activity) {
//            val bundle = Bundle()
//            bundle.putSerializable(VideoPlayerPageActivity.KEY_VIDEO_LIST, videoAPIList)
//            IntentUtil.intentToAnyPage(activity, bundle, VideoPlayerPageActivity::class.java)
//            activity.overridePendingTransition(0, 0)
//            activity.finish()
//        }

        //影片準備好
        fun onVideoReady(): ReadyVideoListener {
            return object : ReadyVideoListener {
                override fun onUpdate() {
                    videoManager?.playerToPlay()
                }
            }
        }

//        fun stopVideoRecyclerViewScrolling(flag: Boolean) {
//            (videoRecyclerView?.layoutManager as CustomGridLayoutManager).setScrollEnabled(!flag)
//        }
//
//        fun stopVideoViewPagerScrolling(flag: Boolean){
//            videoContentViewPager?.isUserInputEnabled = !flag
//        }
    }

}
class VideoModel(){

}
