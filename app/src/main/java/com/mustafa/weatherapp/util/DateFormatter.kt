package com.mustafa.weatherapp.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

class DateFormatter {
    @SuppressLint("SimpleDateFormat")
    fun formatCurrentDate(): String {
        val currentDate = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat("MMM d, h:mm a")
        return simpleDateFormat.format(currentDate.time)
    }
}