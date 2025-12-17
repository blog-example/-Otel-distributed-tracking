package com.example.servera.infrastructure.mongodb

import com.example.servera.application.domain.AirQualityLog
import com.example.servera.application.domain.AirQualityStore
import org.springframework.stereotype.Component

@Component
class AirQualityAdapter(
    private val airQualityRepository: AirQualityRepository
) : AirQualityStore {
    override fun save(airQualityLog: AirQualityLog): AirQualityLog {
        return airQualityRepository.save(airQualityLog)
    }
}
