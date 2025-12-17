package com.example.servera.infrastructure.mongodb

import com.example.servera.application.domain.AirQualityLog

interface AirQualityRepository {
    fun save(airQualityLog: AirQualityLog): AirQualityLog
}
