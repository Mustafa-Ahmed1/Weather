package com.mustafa.weatherapp.model.response

import com.google.gson.annotations.SerializedName

data class Intervals(
    @SerializedName("startTime") val startTime: String,
    @SerializedName("values") val values: Values
)