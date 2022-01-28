package com.foundationPractice.dialogFunction

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import com.rockex6.practiceappfoundation.R
import com.foundationPractice.util.loadImage
import kotlinx.android.synthetic.main.test_dialog_one.*

class TestDialogOne(context: Context) : Dialog(context) {

    init {
        init()
    }

    private fun init() {
        val window = window
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.test_dialog_one)

        dialogImageView.loadImage(context,
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${(1..151).random()}.png")

        dialogImageView.setOnClickListener { dismiss() }
        //設定dialog出現在最下面
        val wlp = window.attributes
        wlp.gravity = Gravity.BOTTOM
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT
        wlp.windowAnimations = R.style.DialogAnimationTheme
        window.attributes = wlp
    }

}