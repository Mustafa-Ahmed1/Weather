package com.mustafa.weatherapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mustafa.weatherapp.WeatherRepository
import com.mustafa.weatherapp.databinding.ActivityMainBinding
import com.mustafa.weatherapp.model.response.Weather
import com.mustafa.weatherapp.util.Status
import com.mustafa.weatherapp.util.add
import com.mustafa.weatherapp.util.hide
import com.mustafa.weatherapp.util.show
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getWeatherData()

        binding.lottieReload.setOnClickListener {
            getWeatherData()
            binding.lottieReload.playAnimation()
        }
    }

    private fun getWeatherData() {
        WeatherRepository.getWeather()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    onWeatherResult(it)
                },
                {
                    binding.screenOnFail.show()
                    binding.screenOnSuccess.hide()
                }).add(compositeDisposable)
    }

    private fun onWeatherResult(response: Status<Weather>) {
        hideAllViews()

        when (response) {
            is Status.Error -> {
                binding.screenOnFail.show()
            }

            is Status.Loading -> {
                binding.screenOnSuccess.show()
                getWeatherLoadingLottieViews().forEach { it.show() }
                getWeatherResultViews().forEach { it.hide() }
            }

            is Status.Success -> {
                binding.screenOnSuccess.show()
                getWeatherResultViews().forEach { it.show() }
                getWeatherLoadingLottieViews().forEach { it.hide() }
                bindOnSuccessData(response.data)
            }
        }
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun bindOnSuccessData(weather: Weather) {
        val currentDate = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat("MMM d, h:mm a")
        val formattedDate = simpleDateFormat.format(currentDate.time)

        val currentWeatherValues = weather.data.timelines[0].intervals[0].values
        binding.apply {
            textTemperatureNow.text = "${currentWeatherValues.temperature}°"
            textTemperatureToday.text = weather.data.timelines[0].intervals[3].values.temperature
            textWindSpeedToday.text = currentWeatherValues.windSpeed
            textHumidityToday.text = currentWeatherValues.humidity
            textTodayDate.text = "Today, $formattedDate"
        }
    }

    private fun getWeatherLoadingLottieViews(): MutableList<View> {
        with(binding) {
            return mutableListOf(
                lottieTemperatureNowLoading,
                lottieTemperatureTodayLoading,
                lottieHumidityTodayLoading,
                lottieWindSpeedTodayLoading
            )
        }
    }

    private fun getWeatherResultViews(): MutableList<View> {
        with(binding) {
            return mutableListOf(
                textTemperatureNow,
                textTemperatureToday,
                textHumidityToday,
                textWindSpeedToday
            )
        }
    }

    private fun hideAllViews() {
        binding.apply {
            screenOnFail.hide()
            screenOnSuccess.hide()
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