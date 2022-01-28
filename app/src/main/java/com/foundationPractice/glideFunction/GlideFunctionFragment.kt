package com.foundationPractice.glideFunction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rockex6.practiceappfoundation.R
import com.foundationPractice.util.loadImage
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

        pokemonImageView.loadImage(this,
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${(1..151).random()}.png")
        randomButton.setOnClickListener {
            pokemonImageView.loadImage(this,
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${(1..151).random()}.png")
        }
    }


}