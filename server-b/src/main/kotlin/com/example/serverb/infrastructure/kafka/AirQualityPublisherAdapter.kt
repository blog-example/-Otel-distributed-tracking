package com.example.serverb.infrastructure.kafka

import com.example.serverb.application.domain.AirQualityPublisher
import com.example.serverb.infrastructure.kafka.message.AirQualityPayload
import com.example.serverb.infrastructure.kafka.message.Message
import com.example.serverb.infrastructure.kafka.message.PollutantPayload
import com.example.serverb.infrastructure.kafka.producer.AirQualityProducer
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.time.Instant
import java.util.UUID

@Component
class AirQualityPublisherAdapter(
    private val airQualityProducer: AirQualityProducer,

    @Value("\${spring.application.name}")
    private val applicationName: String
) : AirQualityPublisher {

    private val log = LoggerFactory.getLogger(AirQualityPublisherAdapter::class.java)

    override fun publish(event: AirQualityPublisher.AirQualityEvent): Mono<AirQualityPublisher.PublishResult> {
        log.info("publish start: deviceId=${event.deviceId}")

        val message = Message(
            messageId = UUID.randomUUID().toString(),
            correlationId = UUID.randomUUID().toString(),
            timestamp = Instant.now(),
            source = applicationName,
            payload = AirQualityPayload(
                longitude = event.longitude,
                latitude = event.latitude,
                pollutants = event.pollutants.map {
                    PollutantPayload(it.type, it.value)
                },
                deviceId = event.deviceId
            )
        )

        return airQualityProducer.send(message)
            .map {
                log.info("publish end: messageId=${message.messageId}")
                AirQualityPublisher.PublishResult(messageId = message.messageId)
            }
    }
}
