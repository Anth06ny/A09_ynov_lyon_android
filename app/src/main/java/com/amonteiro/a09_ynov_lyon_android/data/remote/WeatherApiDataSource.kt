package com.amonteiro.a09_ynov_lyon_android.data.remote

import com.amonteiro.a09_ynov_lyon_android.domain.model.Weather
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

suspend fun main() {

    for (weather in WeatherApiDataSource.loadWeathers("nice")) {
        println(weather.getResume())
    }
}

object WeatherApiDataSource {

    private val client = HttpClient {
        install(Logging) {
            //(import io.ktor.client.plugins.logging.Logger)
            logger = object : Logger {
                override fun log(message: String) {
                    println(message)
                }
            }
            level = LogLevel.INFO  // TRACE, HEADERS, BODY, etc.
        }
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true }, contentType = ContentType.Any)
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 5000
        }
    }

    suspend fun loadWeathers(cityName:String) : List<Weather> {
        val response = client.get("https://api.openweathermap.org/data/2.5/find?q=$cityName&appid=b80967f0a6bd10d23e44848547b26550&units=metric&lang=fr")

        if (!response.status.isSuccess()) {
            throw Exception("Erreur API: ${response.status} - ${response.bodyAsText()}")
        }

       return  response.body<WeatherResultDTO>().list.map {
            val icon = it.weather.firstOrNull()?.icon ?: ""
            Weather(
                id = it.id,
                name = it.name,
                temp = it.main.temp,
                speed = it.wind.speed,
                description = it.weather.firstOrNull()?.description ?: "-",
                icon = "https://openweathermap.org/img/wn/${icon}@4x.png",
            )
        }
    }

}


/* -------------------------------- */
// WEATHER
/* -------------------------------- */
@kotlinx.serialization.Serializable
data class WeatherResultDTO(val list: List<WeatherDTO>)

@kotlinx.serialization.Serializable
data class WeatherDTO(
    val id: Int, val name: String, var main: TempDTO,
    var weather: List<DescriptionDTO>,
    var wind: WindDTO
)

@kotlinx.serialization.Serializable
data class TempDTO(var temp: Double)

@kotlinx.serialization.Serializable
data class DescriptionDTO(var description: String, var icon: String)

@Serializable
data class WindDTO(var speed: Double)