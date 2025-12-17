package com.example.servera.application.service.airquality

import com.example.servera.application.domain.AirQualityLog
import com.example.servera.application.domain.AirQualityStore
import com.example.servera.application.domain.Pollutant
import com.example.servera.application.domain.PollutantType
import org.slf4j.LoggerFactory
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.stereotype.Service

@Service
class AirQualityServiceImpl(
    private val airQualityStore: AirQualityStore
) : AirQualityService {

    private val log = LoggerFactory.getLogger(AirQualityServiceImpl::class.java)

    override fun record(payload: AirQualityService.RecordPayload): AirQualityService.RecordResult {
        log.info("record start: deviceId=${payload.deviceId}")

        val airQualityLog = AirQualityLog(
            location = GeoJsonPoint(payload.longitude, payload.latitude),
            pollutants = payload.pollutants.map {
                Pollutant(
                    type = PollutantType.fromValue(it.type),
                    value = it.value
                )
            },
            deviceId = payload.deviceId
        )
        val saved = airQualityStore.save(airQualityLog)
        val result = AirQualityService.RecordResult(id = saved.id!!)

        log.info("record end: id=${result.id}")
        return result
    }
}
