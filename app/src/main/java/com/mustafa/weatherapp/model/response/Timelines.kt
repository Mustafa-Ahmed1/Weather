package com.mustafa.weatherapp.model.response

import com.google.gson.annotations.SerializedName

data class Timelines(
    @SerializedName("timestep") val timestep: String,
    @SerializedName("endTime") val endTime: String,
    @SerializedName("startTime") val startTime: String,
    @SerializedName("intervals") val intervals: List<Intervals>
)