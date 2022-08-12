package com.mustafa.weatherapp.model.network

import com.google.gson.Gson
import com.mustafa.weatherapp.model.response.Weather
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class Client {
    private val httpUrlBuilder = HttpUrlBuilder()
    private val httpUrl = httpUrlBuilder.buildHttpUrl()
    private val okHttpClient = OkHttpClient()
    private val gson = Gson()

    fun requestWeatherData(): State<Weather> {
        val request = buildRequest()
        val response = makeRequest(request)
        return checkResponseState(response)
    }

    private fun buildRequest() = Request.Builder().url(httpUrl).build()

    private fun makeRequest(request: Request) = okHttpClient.newCall(request).execute()

    private fun checkResponseState(response: Response) = if (response.isSuccessful) {
        val weather = gson.fromJson(response.body!!.string(), Weather::class.java)
        State.Success(weather)
    } else {
        State.Fail(response.message)
    }
}