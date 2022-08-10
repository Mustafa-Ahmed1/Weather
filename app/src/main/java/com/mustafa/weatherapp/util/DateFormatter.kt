package com.mustafa.weatherapp.util

import java.text.SimpleDateFormat
import java.util.*

class DateFormatter {
    fun formatCurrentDate(): String {
        val currentDate = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat("MMM d, h:mm a")
        return simpleDateFormat.format(currentDate.time)
    }
}