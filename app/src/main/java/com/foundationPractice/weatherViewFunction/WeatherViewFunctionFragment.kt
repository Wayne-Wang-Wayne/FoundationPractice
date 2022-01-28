package com.foundationPractice.weatherViewFunction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.matteobattilana.weather.PrecipType
import com.rockex6.practiceappfoundation.R
import kotlinx.android.synthetic.main.fragment_weather_view_function.*


class WeatherViewFunctionFragment : Fragment() {

    var weatherSwitchCount = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_weather_view_function, container, false)
    }

    companion object {
        fun newInstance() = WeatherViewFunctionFragment()
    }

    override fun onResume() {
        super.onResume()
        if (weatherSwitchCount == 1) {
            createSnowView()
        } else {
            createRainView()
        }

        switchWeatherBT.setOnClickListener {
            weatherSwitchCount = if (weatherSwitchCount == 1) {
                createRainView()
                2
            } else {
                createSnowView()
                1
            }
        }
    }


    private fun createSnowView() {
        weather_view_one.resetWeather()
        weather_view_one.setWeatherData(PrecipType.SNOW)
        weather_view_one.emissionRate = 2f
        weather_view_one.fadeOutPercent = 0.95f
        weather_view_one.angle = 30
        weather_view_one.scaleFactor = 3F

        weather_view_two.resetWeather()
        weather_view_two.setWeatherData(PrecipType.SNOW)
        weather_view_two.emissionRate = 1f
        weather_view_two.fadeOutPercent = 0.95f
        weather_view_two.angle = 10
        weather_view_two.scaleFactor = 1F
        weather_view_two.visibility = View.VISIBLE

        weather_view_three.resetWeather()
        weather_view_three.setWeatherData(PrecipType.SNOW)
        weather_view_three.emissionRate = 1.5f
        weather_view_three.fadeOutPercent = 0.95f
        weather_view_three.angle = -20
        weather_view_three.scaleFactor = 6F
        weather_view_three.visibility = View.VISIBLE

        weatherBG.setBackgroundResource(R.drawable.snow_view_background)
    }

    private fun createRainView() {
        weather_view_one.resetWeather()
        weather_view_one.setWeatherData(PrecipType.RAIN)

        weather_view_two.resetWeather()
        weather_view_two.visibility = View.INVISIBLE

        weather_view_three.resetWeather()
        weather_view_three.visibility = View.INVISIBLE

        weatherBG.setBackgroundResource(R.drawable.rain_view_background)
    }
}