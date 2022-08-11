package com.mustafa.weatherapp

import com.mustafa.weatherapp.model.network.Client
import com.mustafa.weatherapp.model.response.Weather
import com.mustafa.weatherapp.util.State
import io.reactivex.rxjava3.core.Observable

object WeatherRepository {
    fun getWeather(): Observable<State<Weather>> {
        return getWeatherInfo().flatMap {
            when (it) {
                is State.Fail -> {
                    Observable.create { emitter ->
                        emitter.onNext(it)
                        emitter.onComplete()
                    }
                }
                is State.Loading -> {
                    Observable.create { emitter ->
                        emitter.onNext(it)
                        emitter.onComplete()
                    }
                }
                is State.Success -> {
                    Observable.create { emitter ->
                        emitter.onNext(it)
                        emitter.onComplete()
                    }
                }
            }
        }

    }

    private fun getWeatherInfo(): Observable<State<Weather>> {
        return Observable.create { emitter ->
            emitter.apply {
                onNext(State.Loading)
                onNext(Client.requestWeatherData())
                onComplete()
            }
        }
    }

}