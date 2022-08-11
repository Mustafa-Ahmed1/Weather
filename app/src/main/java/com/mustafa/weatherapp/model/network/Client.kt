package com.mustafa.weatherapp.model.network

import com.google.gson.Gson
import com.mustafa.weatherapp.model.response.Weather
import okhttp3.OkHttpClient
import okhttp3.Request

object Client {
    private val httpUrlBuilder = HttpUrlBuilder()
    private val httpUrl = httpUrlBuilder.buildHttpUrl()
    private val okHttpClient = OkHttpClient()
    private val gson = Gson()

    fun requestWeatherData(): State<Weather> {
        val request = buildRequest()
        val response = makeRequest(request)

        return if (response.isSuccessful) {
            val weather = gson.fromJson(response.body!!.string(), Weather::class.java)
            State.Success(weather)
        } else {
            State.Fail(response.message)
        }

    }

    private fun buildRequest() = Request.Builder().url(httpUrl).build()

    private fun makeRequest(request: Request) = okHttpClient.newCall(request).execute()

}