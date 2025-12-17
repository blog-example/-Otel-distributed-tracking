package com.example.serverbb.infrastructure.kafka

import com.example.serverbb.infrastructure.kafka.message.AirQualityPayload
import com.example.serverbb.infrastructure.kafka.message.Message
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class AirQualityDltListenerImpl : AirQualityDltListener {

    private val log = LoggerFactory.getLogger(AirQualityDltListenerImpl::class.java)

    @KafkaListener(
        topics = ["\${kafka.topic.air-quality-dlt}"],
        containerFactory = "dltKafkaListenerContainerFactory"
    )
    override fun consume(message: Message<AirQualityPayload>) {
        log.error("DLT received failed message: messageId=${message.messageId}, correlationId=${message.correlationId}, deviceId=${message.payload.deviceId}")
    }
}
