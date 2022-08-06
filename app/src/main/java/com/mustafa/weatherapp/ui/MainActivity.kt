package com.mustafa.weatherapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.mustafa.weatherapp.databinding.ActivityMainBinding
import com.mustafa.weatherapp.model.response.Weather
import com.mustafa.weatherapp.util.Constants
import okhttp3.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val client = OkHttpClient()
    private val httpUrl = buildHttpUrl()
    private val LOG_TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestWeatherData()
    }

    private fun requestWeatherData() {
        val request = Request.Builder().url(httpUrl).build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                Log.i(LOG_TAG, "fail: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { jsonString ->
                    val weather = Gson().fromJson(jsonString, Weather::class.java)
                    runOnUiThread {
                        bindViews(weather)
                    }
                }
            }
        })
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun bindViews(weather: Weather) {
        val currentDate = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat("MMM d, h:mm a")
        val formattedDate = simpleDateFormat.format(currentDate.time)

        val currentValues = weather.data.timelines[0].intervals[0].values

        binding.apply {
            textTemperatureNow.text = "${currentValues.temperature}°"
            textTemperatureToday.text = weather.data.timelines[0].intervals[3].values.temperature
            textWindSpeedToday.text = currentValues.windSpeed
            textRainToday.text = currentValues.humidity
            textWeatherDescription.text =
                weatherCodeMap[weather.data.timelines[0].intervals[0].values.weatherCode]
            textTodayDate.text = "Today, $formattedDate"
        }
    }

    private fun buildHttpUrl() = with(Constants.HttpUrl) {
        val keys = Constants.HttpUrl.Keys
        val values = Constants.HttpUrl.Values
        HttpUrl.Builder()
            .scheme(SCHEME)
            .host(HOST)
            .addPathSegments(PATH_SEGMENTS)
            .addQueryParameter(
                keys.LOCATION,
                "${values.IRAQ_MISAN_LAT}, ${values.IRAQ_MISAN_LONG}"
            )
            .addQueryParameter(keys.FIELDS, values.TEMPERATURE)
            .addQueryParameter(keys.FIELDS, values.HUMIDITY)
            .addQueryParameter(keys.FIELDS, values.WIND_SPEED)
            .addQueryParameter(keys.FIELDS, values.WEATHER_CODE)
            .addQueryParameter(keys.FIELDS, values.RAIN_INTENSITY)
            .addQueryParameter(keys.TIMESTEPS, values.ONE_HOUR)
            .addQueryParameter(keys.UNITS, values.METRIC)
            .addQueryParameter(keys.START_TIME, values.NOW)
            .addQueryParameter(keys.END_TIME, values.NOW_PLUS_6H)
            .addQueryParameter(keys.API_KEY, values.API_KEY)
            .build()
    }

    private val weatherCodeMap = mapOf(
        "0" to "Unknown",
        "1000" to "Clear, Sunny",
        "1100" to "Mostly Clear",
        "1101" to "Partly Cloudy",
        "1102" to "Mostly Cloudy",
        "1001" to "Cloudy",
        "2000" to "Fog",
        "2100" to "Light Fog",
        "4000" to "Drizzle",
        "4001" to "Rain",
        "4200" to "Light Rain",
        "4201" to "Heavy Rain",
        "5000" to "Snow",
        "5001" to "Flurries",
        "5100" to "Light Snow",
        "5101" to "Heavy Snow",
        "6000" to "Freezing Drizzle",
        "6001" to "Freezing Rain",
        "6200" to "Light Freezing Rain",
        "6201" to "Heavy Freezing Rain",
        "7000" to "Ice Pellets",
        "7101" to "Heavy Ice Pellets",
        "7102" to "Light Ice Pellets",
        "8000" to "Thunderstorm"
    )
}

