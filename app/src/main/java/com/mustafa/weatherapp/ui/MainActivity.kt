package com.mustafa.weatherapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mustafa.weatherapp.databinding.ActivityMainBinding
import com.mustafa.weatherapp.model.network.State
import com.mustafa.weatherapp.model.reposerties.weather.WeatherRepository
import com.mustafa.weatherapp.model.response.Weather
import com.mustafa.weatherapp.util.DateFormatter
import com.mustafa.weatherapp.util.extensions.add
import com.mustafa.weatherapp.util.extensions.hide
import com.mustafa.weatherapp.util.extensions.show
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val dateFormatter = DateFormatter()
    private val weatherRepository = WeatherRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestWeatherData()
        setOnClickListenersOfViews()
    }

    private fun requestWeatherData() {
        weatherRepository.getWeatherInfo()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { state ->
                    showResponseState(state)
                },
                {
                    showFailState()
                }
            ).add(compositeDisposable)
    }

    private fun showResponseState(responseState: State<Weather>) = when (responseState) {
        is State.Fail -> showFailState()
        is State.Loading -> showLoadingState()
        is State.Success -> showSuccessState(responseData = responseState.data)
    }

    private fun showFailState() {
        binding.apply {
            screenOnSuccess.hide()
            screenOnFail.show()
        }
    }

    private fun showLoadingState() {
        binding.apply {
            loadingGroup.show()
            screenOnSuccess.show()
            screenOnFail.hide()
            weatherResultsGroup.hide()
        }
    }

    private fun showSuccessState(responseData: Weather) {
        binding.apply {
            screenOnSuccess.show()
            weatherResultsGroup.show()
            loadingGroup.hide()
        }

        bindWeatherData(responseData)
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun bindWeatherData(weather: Weather) {
        val formattedDate = dateFormatter.formatCurrentDate()

        val recentWeatherData = weather.data.timelines[0]
        val currentWeatherValues = recentWeatherData.intervals[0].values

        binding.apply {
            textTemperatureNow.text = "${currentWeatherValues.temperature}°"
            textTemperatureToday.text = recentWeatherData.intervals[3].values.temperature
            textWindSpeedToday.text = currentWeatherValues.windSpeed
            textHumidityToday.text = currentWeatherValues.humidity
            textTodayDate.text = "Today, $formattedDate"
        }
    }


    @SuppressLint("ClickableViewAccessibility", "ResourceAsColor")
    private fun setOnClickListenersOfViews() {
        with(binding) {
            lottieReloadButton.setOnClickListener {
                requestWeatherData()
                lottieReloadButton.playAnimation()
            }

            textTryAgain.setOnClickListener {
                requestWeatherData()

                lottieReloadButton.cancelAnimation()
                lottieErrorAnimation.playAnimation()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

}