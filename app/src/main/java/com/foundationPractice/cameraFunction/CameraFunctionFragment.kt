package com.foundationPractice.cameraFunction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rockex6.practiceappfoundation.R


class CameraFunctionFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_camera_function, container, false)
    }

    companion object {
        fun newInstance() = CameraFunctionFragment()
    }
}