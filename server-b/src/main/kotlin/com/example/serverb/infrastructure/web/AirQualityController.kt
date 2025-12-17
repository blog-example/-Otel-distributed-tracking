package com.example.serverb.infrastructure.web

import com.example.serverb.application.service.airquality.AirQualityService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class AirQualityController(
    private val airQualityService: AirQualityService
) : AirQualityAPI {

    private val log = LoggerFactory.getLogger(AirQualityController::class.java)

    override fun record(request: AirQualityAPI.RecordRequest): Mono<AirQualityAPI.RecordResponse> {
        log.info("record start: deviceId=${request.deviceId}")

        val payload = AirQualityService.RecordPayload(
            longitude = request.longitude,
            latitude = request.latitude,
            pollutants = request.pollutants.map {
                AirQualityService.PollutantPayload(it.type, it.value)
            },
            deviceId = request.deviceId
        )

        return airQualityService.record(payload)
            .map { result ->
                val response = AirQualityAPI.RecordResponse(id = result.id)
                log.info("record end: id=${response.id}")
                response
            }
    }
}
