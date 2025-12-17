package com.example.serverbb.application.domain

data class Pollutant(
    val type: PollutantType,
    val value: Double
)

enum class PollutantType {
    PM10,
    PM25,
    CO2,
    NO2;

    companion object {
        fun fromValue(value: String): PollutantType {
            return valueOf(value.uppercase())
        }
    }
}
