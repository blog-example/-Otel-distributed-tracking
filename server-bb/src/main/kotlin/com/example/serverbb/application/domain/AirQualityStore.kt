package com.example.serverbb.application.domain

interface AirQualityStore {
    fun save(airQualityLog: AirQualityLog): AirQualityLog
}
