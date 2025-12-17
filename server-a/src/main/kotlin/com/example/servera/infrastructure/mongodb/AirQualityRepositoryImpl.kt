package com.example.servera.infrastructure.mongodb

import com.example.servera.application.domain.AirQualityLog
import org.slf4j.LoggerFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Repository

@Repository
class AirQualityRepositoryImpl(
    private val mongoTemplate: MongoTemplate
) : AirQualityRepository {

    private val log = LoggerFactory.getLogger(AirQualityRepositoryImpl::class.java)

    override fun save(airQualityLog: AirQualityLog): AirQualityLog {
        log.info("save start")
        val result = mongoTemplate.save(airQualityLog)
        log.info("save end")
        return result
    }
}
