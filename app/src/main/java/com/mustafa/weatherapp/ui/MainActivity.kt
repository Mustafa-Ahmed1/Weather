package com.mustafa.weatherapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mustafa.weatherapp.WeatherRepository
import com.mustafa.weatherapp.databinding.ActivityMainBinding
import com.mustafa.weatherapp.model.response.Weather
import com.mustafa.weatherapp.util.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val dateFormatter = DateFormatter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestWeatherData()
        setOnClickListenersOfViews()
    }

    private fun requestWeatherData() {
        WeatherRepository.getWeather()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { state ->
                    Log.i(LOG_TAG, "onNext: $state")
                    checkRequestState(state)
                },
                {
                    Log.i(LOG_TAG, "onError: ${it.message}")
                    showFailScreen()
                }
            ).add(compositeDisposable)
    }

    private fun checkRequestState(responseState: State<Weather>) {
        hideAllViews()

        when (responseState) {
            is State.Fail -> {
                showFailScreen()
            }
            is State.Loading -> {
                showLoadingScreen()
            }
            is State.Success -> {
                showSuccessScreen(responseState.data)
            }
        }
    }

    private fun hideAllViews() {
        binding.apply {
            screenOnFail.hide()
            screenOnSuccess.hide()
        }
    }

    private fun showFailScreen() {
        binding.apply {
            screenOnSuccess.hide()
            screenOnFail.show()
        }
    }

    private fun showLoadingScreen() {
        binding.screenOnSuccess.show()
        showLottieAnimations()
        hideWeatherResultViews()

        Log.i(LOG_TAG, "loading...")
    }

    private fun showSuccessScreen(response: Weather) {
        showWeatherResultViews()
        hideLottieAnimations()

        bindWeatherData(response)
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun bindWeatherData(weather: Weather) {
        val formattedDate = dateFormatter.formatCurrentDate()
        val currentWeatherValues = weather.data.timelines[0].intervals[0].values

        binding.apply {
            textTemperatureNow.text = "${currentWeatherValues.temperature}°"
            textTemperatureToday.text = weather.data.timelines[0].intervals[3].values.temperature
            textWindSpeedToday.text = currentWeatherValues.windSpeed
            textHumidityToday.text = currentWeatherValues.humidity
            textTodayDate.text = "Today, $formattedDate"
        }
    }

    private fun showLottieAnimations() {
        binding.apply {
            combineViews(
                lottieTemperatureNowLoading, lottieTemperatureTodayLoading,
                lottieHumidityTodayLoading, lottieWindSpeedTodayLoading
            ) { it.show() }
        }
    }

    private fun hideLottieAnimations() {
        binding.apply {
            combineViews(
                lottieTemperatureNowLoading, lottieTemperatureTodayLoading,
                lottieHumidityTodayLoading, lottieWindSpeedTodayLoading
            ) { view -> view.hide() }
        }
    }

    private fun showWeatherResultViews() {
        binding.apply {
            combineViews(
                screenOnSuccess,
                textTemperatureToday,
                textTemperatureNow,
                textHumidityToday,
                textWindSpeedToday,
                textTopTemperatureUnit,
                textTopPercent,
                textTopSpeedUnit
            ) {
                it.show()
            }
        }
    }

    private fun hideWeatherResultViews() {
        binding.apply {
            combineViews(
                textTemperatureToday,
                textTemperatureNow,
                textHumidityToday,
                textWindSpeedToday,
                textTopTemperatureUnit,
                textTopPercent,
                textTopSpeedUnit,
            ) { view ->
                view.hide()
            }
        }
    }

    private fun combineViews(vararg views: View, changeVisibility: (view: View) -> Unit) {
        views.forEach { view ->
            changeVisibility(view)
        }
    }


    @SuppressLint("ClickableViewAccessibility", "ResourceAsColor")
    private fun setOnClickListenersOfViews() {
        binding.apply {
            lottieReload.setOnClickListener {
                requestWeatherData()
                binding.lottieReload.playAnimation()
            }

            textTryAgain.setOnClickListener {
                requestWeatherData()
                binding.lottieAnimationView.playAnimation()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }


    companion object {
        const val LOG_TAG = "MainActivity"
    }
}