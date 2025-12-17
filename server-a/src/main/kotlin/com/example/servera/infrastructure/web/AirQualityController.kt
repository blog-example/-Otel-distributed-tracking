package com.example.servera.infrastructure.web

import com.example.servera.application.service.airquality.AirQualityService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RestController

@RestController
class AirQualityController(
    private val airQualityService: AirQualityService
) : AirQualityAPI {
    private val log = LoggerFactory.getLogger(AirQualityController::class.java)

    override fun record(request: AirQualityAPI.RecordRequest): AirQualityAPI.RecordResponse {
        log.info("AirQualityController.record() started")
        val payload = AirQualityService.RecordPayload(
            longitude = request.longitude,
            latitude = request.latitude,
            pollutants = request.pollutants.map {
                AirQualityService.PollutantPayload(
                    type = it.type,
                    value = it.value
                )
            },
            deviceId = request.deviceId
        )
        val result = airQualityService.record(payload)
        log.info("AirQualityController.record() finished")
        return AirQualityAPI.RecordResponse(id = result.id)
    }
}
