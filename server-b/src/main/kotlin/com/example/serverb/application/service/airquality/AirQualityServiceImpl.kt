package com.example.serverb.application.service.airquality

import com.example.serverb.application.domain.AirQualityPublisher
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AirQualityServiceImpl(
    private val airQualityPublisher: AirQualityPublisher
) : AirQualityService {

    private val log = LoggerFactory.getLogger(AirQualityServiceImpl::class.java)

    override fun record(payload: AirQualityService.RecordPayload): Mono<AirQualityService.RecordResult> {
        log.info("record start: deviceId=${payload.deviceId}")

        val event = AirQualityPublisher.AirQualityEvent(
            longitude = payload.longitude,
            latitude = payload.latitude,
            pollutants = payload.pollutants.map {
                AirQualityPublisher.PollutantEvent(it.type, it.value)
            },
            deviceId = payload.deviceId
        )

        return airQualityPublisher.publish(event)
            .map { publishResult ->
                val result = AirQualityService.RecordResult(id = publishResult.messageId)
                log.info("record end: id=${result.id}")
                result
            }
    }
}
