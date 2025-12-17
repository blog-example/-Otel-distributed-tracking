package com.example.servera.application.domain

interface AirQualityStore {
    fun save(airQualityLog: AirQualityLog): AirQualityLog
}
