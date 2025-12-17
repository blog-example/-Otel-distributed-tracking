package com.example.serverbb.infrastructure.kafka

import com.example.serverbb.application.service.airquality.AirQualityService
import com.example.serverbb.infrastructure.kafka.message.AirQualityPayload
import com.example.serverbb.infrastructure.kafka.message.Message
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class AirQualityListenerImpl(
    private val airQualityService: AirQualityService
) : AirQualityListener {

    private val log = LoggerFactory.getLogger(AirQualityListenerImpl::class.java)

    @KafkaListener(
        topics = ["\${kafka.topic.air-quality}"],
        containerFactory = "kafkaListenerContainerFactory"
    )
    override fun consume(message: Message<AirQualityPayload>) {
        log.info("Processing air quality message: messageId=${message.messageId}, deviceId=${message.payload.deviceId}")

        val payload = message.payload
        val recordPayload = AirQualityService.RecordPayload(
            longitude = payload.longitude,
            latitude = payload.latitude,
            pollutants = payload.pollutants.map {
                AirQualityService.PollutantPayload(it.type, it.value)
            },
            deviceId = payload.deviceId
        )

        val result = airQualityService.record(recordPayload)
        log.info("Saved air quality log: id=${result.id}, correlationId=${message.correlationId}")
    }
}
