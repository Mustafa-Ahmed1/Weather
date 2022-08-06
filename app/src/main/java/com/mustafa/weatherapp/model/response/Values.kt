package com.mustafa.weatherapp.model.response

import com.google.gson.annotations.SerializedName

data class Values(
    @SerializedName("temperature") val temperature: String,
    @SerializedName("humidity") val humidity: String,
    @SerializedName("windSpeed") val windSpeed: String,
    @SerializedName("weatherCode") val weatherCode: String,
    @SerializedName("rainIntensity") val rainIntensity: String
)