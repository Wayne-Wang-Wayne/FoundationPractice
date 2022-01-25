package com.setDDG.dialogFunction

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rockex6.practiceappfoundation.R
import com.setDDG.gridLayoutRecyclerViewFunction.GridLayoutFunctionFragment
import com.setDDG.util.OnSwipeTouchListener
import kotlinx.android.synthetic.main.fragment_dialog_function.*
import kotlinx.android.synthetic.main.test_dialog_one.*


class DialogFunctionFragment : Fragment() {

    private lateinit var mContext :Context


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_function, container, false)
    }

    companion object {
        fun newInstance() = DialogFunctionFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialogFragmentImageView.setOnTouchListener( object :OnSwipeTouchListener(mContext){
            override fun onSwipeTop() {
                super.onSwipeTop()
               val testDialogOne = TestDialogOne(mContext)
                testDialogOne.show()
            }

            override fun onSwipeBottom() {
                super.onSwipeBottom()
            }

            override fun onSwipeLeft() {
                super.onSwipeLeft()
            }

            override fun onSwipeRight() {
                super.onSwipeRight()
            }
        })
    }
}