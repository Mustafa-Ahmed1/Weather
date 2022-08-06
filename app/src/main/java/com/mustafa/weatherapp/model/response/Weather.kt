package com.mustafa.weatherapp.model.response

import com.google.gson.annotations.SerializedName

data class Weather(
    @SerializedName("data") val data: Data
)