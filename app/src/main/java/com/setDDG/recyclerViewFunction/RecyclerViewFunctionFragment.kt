package com.setDDG.recyclerViewFunction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.rockex6.practiceappfoundation.R
import com.setDDG.weatherViewFunction.WeatherViewFunctionFragment


class RecyclerViewFunctionFragment : Fragment() {

    private lateinit var recyclerViewFunctionViewModel: RecyclerViewFunctionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recycler_view_function, container, false)
    }

    companion object {
        fun newInstance() = RecyclerViewFunctionFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        recyclerViewFunctionViewModel.fetchPokeData()
    }

    private fun init(){
        recyclerViewFunctionViewModel = ViewModelProvider(this).get(RecyclerViewFunctionViewModel::class.java)
    }
}