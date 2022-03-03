package com.foundationPractice.flexboxLayoutFunction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rockex6.practiceappfoundation.R

//參考這篇：https://www.jianshu.com/p/3c471953e36d
class FlexboxLayoutFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flexbox_layout, container, false)
    }

    companion object {
        fun newInstance() = FlexboxLayoutFragment()
    }
}