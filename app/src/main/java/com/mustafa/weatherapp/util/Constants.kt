package com.mustafa.weatherapp.util

object Constants {
    object HttpUrl {
        const val SCHEME = "https"
        const val HOST = "api.tomorrow.io"
        const val PATH_SEGMENTS = "v4/timelines"

        object Keys {
            const val LOCATION = "location"
            const val FIELDS = "fields"
            const val TIMESTEPS = "timesteps"
            const val UNITS = "units"
            const val START_TIME = "startTime"
            const val END_TIME = "endTime"
            const val API_KEY = "apikey"
        }

        object Values {
            const val IRAQ_LAT = "33.312805"
            const val IRAQ_LONG = "44.361488"
            const val IRAQ_MISAN_LAT = "31.833332"
            const val IRAQ_MISAN_LONG = "47.150002"
            const val TEMPERATURE = "temperature"
            const val HUMIDITY = "humidity"
            const val WIND_SPEED = "windSpeed"
            const val WEATHER_CODE = "weatherCode"
            const val RAIN_INTENSITY = "rainIntensity"
            const val ONE_HOUR = "1h"
            const val METRIC = "metric"
            const val NOW = "now"
            const val NOW_PLUS_6H = "nowPlus6h"
            const val API_KEY = "OxBxY6VWk60fGuCUVpBFMFMysYVhDdZR"
        }
    }


}