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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestWeatherData()
    }


    // تنقل الى كلاس جديد باسم Client
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
            textHumidityToday.text = currentValues.humidity
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

    companion object {
        const val LOG_TAG = "MainActivity"
    }

}