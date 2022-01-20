package com.setDDG.weatherViewFunction

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.matteobattilana.weather.PrecipType
import com.github.matteobattilana.weather.WeatherView
import com.rockex6.practiceappfoundation.R
import com.setDDG.webViewFunction.WebViewFunctionFragment
import kotlinx.android.synthetic.main.fragment_weather_view_function.*


class WeatherViewFunctionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_weather_view_function, container, false)
    }

    companion object {
        fun newInstance() = WeatherViewFunctionFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        createWeatherView()
    }


    private fun createWeatherView(){
        weather_view_one.setWeatherData(PrecipType.SNOW)
        weather_view_one.emissionRate = 2f
        weather_view_one.fadeOutPercent = 0.95f
        weather_view_one.angle = 30
        weather_view_one.scaleFactor = 3F

        weather_view_two.setWeatherData(PrecipType.SNOW)
        weather_view_two.emissionRate = 1f
        weather_view_two.fadeOutPercent = 0.95f
        weather_view_two.angle = 10
        weather_view_two.scaleFactor = 1F

        weather_view_three.setWeatherData(PrecipType.SNOW)
        weather_view_three.emissionRate = 1.5f
        weather_view_three.fadeOutPercent = 0.95f
        weather_view_three.angle = -20
        weather_view_three.scaleFactor = 6F
    }
}