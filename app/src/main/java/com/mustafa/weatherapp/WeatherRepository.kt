package com.mustafa.weatherapp

import com.mustafa.weatherapp.model.response.Weather
import com.mustafa.weatherapp.util.Status
import io.reactivex.rxjava3.core.Observable

object WeatherRepository {
    fun getWeatherForCity(): Observable<Status<Weather>> {
        return getWeatherInfo().flatMap {
            when(it){
                is Status.Error -> {
                    Observable.create{ emitter ->
                        emitter.onNext(it)
                        emitter.onComplete()
                    }
                }
                is Status.Loading -> {
                    Observable.create{ emitter ->
                        emitter.onNext(it)
                        emitter.onComplete()
                    }
                }
                is Status.Success -> {
                    Observable.create { emitter ->
                        emitter.onNext(it) //to make it easier we pick the first city and skip others
                        emitter.onComplete()
                    }
                }
            }
        }

    }

    private fun getWeatherInfo(): Observable<Status<Weather>> {
        return Observable.create { emitter ->
            with(emitter) {
                onNext(Status.Loading)
                onNext(Client.requestWeatherData())
                onComplete()
            }
        }
    }

}