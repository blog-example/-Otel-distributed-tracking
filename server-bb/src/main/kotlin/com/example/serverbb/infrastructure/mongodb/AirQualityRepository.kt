package com.example.serverbb.infrastructure.mongodb

import com.example.serverbb.application.domain.AirQualityLog
import org.slf4j.LoggerFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Repository

@Repository
class AirQualityRepository(
    private val mongoTemplate: MongoTemplate
) {
    private val log = LoggerFactory.getLogger(AirQualityRepository::class.java)

    fun save(airQualityLog: AirQualityLog): AirQualityLog {
        log.info("AirQualityRepository.save() started")
        val result = mongoTemplate.save(airQualityLog)
        log.info("AirQualityRepository.save() finished")
        return result
    }
}
