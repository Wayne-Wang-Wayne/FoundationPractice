package com.setDDG.recyclerViewFunction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rockex6.practiceappfoundation.R
import com.setDDG.weatherViewFunction.WeatherViewFunctionFragment


class RecyclerViewFunctionFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recycler_view_function, container, false)
    }

    companion object {
        fun newInstance() = RecyclerViewFunctionFragment()
    }
}