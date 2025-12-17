package com.example.serverbb.infrastructure.mongodb

import com.example.serverbb.application.domain.AirQualityLog
import com.example.serverbb.application.domain.AirQualityStore
import org.springframework.stereotype.Component

@Component
class AirQualityAdapter(
    private val airQualityRepository: AirQualityRepository
) : AirQualityStore {
    override fun save(airQualityLog: AirQualityLog): AirQualityLog {
        return airQualityRepository.save(airQualityLog)
    }
}
