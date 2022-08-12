package com.mustafa.weatherapp.model.reposerties.weather

import com.mustafa.weatherapp.model.network.Client
import com.mustafa.weatherapp.model.network.State
import com.mustafa.weatherapp.model.response.Weather
import io.reactivex.rxjava3.core.Observable

class WeatherRepository  {
    private val client = Client()

    fun getWeatherInfo(): Observable<State<Weather>> = Observable.create { emitter ->
        emitter.onNext(State.Loading)
        emitter.onNext(client.requestWeatherData())
        emitter.onComplete()
    }
}