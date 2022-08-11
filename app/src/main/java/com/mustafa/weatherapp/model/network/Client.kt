package com.mustafa.weatherapp.model.network

import com.google.gson.Gson
import com.mustafa.weatherapp.model.response.Weather
import com.mustafa.weatherapp.util.Constants
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request

object Client {
    private val httpUrl = buildHttpUrl()
    private val okHttpClient = OkHttpClient()
    private val gson = Gson()

    fun requestWeatherData(): State<Weather> {
        val request = buildRequest()
        val response = executeRequest(request)

        return if (response.isSuccessful) {
            val weather = gson.fromJson(response.body!!.string(), Weather::class.java)
            State.Success(weather)
        } else {
            State.Fail(response.message)
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
            .addQueryParameter(keys.TIMESTEPS, values.ONE_HOUR)
            .addQueryParameter(keys.UNITS, values.METRIC)
            .addQueryParameter(keys.START_TIME, values.NOW)
            .addQueryParameter(keys.END_TIME, values.NOW_PLUS_6H)
            .addQueryParameter(keys.API_KEY, values.API_KEY)
            .build()
    }

    private fun buildRequest() = Request.Builder().url(httpUrl).build()

    private fun executeRequest(request: Request) = okHttpClient.newCall(request).execute()

}