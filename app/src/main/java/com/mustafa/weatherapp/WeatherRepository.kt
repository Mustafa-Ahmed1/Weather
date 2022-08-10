package com.mustafa.weatherapp

import com.mustafa.weatherapp.model.network.Client
import com.mustafa.weatherapp.model.response.Weather
import com.mustafa.weatherapp.util.Status
import io.reactivex.rxjava3.core.Observable

object WeatherRepository {
    fun getWeather(): Observable<Status<Weather>> {
        return getWeatherInfo().flatMap {
            when (it) {
                is Status.Error -> {
                    Observable.create { emitter ->
                        emitter.onNext(it)
                        emitter.onComplete()
                    }
                }
                is Status.Loading -> {
                    Observable.create { emitter ->
                        emitter.onNext(it)
                        emitter.onComplete()
                    }
                }
                is Status.Success -> {
                    Observable.create { emitter ->
                        emitter.onNext(it)
                        emitter.onComplete()
                    }
                }
            }
        }

    }

    private fun getWeatherInfo(): Observable<Status<Weather>> {
        return Observable.create { emitter ->
            emitter.apply {
                onNext(Status.Loading)
                onNext(Client.requestWeatherData())
                onComplete()
            }
        }
    }

}