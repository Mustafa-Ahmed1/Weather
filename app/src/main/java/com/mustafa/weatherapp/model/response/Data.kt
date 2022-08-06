package com.mustafa.weatherapp.model.response

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("timelines") val timelines: List<Timelines>
)
