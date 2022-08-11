package com.mustafa.weatherapp.model.reposerties.weather

import com.mustafa.weatherapp.model.network.Client
import com.mustafa.weatherapp.model.response.Weather
import com.mustafa.weatherapp.model.network.State
import io.reactivex.rxjava3.core.Observable

class WeatherRepository {
    fun getWeatherInfo(): Observable<State<Weather>> = Observable.create { emitter ->
        emitter.onNext(State.Loading)
        emitter.onNext(Client.requestWeatherData())
        emitter.onComplete()
    }
}