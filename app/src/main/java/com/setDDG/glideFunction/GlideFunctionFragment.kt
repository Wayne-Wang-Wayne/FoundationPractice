package com.setDDG.glideFunction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rockex6.practiceappfoundation.R
import com.setDDG.util.loadImage
import kotlinx.android.synthetic.main.fragment_glide_function.*


class GlideFunctionFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_glide_function, container, false)
    }

    companion object {
       fun newInstance() = GlideFunctionFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        catImage.loadImage(this,"https://cataas.com/cat")
    }

    override fun onResume() {
        super.onResume()
        catImage.loadImage(this,"https://cataas.com/cat")
    }
}